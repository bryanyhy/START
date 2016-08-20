package ambiesoft.start.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class CreatePerformanceFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private static final int PLACE_PICKER_REQUEST = 1;

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

    private EditText nameInput;
    private EditText descInput;
    private Spinner categorySpinner;
    private Button createButton;
    private Button locationButton;
    private Button dateButton;
    private Button sTimeButton;
    private Button durationButton;

    public CreatePerformanceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_performance, container, false);
        nameInput = (EditText) view.findViewById(R.id.nameInput);
        descInput = (EditText) view.findViewById(R.id.descInput);
        categorySpinner = (Spinner) view.findViewById(R.id.categorySpinner);
        createButton = (Button) view.findViewById(R.id.createButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try {
                    submit();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        locationButton = (Button) view.findViewById(R.id.locationButton);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                chooseLocation();
            }
        });
        dateButton = (Button) view.findViewById(R.id.dateButton);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try {
                    chooseDate();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        sTimeButton = (Button) view.findViewById(R.id.sTimeButton);
        sTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                chooseStartTime();
            }
        });
        durationButton = (Button) view.findViewById(R.id.durationButton);
        durationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                chooseDuration();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupFirebase(getContext());

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient
                    .Builder(getContext())
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .build();
        }
        mGoogleApiClient.connect();


        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.category_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        categorySpinner.setAdapter(adapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                selectedCategory = parent.getItemAtPosition(position).toString();
                if (isStartup) {
                    isStartup = false;
                } else {
                    Toast.makeText(getActivity(), selectedCategory + " is selected.", Toast.LENGTH_SHORT).show();
                }
            }
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        selectedDuration = 0;
    }

    public void chooseDate() throws ParseException {
        // TODO Auto-generated method stub
        //Set and show current date in the datepicker by default
        int mYear = getCurrentYear();
        int mMonth = getCurrentMonth();
        int mDay = getCurrentDay();

        DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                selectedDate = getSelectedDateWithLeadingZero(selectedDay, selectedMonth, selectedYear);
                Toast.makeText(getActivity(), selectedDate + " is selected.", Toast.LENGTH_SHORT).show();
                dateButton.setText(selectedDate);
            }
        },mYear, mMonth, mDay);
        mDatePicker.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
        mDatePicker.setTitle("Select date");
        mDatePicker.show();
    }

    public void chooseStartTime() {
        //Set and show current time in the timepicker by default
        int mHour = getCurrentHour();
        int mMinute = getCurrentMinute();

        TimePickerDialog mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                // check if the hour or minute is smaller than 10, as the leading zero may be missing in such case
                selectedSTime = getSelectedTimeWithLeadingZero(selectedHour, selectedMinute);
                Toast.makeText(getActivity(), selectedSTime + " is selected.", Toast.LENGTH_SHORT).show();
                sTimeButton.setText(selectedSTime);
            }
        },mHour, mMinute, true);
        mTimePicker.setTitle("Select time");
        mTimePicker.show();
    }

    public void chooseDuration() {
        NumberPicker mNumberPicker = new NumberPicker(getContext());
        mNumberPicker.setMaxValue(120);
        mNumberPicker.setMinValue(1);
        if (selectedDuration == 0) {
            selectedDuration = 1;
            mNumberPicker.setValue(selectedDuration);
        } else {
            mNumberPicker.setValue(selectedDuration);
        }
        NumberPicker.OnValueChangeListener mVCL = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                selectedDuration = newVal;
            }
        };
        mNumberPicker.setOnValueChangedListener(mVCL);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext()).setView(mNumberPicker);
        builder.setTitle("Performance Duration (Minutes)");
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int any) {
                durationButton.setText(selectedDuration + " minutes");
                Toast.makeText(getActivity(), selectedDuration + " is selected.", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    public void chooseLocation() {
        showProgressDialog(getContext());
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = PlacePicker.getPlace(data, getActivity());
                String locationName = place.getName().toString();
                LatLng latLng = place.getLatLng();
                selectedLat = latLng.latitude;
                selectedLng = latLng.longitude;
//                Toast.makeText(getActivity(), "Lat: " + selectedLat + ", Lng: " + selectedLng, Toast.LENGTH_LONG).show();
                locationButton.setText(locationName);
                dismissProgressDialog();
            }
        }
    }

    public void submit() throws ParseException {
        name = nameInput.getText().toString().trim();
        desc = descInput.getText().toString().trim();
        if (validInput()) {
            selectedETime = getEndingTimeForPerformance(selectedSTime, selectedDuration);
            Performance performance = new Performance(name, selectedCategory, desc, selectedDate, selectedSTime
                    ,selectedETime, selectedLat, selectedLng );
            savePerformance(performance);
            Toast.makeText(getActivity(), "Saved.", Toast.LENGTH_SHORT).show();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new CreatePerformanceFragment());
            ft.commit();
        } else {
            showAlertBox("Alert", "No empty field is allowed.", getActivity());
        }
    }

    public boolean validInput() {
        if (name.matches("") || desc.matches("") || selectedLat == null || selectedLng == null
                || selectedDate == null || selectedSTime == null || selectedDuration == 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        dismissProgressDialog();
    }

    @Override
    public void onConnectionSuspended(int i) {
        dismissProgressDialog();
        showAlertBox("Alert", "Connection to Google Map is suspended. Please try again later.", getActivity());
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        dismissProgressDialog();
        showAlertBox("Alert", "Connection to Google Map is failed. Please try again later.", getActivity());
    }
}
