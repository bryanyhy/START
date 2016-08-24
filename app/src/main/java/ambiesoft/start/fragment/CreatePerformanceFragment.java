package ambiesoft.start.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import java.text.ParseException;
import java.util.Calendar;

import ambiesoft.start.R;
import ambiesoft.start.dataclass.Performance;

import static ambiesoft.start.utility.AlertBox.showAlertBox;
import static ambiesoft.start.utility.DateFormatter.getCurrentDay;
import static ambiesoft.start.utility.DateFormatter.getCurrentHour;
import static ambiesoft.start.utility.DateFormatter.getCurrentMinute;
import static ambiesoft.start.utility.DateFormatter.getCurrentMonth;
import static ambiesoft.start.utility.DateFormatter.getCurrentYear;
import static ambiesoft.start.utility.DateFormatter.getEndingTimeForPerformance;
import static ambiesoft.start.utility.DateFormatter.getSelectedDateWithLeadingZero;
import static ambiesoft.start.utility.DateFormatter.getSelectedTimeWithLeadingZero;
import static ambiesoft.start.utility.Firebase.savePerformance;
import static ambiesoft.start.utility.Firebase.setupFirebase;
import static ambiesoft.start.utility.ProgressLoadingDialog.dismissProgressDialog;
import static ambiesoft.start.utility.ProgressLoadingDialog.showProgressDialog;
import static ambiesoft.start.utility.SoftKeyboard.hideSoftKeyboard;

/**
 * A Fragment for performance creation.
 */
public class CreatePerformanceFragment extends Fragment {

    // for identifying the request involving the usage of Google Place API
    private static final int PLACE_PICKER_REQUEST = 1;

    private CreatePerformanceFragmentPresenter presenter;
    private GoogleApiClient mGoogleApiClient;
    private boolean isStartup = true;
    private String name;
    private String desc;
    private String selectedCategory;
    private Double selectedLat;
    private Double selectedLng;
    private String selectedDate;
    private String selectedSTime;
    private String selectedETime;
    private int selectedDuration;

    public EditText nameInput;
    public EditText descInput;
    private Spinner categorySpinner;
    public Button createButton;
    public Button locationButton;
    public Button dateButton;
    public Button sTimeButton;
    public Button durationButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_performance, container, false);
        nameInput = (EditText) view.findViewById(R.id.nameInput);
        hideSoftKeyboard(getActivity(), nameInput.getWindowToken());
        descInput = (EditText) view.findViewById(R.id.descInput);
        categorySpinner = (Spinner) view.findViewById(R.id.categorySpinner);
        createButton = (Button) view.findViewById(R.id.createButton);
//        createButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                try {
//                    submit();
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
        locationButton = (Button) view.findViewById(R.id.locationButton);
//        locationButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                chooseLocation();
//            }
//        });
        dateButton = (Button) view.findViewById(R.id.dateButton);
//        dateButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                try {
//                    chooseDate();
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
        sTimeButton = (Button) view.findViewById(R.id.sTimeButton);
//        sTimeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                chooseStartTime();
//            }
//        });
        durationButton = (Button) view.findViewById(R.id.durationButton);
//        durationButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                chooseDuration();
//            }
//        });
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (presenter == null) {
            presenter = new CreatePerformanceFragmentPresenter(this);
        }
//        // setup Firebase
//        setupFirebase(getContext());
//        // Setup Google API Client, used to get the latitude and longitude from user's location selection
//        if (mGoogleApiClient == null) {
//            mGoogleApiClient = new GoogleApiClient
//                    .Builder(getContext())
//                    .addApi(Places.GEO_DATA_API)
//                    .addApi(Places.PLACE_DETECTION_API)
//                    .build();
//        }
//        // connect to google API client
//        mGoogleApiClient.connect();
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try {
                    presenter.submit();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                presenter.chooseLocation();
            }
        });
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try {
                    presenter.chooseDate();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        sTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                presenter.chooseStartTime();
            }
        });
        durationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                presenter.chooseDuration();
            }
        });

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.category_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        categorySpinner.setAdapter(adapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                presenter.setSelectedCategory(parent, position);
//                // On selecting a spinner item
//                selectedCategory = parent.getItemAtPosition(position).toString();
//                if (isStartup) {
//                    isStartup = false;
//                } else {
//                    Toast.makeText(getActivity(), selectedCategory + " is selected.", Toast.LENGTH_SHORT).show();
//                }
            }
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

//        selectedDuration = 0;
    }

//    // Method called when user click on the date button
//    public void chooseDate() throws ParseException {
//        // TODO Auto-generated method stub
//        //Set and show current date in the datepicker by default
//        int mYear = getCurrentYear();
//        int mMonth = getCurrentMonth();
//        int mDay = getCurrentDay();
//        // DatePickerDialog popup
//        DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
//            // when a date is selected in calendar
//            public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
//                // get the selected date, with leading zero when day or month is smaller than 2 digits
//                selectedDate = getSelectedDateWithLeadingZero(selectedDay, selectedMonth, selectedYear);
//                Toast.makeText(getActivity(), selectedDate + " is selected.", Toast.LENGTH_SHORT).show();
//                // set the text on the date button to be the date chosen
//                dateButton.setText(selectedDate);
//            }
//        },mYear, mMonth, mDay);
//        // only today or days after today can be selected
//        mDatePicker.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
//        mDatePicker.setTitle("Select date");
//        mDatePicker.show();
//    }

//    // Method called when user click on the time button
//    public void chooseStartTime() {
//        //Set and show current time in the timepicker by default
//        int mHour = getCurrentHour();
//        int mMinute = getCurrentMinute();
//        // TimePickerDialog popup
//        TimePickerDialog mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
//            // When a time is selected on clock
//            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                // get the selected time, with leading zero when hour or minute is smaller than 2 digits
//                selectedSTime = getSelectedTimeWithLeadingZero(selectedHour, selectedMinute);
//                Toast.makeText(getActivity(), selectedSTime + " is selected.", Toast.LENGTH_SHORT).show();
//                // set the text on the time button to be the time chosen
//                sTimeButton.setText(selectedSTime);
//            }
//        },mHour, mMinute, true);
//        mTimePicker.setTitle("Select time");
//        mTimePicker.show();
//    }

//    // Method called when user click on the duration button
//    public void chooseDuration() {
//        // setup number picker
//        NumberPicker mNumberPicker = new NumberPicker(getContext());
//        // minimum minute is 1, maximum is 120, as busker can only stay in a certain location to perform for maximum 120 minutes only,
//        // according to City of Melbourne
//        mNumberPicker.setMaxValue(120);
//        mNumberPicker.setMinValue(1);
//        // if this fragment is just created
//        if (selectedDuration == 0) {
//            // set the default duration selection to be 1
//            selectedDuration = 1;
//            mNumberPicker.setValue(selectedDuration);
//        } else {
//            // set the default duration value to previously selected one
//            mNumberPicker.setValue(selectedDuration);
//        }
//        NumberPicker.OnValueChangeListener mVCL = new NumberPicker.OnValueChangeListener() {
//            // when duration value is selected
//            @Override
//            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
//                selectedDuration = newVal;
//            }
//        };
//        mNumberPicker.setOnValueChangedListener(mVCL);
//        // Build dialog to hold the number picker
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext()).setView(mNumberPicker);
//        builder.setTitle("Performance Duration (Minutes)");
//        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//            // when "ok" in dialog is clicked
//            @Override
//            public void onClick(DialogInterface dialog, int any) {
//                // set the button's text
//                durationButton.setText(selectedDuration + " minutes");
//                Toast.makeText(getActivity(), selectedDuration + " minutes is selected.", Toast.LENGTH_SHORT).show();
//            }
//        });
//        builder.show();
//    }

//    // Method called when location button is clicked
//    public void chooseLocation() {
//        // show the loading progress dialog
//        showProgressDialog(getContext());
//        // initialise the Google Place API
//        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
//        try {
//            // Start place picker activity on Google Place API
//            startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
//        } catch (GooglePlayServicesRepairableException e) {
//            e.printStackTrace();
//        } catch (GooglePlayServicesNotAvailableException e) {
//            e.printStackTrace();
//        }
//    }
//
    // When a location is selected in Google Place API
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        presenter.getLocationLatAndLng(requestCode, resultCode, data);
    }

//    // when submit button is clicked
//    public void submit() throws ParseException {
//        name = nameInput.getText().toString().trim();
//        desc = descInput.getText().toString().trim();
//        // valid the input, to see if there are any empty input
//        if (validInput()) {
//            //if input is valid
//            // get the ending time by using start time and duration
//            selectedETime = getEndingTimeForPerformance(selectedSTime, selectedDuration);
//            // create a new performance object
//            Performance performance = new Performance(name, selectedCategory, desc, selectedDate, selectedSTime
//                    ,selectedETime, selectedLat, selectedLng);
//            // save performance into Firebase
//            savePerformance(performance);
//            Toast.makeText(getActivity(), "Saved.", Toast.LENGTH_SHORT).show();
//            // restart the current fragment, clear all input
//            FragmentTransaction ft = getFragmentManager().beginTransaction();
//            ft.replace(R.id.content_frame, new CreatePerformanceFragment());
//            ft.commit();
//        } else {
//            // if input is not valid, show alertbox
//            showAlertBox("Alert", "No empty field is allowed.", getActivity());
//        }
//    }
//
//    // validate if the user input is correct
//    public boolean validInput() {
//        // check on empty input
//        if (name.matches("") || desc.matches("") || selectedLat == null || selectedLng == null
//                || selectedDate == null || selectedSTime == null || selectedDuration == 0) {
//            // if there is empty input
//            return false;
//        } else {
//            // if no empty input
//            return true;
//        }
//    }

//    // connection status to Google API Client
//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//        dismissProgressDialog();
//    }
//    @Override
//    public void onConnectionSuspended(int i) {
//        dismissProgressDialog();
//        showAlertBox("Alert", "Connection to Google API Client is suspended. Please try again later.", getActivity());
//    }
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//        dismissProgressDialog();
//        showAlertBox("Alert", "Connection to Google API Client is failed. Please try again later.", getActivity());
//    }
}
