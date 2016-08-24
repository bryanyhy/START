package ambiesoft.start.fragment;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.util.Calendar;

import ambiesoft.start.R;

import static ambiesoft.start.utility.BundleItemChecker.getFilterCategoryFromBundle;
import static ambiesoft.start.utility.BundleItemChecker.getFilterDateFromBundle;
import static ambiesoft.start.utility.BundleItemChecker.getFilterKeywordFromBundle;
import static ambiesoft.start.utility.BundleItemChecker.getFilterTimeFromBundle;
import static ambiesoft.start.utility.DateFormatter.getCurrentDay;
import static ambiesoft.start.utility.DateFormatter.getCurrentHour;
import static ambiesoft.start.utility.DateFormatter.getCurrentMinute;
import static ambiesoft.start.utility.DateFormatter.getCurrentMonth;
import static ambiesoft.start.utility.DateFormatter.getCurrentYear;
import static ambiesoft.start.utility.DateFormatter.getSelectedDateWithLeadingZero;
import static ambiesoft.start.utility.DateFormatter.getSelectedTimeWithLeadingZero;
import static ambiesoft.start.utility.DateFormatter.getTodayDate;

/**
 * Created by Bryanyhy on 23/8/2016.
 */
public class FilterResultFragmentPresenter {

    private FilterResultFragment view;

    // The previous called fragment's ID
    // 0 is HomeFragment, 1 is GoogleMapFragment
    private static final int HOME_FRAGMENT = 0;
    private static final int MAP_FRAGMENT = 1;

    private boolean isStartup = true;
    private int requestFragment;
    private String selectedCategory;
    private static String selectedDate;
    private String selectedTime;

    public FilterResultFragmentPresenter(FilterResultFragment view) {
        this.view = view;
    }

    public void getBundleFromPreviousFragment() {
        // get the bundle from previous fragment
        Bundle bundle = view.getArguments();
        // if there is bundle
        if (bundle != null) {
            if (bundle.containsKey("previousFragmentID")) {
                // get the fragment ID on the caller
                requestFragment = bundle.getInt("previousFragmentID");
            }
            if (bundle.containsKey("filterDate")) {
                // get the selected date in the caller
                selectedDate = bundle.getString("filterDate");
            }
        }
        view.dateButton.setText(selectedDate);
    }

    public void setSelectedCategory(AdapterView<?> parent, int position) {
        // On selecting a spinner item
        selectedCategory = parent.getItemAtPosition(position).toString();
        if (isStartup) {
            isStartup = false;
        } else {
            Toast.makeText(view.getActivity(), selectedCategory + " is selected.", Toast.LENGTH_SHORT).show();
        }
    }

    // Method called when user click on the date button
    public void chooseDate() throws ParseException {
        // TODO Auto-generated method stub
        //Set and show current date in the datepicker by default
        int mYear = getCurrentYear();
        int mMonth = getCurrentMonth();
        int mDay = getCurrentDay();
        // DatePickerDialog popup
        DatePickerDialog mDatePicker = new DatePickerDialog(view.getActivity(), new DatePickerDialog.OnDateSetListener() {
            // when a date is selected in calendar
            public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                // get the selected date, with leading zero when day or month is smaller than 2 digits
                selectedDate = getSelectedDateWithLeadingZero(selectedDay, selectedMonth, selectedYear);
                Toast.makeText(view.getActivity(), selectedDate + " is selected.", Toast.LENGTH_SHORT).show();
                // set the text on the date button to be the date chosen
                view.dateButton.setText(selectedDate);
            }
        },mYear, mMonth, mDay);
        // only today or days after today can be selected
        mDatePicker.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
        mDatePicker.setTitle("Select date");
        mDatePicker.show();
    }

    // Method called when user click on the time button
    public void chooseTime() {
        //Set and show current time in the timepicker by default
        int mHour = getCurrentHour();
        int mMinute = getCurrentMinute();
        // TimePickerDialog popup
        TimePickerDialog mTimePicker = new TimePickerDialog(view.getActivity(), new TimePickerDialog.OnTimeSetListener() {
            // When a time is selected on clock
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                // get the selected time, with leading zero when hour or minute is smaller than 2 digits
                selectedTime = getSelectedTimeWithLeadingZero(selectedHour, selectedMinute);
                Toast.makeText(view.getActivity(), selectedTime + " is selected.", Toast.LENGTH_SHORT).show();
                // set the text on the time button to be the time chosen
                view.timeButton.setText(selectedTime);
            }
        },mHour, mMinute, true);
        mTimePicker.setTitle("Select time");
        mTimePicker.show();
    }

    // when submit filter choice button is clicked
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
        String keyword = view.keywordInput.getText().toString().trim();
        // Check if input is empty
        if (!keyword.matches("")) {
            // Put keyword into bundle if not empty
            bundle.putString("keywordFromFilter", keyword);
            Log.i("System.out","Keyword added to bundle");
        }
        // Add selected time into bundle if there is any
        if (selectedTime != null) {
            bundle.putString("timeFromFilter", selectedTime);
            Log.i("System.out","Time added to bundle");
        }
        fragment.setArguments(bundle);
        Log.i("System.out","Bundle created from filter result");
        // transact to the caller fragment
        view.getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
    }

}
