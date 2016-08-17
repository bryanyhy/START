package ambiesoft.start.fragment;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
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

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.MapFragment;

import java.text.ParseException;
import java.util.Calendar;

import ambiesoft.start.R;
import ambiesoft.start.dataclass.Performance;

import static ambiesoft.start.utility.AlertBox.showAlertBox;
import static ambiesoft.start.utility.Firebase.savePerformance;
import static ambiesoft.start.utility.Firebase.setupFirebase;

/**
 * A simple {@link Fragment} subclass.
 */
public class FilterResultFragment extends Fragment {

    private static final int HOME_FRAGMENT = 0;
    private static final int MAP_FRAGMENT = 1;

    private boolean isStartup = true;
    private int requestFragment;
    private String selectedCategory;
    private String selectedDate;
    private String selectedSTime;
    private String selectedETime;

    private EditText nameInput;
    private Spinner categorySpinner;
    private Button filterButton;
    private Button dateButton;
    private Button sTimeButton;
    private Button eTimeButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filter_result, container, false);
        nameInput = (EditText) view.findViewById(R.id.nameInput);
        categorySpinner = (Spinner) view.findViewById(R.id.categorySpinner);
        filterButton = (Button) view.findViewById(R.id.filterButton);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try {
                    submit();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
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

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey("requestFragment")) {
                requestFragment = bundle.getInt("requestFragment");
            }
        }

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.performance_category_array_for_filter, android.R.layout.simple_spinner_item);
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
                selectedSTime = selectedHour + ":" + selectedMinute;
                Toast.makeText(getActivity(), selectedSTime + " is selected.", Toast.LENGTH_SHORT).show();
                sTimeButton.setText(selectedSTime);
            }
        },mHour, mMinute, true);
        mTimePicker.setTitle("Select time");
        mTimePicker.show();
    }

    public void submit() throws ParseException {
        Fragment fragment = null;
        if (requestFragment == HOME_FRAGMENT) {
            fragment = new HomeFragment();
        }
        else if (requestFragment == MAP_FRAGMENT){
            fragment = new GoogleMapFragment();
        }
        if (selectedDate != null) {
            Bundle bundle = new Bundle();
            bundle.putString("date",selectedDate);
            fragment.setArguments(bundle);
        }
        getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
    }
}
