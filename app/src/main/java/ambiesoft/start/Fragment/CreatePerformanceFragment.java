package ambiesoft.start.Fragment;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ambiesoft.start.R;

import static ambiesoft.start.Utility.AlertBox.showAlertBox;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreatePerformanceFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private static final int PLACE_PICKER_REQUEST = 1;

    private GoogleApiClient mGoogleApiClient;
    private boolean isStartup = true;
    private String selectedCategory;
    private Double selectedLat;
    private Double selectedLng;
    private String selectedDate;
    private String selectedSTime;
    private String selectedETime;
    private int startOrEndTime;

    private EditText nameInput;
    private EditText descInput;
    private Spinner categorySpinner;
    private Button createButton;
    private Button locationButton;
    private Button dateButton;
    private Button sTimeButton;
    private Button eTimeButton;


    private static final String CATEGORY_TYPE[] = {"Instrument", "Dance", "Magic"};

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
                submit();
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
                startOrEndTime = 0;
                chooseStartTime();
            }
        });
        eTimeButton = (Button) view.findViewById(R.id.eTimeButton);
        eTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                startOrEndTime = 1;
                chooseStartTime();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
    }

    public void chooseDate() throws ParseException {
        // TODO Auto-generated method stub
        //To show current date in the datepicker
        Calendar mCurrentDate = Calendar.getInstance();
        int mYear = mCurrentDate.get(Calendar.YEAR);
        int mMonth = mCurrentDate.get(Calendar.MONTH);
        int mDay = mCurrentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                selectedDate = selectedDay + "-" + (selectedMonth + 1) + "-" + selectedYear;
                Toast.makeText(getActivity(), selectedDate + " is selected.", Toast.LENGTH_SHORT).show();
                dateButton.setText(selectedDate);
            }
        },mYear, mMonth, mDay);
        mDatePicker.getDatePicker().setMinDate(mCurrentDate.getTimeInMillis());
        mDatePicker.setTitle("Select date");
        mDatePicker.show();
    }

    public void chooseStartTime() {
        Calendar mCurrentTime = Calendar.getInstance();
        int mHour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int mMinute = mCurrentTime.get(Calendar.MINUTE);

        TimePickerDialog mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                if (startOrEndTime == 0) {
                    selectedSTime = selectedHour + ":" + selectedMinute;
                    Toast.makeText(getActivity(), selectedSTime + " is selected.", Toast.LENGTH_SHORT).show();
                    sTimeButton.setText(selectedSTime);
                } else {
                    selectedETime = selectedHour + ":" + selectedMinute;
                    Toast.makeText(getActivity(), selectedETime + " is selected.", Toast.LENGTH_SHORT).show();
                    eTimeButton.setText(selectedETime);
                }
            }
        },mHour, mMinute, true);
        mTimePicker.setTitle("Select time");
        mTimePicker.show();
    }

    public void chooseLocation() {
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
                Toast.makeText(getActivity(), "Lat: " + selectedLat + ", Lng: " + selectedLng, Toast.LENGTH_LONG).show();
                locationButton.setText(locationName);
            }
        }
    }

    public void submit() {
        String name = nameInput.getText().toString();
        String desc = descInput.getText().toString();
        if (name.trim().matches("") || desc.trim().matches("") || selectedLat == null || selectedLng == null
                || selectedDate == null || selectedSTime == null || selectedETime == null) {
            // check if any empty field
            showAlertBox("No empty field is allowed.", getActivity());
        } else {
            Toast.makeText(getActivity(), "All input are valid", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
