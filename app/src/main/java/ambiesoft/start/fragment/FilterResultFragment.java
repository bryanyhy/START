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
import static ambiesoft.start.utility.DateFormatter.getCurrentDay;
import static ambiesoft.start.utility.DateFormatter.getCurrentHour;
import static ambiesoft.start.utility.DateFormatter.getCurrentMinute;
import static ambiesoft.start.utility.DateFormatter.getCurrentMonth;
import static ambiesoft.start.utility.DateFormatter.getCurrentYear;
import static ambiesoft.start.utility.DateFormatter.getSelectedDateWithLeadingZero;
import static ambiesoft.start.utility.DateFormatter.getSelectedTimeWithLeadingZero;
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
    private static String selectedDate;
    private String selectedTime;
//    private String selectedETime;
//    private Integer sTimeHour;
//    private Integer sTimeMinute;
//    private Integer eTimeHour;
//    private Integer eTimeMinute;

    private EditText keywordInput;
    private Spinner categorySpinner;
    private Button filterButton;
    private Button dateButton;
    private Button timeButton;
    private Button eTimeButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filter_result, container, false);
        keywordInput = (EditText) view.findViewById(R.id.keywordInput);
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
        timeButton = (Button) view.findViewById(R.id.timeButton);
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                chooseTime();
            }
        });
//        eTimeButton = (Button) view.findViewById(R.id.eTimeButton);
//        eTimeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                chooseEndTime();
//            }
//        });
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey("previousFragmentID")) {
                requestFragment = bundle.getInt("previousFragmentID");
            }
            if (bundle.containsKey("filterDate")) {
                selectedDate = bundle.getString("filterDate");
            }
        }
        dateButton.setText(selectedDate);

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

    public void chooseTime() {
        //Set and show current time in the timepicker by default
        int mHour = getCurrentHour();
        int mMinute = getCurrentMinute();

        TimePickerDialog mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                // check if the hour or minute is smaller than 10, as the leading zero may be missing in such case
                selectedDate = getSelectedTimeWithLeadingZero(selectedHour, selectedMinute);
                Toast.makeText(getActivity(), selectedTime + " is selected.", Toast.LENGTH_SHORT).show();
                timeButton.setText(selectedTime);
            }
        },mHour, mMinute, true);
        mTimePicker.setTitle("Select time");
        mTimePicker.show();
    }

//    public void chooseEndTime() {
//        Calendar mCurrentTime = Calendar.getInstance();
//        int mHour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
//        int mMinute = mCurrentTime.get(Calendar.MINUTE);
//
//        TimePickerDialog mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
//            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                eTimeHour = selectedHour;
//                eTimeMinute = selectedMinute;
//                if (sTimeHour != null) {
//                    if (timeIsValid(sTimeHour, sTimeMinute, eTimeHour, eTimeMinute)) {
//                        // check if the hour or minute is smaller than 10, as the leading zero may be missing in such case
//                        if (selectedHour < 10 && selectedMinute < 10) {
//                            // add leading 0 to both hour and minute
//                            selectedETime = "0" + selectedHour + ":0" + selectedMinute;
//                        } else if (selectedHour < 10) {
//                            // add leading 0 to hour
//                            selectedETime = "0" + selectedHour + ":" + selectedMinute;
//                        } else if (selectedMinute < 10) {
//                            // add leading 0 to minute
//                            selectedETime = selectedHour + ":0" + selectedMinute;
//                        } else {
//                            // no leading 0 is needed
//                            selectedETime = selectedHour + ":" + selectedMinute;
//                        }
//                        selectedETime = selectedHour + ":" + selectedMinute;
//                        Toast.makeText(getActivity(), selectedETime + " is selected.", Toast.LENGTH_SHORT).show();
//                        sTimeButton.setText(selectedETime);
//                    }
//                }
////
////                // check if the hour or minute is smaller than 10, as the leading zero may be missing in such case
////                if (selectedHour < 10 && selectedMinute < 10) {
////                    // add leading 0 to both hour and minute
////                    selectedETime = "0" + selectedHour + ":0" + selectedMinute;
////                } else if (selectedHour < 10) {
////                    // add leading 0 to hour
////                    selectedETime = "0" + selectedHour + ":" + selectedMinute;
////                } else if (selectedMinute < 10) {
////                    // add leading 0 to minute
////                    selectedETime = selectedHour + ":0" + selectedMinute;
////                } else {
////                    // no leading 0 is needed
////                    selectedETime = selectedHour + ":" + selectedMinute;
////                }
////                selectedETime = selectedHour + ":" + selectedMinute;
////                Toast.makeText(getActivity(), selectedETime + " is selected.", Toast.LENGTH_SHORT).show();
////                sTimeButton.setText(selectedETime);
//            }
//        },mHour, mMinute, true);
//        mTimePicker.setTitle("Select time");
//        mTimePicker.show();
//    }

//    private boolean timeIsValid(Integer sTimeHour, Integer sTimeMinute, Integer eTimeHour, Integer eTimeMinute) {
//        if (sTimeHour > eTimeHour) {
//            return false;
//        } else {
//            if ((sTimeHour == eTimeHour) && (sTimeMinute >= eTimeMinute)) {
//                return false;
//            }
//        }
//        return true;
//    }

    public void submit() throws ParseException {
        Fragment fragment = null;
        // Identify the previous fragment, and transact the bundle back
        if (requestFragment == HOME_FRAGMENT) {
            fragment = new HomeFragment();
        }
        else if (requestFragment == MAP_FRAGMENT){
            fragment = new GoogleMapFragment();
        }
        // Create new bundle to pass
        Bundle bundle = new Bundle();
        // Add date into bundle, as it is a must selected item in the filter
        bundle.putString("dateFromFilter", selectedDate);
        Log.i("System.out","Date added to bundle");
        // Add the selected category into bundle if it is not "Any"
        if (!selectedCategory.matches("Any")) {
            bundle.putString("categoryFromFilter", selectedCategory);
            Log.i("System.out","Category added to bundle");
        }
        // Get the keyword from user input
        String keyword = keywordInput.getText().toString().trim();
        // Check if input is empty
        if (!keyword.matches("")) {
            // Put keyword into bundle if not empty
            bundle.putString("keywordFromFilter", keyword);
            Log.i("System.out","Keyword added to bundle");
        }
        if (selectedTime != null) {
            bundle.putString("timeFromFilter", selectedTime);
            Log.i("System.out","Time added to bundle");
        }
//        if (selectedETime != null) {
//            bundle.putString("eTimeFromFilter", selectedETime);
//        }
        fragment.setArguments(bundle);
        Log.i("System.out","Bundle created from filter result");
        getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
    }
}
