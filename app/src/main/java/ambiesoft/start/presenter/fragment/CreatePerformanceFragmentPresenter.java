package ambiesoft.start.presenter.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import ambiesoft.start.R;
import ambiesoft.start.model.dataclass.Performance;
import ambiesoft.start.view.activity.MainActivity;
import ambiesoft.start.view.fragment.CreatePerformanceFragment;
import ambiesoft.start.view.fragment.HeatMapFragment;
import ambiesoft.start.view.fragment.MyBuskingFragment;

import static ambiesoft.start.model.utility.AlertBox.showAlertBox;
import static ambiesoft.start.model.utility.BundleItemChecker.getFilterCategoryFromBundle;
import static ambiesoft.start.model.utility.BundleItemChecker.getFilterDateFromBundle;
import static ambiesoft.start.model.utility.BundleItemChecker.getFilterKeywordFromBundle;
import static ambiesoft.start.model.utility.BundleItemChecker.getFilterTimeFromBundle;
import static ambiesoft.start.model.utility.BundleItemChecker.getPerformanceListFromBundle;
import static ambiesoft.start.model.utility.BundleItemChecker.getSelectedPerformanceFromBundle;
import static ambiesoft.start.model.utility.DateFormatter.checkIfTimeIsInBetween;
import static ambiesoft.start.model.utility.DateFormatter.getCurrentDay;
import static ambiesoft.start.model.utility.DateFormatter.getCurrentHour;
import static ambiesoft.start.model.utility.DateFormatter.getCurrentMinute;
import static ambiesoft.start.model.utility.DateFormatter.getCurrentMonth;
import static ambiesoft.start.model.utility.DateFormatter.getCurrentYear;
import static ambiesoft.start.model.utility.DateFormatter.getEndingTimeForPerformance;
import static ambiesoft.start.model.utility.DateFormatter.getSelectedDateWithLeadingZero;
import static ambiesoft.start.model.utility.DateFormatter.getSelectedTimeWithLeadingZero;
import static ambiesoft.start.model.utility.DateFormatter.getTodayDate;
import static ambiesoft.start.model.utility.FirebaseUtility.savePerformance;
import static ambiesoft.start.model.utility.FirebaseUtility.setupFirebase;
import static ambiesoft.start.model.utility.FirebaseUtility.updatePerformance;
import static ambiesoft.start.model.utility.ProgressLoadingDialog.dismissProgressDialog;
import static ambiesoft.start.model.utility.ProgressLoadingDialog.showProgressDialog;

/**
 * Created by Bryanyhy on 23/8/2016.
 */
public class CreatePerformanceFragmentPresenter implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    // for identifying the request involving the usage of Google Place API
    private static final int PLACE_PICKER_REQUEST = 1;

    private CreatePerformanceFragment view;
    private GoogleApiClient mGoogleApiClient;

    private String name;
    private String desc;
    private String selectedCategory;
    private String selectedAddress;
    private Double selectedLat;
    private Double selectedLng;
    private String selectedDate;
    private String selectedSTime;
    private String selectedETime;
    private int selectedDuration;
    private Performance performanceFromPreviousFragment;
    private ArrayList<Performance> performanceListFromPreviousFragment;

    public CreatePerformanceFragmentPresenter() {    }

    public CreatePerformanceFragmentPresenter(CreatePerformanceFragment view) {
        this.view = view;
        selectedDuration = 0;
        getBundleFromPreviousFragment();
        // setup FirebaseUtility
        setupFirebase(view.getContext());
        // Setup Google API Client, used to get the latitude and longitude from user's location selection
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient
                    .Builder(view.getContext())
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .build();
        }
        // connect to google API client
        mGoogleApiClient.connect();
        if (performanceFromPreviousFragment != null) {
            setViewDetailsFromPerformance();
            view.createButton.setText("Update");
            Log.i("System.out", performanceFromPreviousFragment.getKey());
        }
    }

    public void getBundleFromPreviousFragment() {
        // get bundle from previous fragment
        Bundle bundle = view.getArguments();
        if (bundle != null) {
            // if bundle exists, get the filter values
            performanceFromPreviousFragment = getSelectedPerformanceFromBundle(bundle);
            performanceListFromPreviousFragment = getPerformanceListFromBundle(bundle);
        }
    }

    // Method called when user click on the date button
    public void chooseDate() throws ParseException {
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
    public void chooseStartTime() {
        //Set and show current time in the timepicker by default
        int mHour = getCurrentHour();
        int mMinute = getCurrentMinute();
        // TimePickerDialog popup
        TimePickerDialog mTimePicker = new TimePickerDialog(view.getActivity(), new TimePickerDialog.OnTimeSetListener() {
            // When a time is selected on clock
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                // get the selected time, with leading zero when hour or minute is smaller than 2 digits
                selectedSTime = getSelectedTimeWithLeadingZero(selectedHour, selectedMinute);
                Toast.makeText(view.getActivity(), selectedSTime + " is selected.", Toast.LENGTH_SHORT).show();
                // set the text on the time button to be the time chosen
                view.sTimeButton.setText(selectedSTime);
            }
        },mHour, mMinute, true);
        mTimePicker.setTitle("Select time");
        mTimePicker.show();
    }

    // Method called when user click on the duration button
    public void chooseDuration() {
        // setup number picker
        NumberPicker mNumberPicker = new NumberPicker(view.getContext());
        // minimum minute is 1, maximum is 120, as busker can only stay in a certain location to perform for maximum 120 minutes only,
        // according to City of Melbourne
        mNumberPicker.setMaxValue(120);
        mNumberPicker.setMinValue(1);
        // if this fragment is just created
        if (selectedDuration == 0) {
            // set the default duration selection to be 30
            selectedDuration = 30;
            mNumberPicker.setValue(selectedDuration);
        } else {
            // set the default duration value to previously selected one
            mNumberPicker.setValue(selectedDuration);
        }
        NumberPicker.OnValueChangeListener mVCL = new NumberPicker.OnValueChangeListener() {
            // when duration value is selected
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                selectedDuration = newVal;
            }
        };
        mNumberPicker.setOnValueChangedListener(mVCL);
        // Build dialog to hold the number picker
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext()).setView(mNumberPicker);
        builder.setTitle("Performance Duration (Minutes)");
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            // when "ok" in dialog is clicked
            @Override
            public void onClick(DialogInterface dialog, int any) {
                // set the button's text
                view.durationButton.setText(selectedDuration + " minutes");
                Toast.makeText(view.getActivity(), selectedDuration + " minutes is selected.", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    // Method called when location button is clicked
    public void chooseLocation() {
////        // show the loading progress dialog
////        showProgressDialog(view.getContext());
//        // initialise the Google Place API
//        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
//        try {
//            // Start place picker activity on Google Place API
//            view.startActivityForResult(builder.build(view.getActivity()), PLACE_PICKER_REQUEST);
//        } catch (GooglePlayServicesRepairableException e) {
//            e.printStackTrace();
//        } catch (GooglePlayServicesNotAvailableException e) {
//            e.printStackTrace();
//        }
        showProgressDialog(view.getContext());
//        view.getView().setVisibility(View.GONE);
        FragmentTransaction ft = view.getFragmentManager().beginTransaction().hide(view);

        if (performanceFromPreviousFragment != null) {
            Fragment heatMapFragment = new HeatMapFragment();
            Bundle bundle = new Bundle();
            bundle.putDouble("latFromPreviousFragment", selectedLat);
            bundle.putDouble("lngFromPreviousFragment", selectedLng);
            heatMapFragment.setArguments(bundle);

            ft.replace(R.id.content_frame_map, heatMapFragment).addToBackStack(null);
        } else {
            ft.replace(R.id.content_frame_map, new HeatMapFragment()).addToBackStack(null);
        }
        ft.commit();
    }

//    // When a location is selected in Google Place API
//    public void getLocationLatAndLng(int requestCode, int resultCode, Intent data) {
//        if (requestCode == PLACE_PICKER_REQUEST) {
//            if (resultCode == Activity.RESULT_OK) {
//                // get the latitude and longitude of the selected location
//                Place place = PlacePicker.getPlace(data, view.getActivity());
//                String locationName = place.getName().toString();
//                LatLng latLng = place.getLatLng();
//                selectedLat = latLng.latitude;
//                selectedLng = latLng.longitude;
//                // set the button text to be the location name or lat/lng
//                view.locationButton.setText(locationName);
////                // dismiss the load progress dialog
////                dismissProgressDialog();
//            }
//        }
//    }

    // connection status to Google API Client
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        dismissProgressDialog();
    }
    @Override
    public void onConnectionSuspended(int i) {
        showAlertBox("Alert", "Connection to Google API Client is suspended. Please try again later.", view.getActivity());
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        showAlertBox("Alert", "Connection to Google API Client is failed. Please try again later.", view.getActivity());
    }

    // when submit button is clicked
    public void submit() throws ParseException {
        name = view.nameInput.getText().toString().trim();
        desc = view.descInput.getText().toString().trim();
        // valid the input, to see if there are any empty input
        if (validInput()) {
            // if input is valid
            // get the ending time by using start time and duration
            selectedETime = getEndingTimeForPerformance(selectedSTime, selectedDuration);
            if (performanceListFromPreviousFragment != null) {
                if (validPerformanceClash()) {
                    savePerformanceChange();
                } else {
                    // if performance time is clashed, show alertbox
                    showAlertBox("Alert", "There is a time clash with yours existing performance.", view.getActivity());
                }
            } else {
                savePerformanceChange();
            }
        } else {
            // if input is not valid, show alertbox
            showAlertBox("Alert", "No empty field is allowed.", view.getActivity());
        }
    }

    public void savePerformanceChange() {
        // create a new performance object
        Performance performance = new Performance(name, selectedCategory, desc, selectedDate, selectedSTime
                ,selectedETime, selectedDuration, selectedLat, selectedLng, selectedAddress, ((MainActivity) view.getActivity()).getUserEmail());
        if (performanceFromPreviousFragment == null) {
            // save performance into Firebase Utility
            savePerformance(performance);
            Toast.makeText(view.getActivity(), "Performance is created.", Toast.LENGTH_SHORT).show();
        } else {
            // update performance into Firebase Utility
            updatePerformance(performance, performanceFromPreviousFragment.getKey());
            Toast.makeText(view.getActivity(), "Performance is updated.", Toast.LENGTH_SHORT).show();
        }
        // back to MyBuskingFragment
        view.getFragmentManager().beginTransaction().replace(R.id.content_frame, new MyBuskingFragment()).remove(view).commit();
    }

    // validate if the user input is correct
    public boolean validInput() {
        // check on empty input
        if (getName() == null || getName().trim().matches("") || getDesc() == null || getDesc().trim().matches("") || getSelectedLat() == null || getSelectedLng() == null
                || getSelectedDate() == null || getSelectedSTime() == null || getSelectedDuration() == 0 || getSelectedAddress() == null) {
            // if there is empty input
            return false;
        } else {
            // if no empty input
            return true;
        }
    }

    public boolean validPerformanceClash() throws ParseException {
        for (Performance performance: performanceListFromPreviousFragment) {
            if (checkIfTimeIsInBetween(getSelectedDate(), getSelectedSTime(), getSelectedETime(),performance.getDate(), performance.getsTime(), performance.geteTime())) {
                return false;
            }
        }
        return true;
    }

    public void setSelectedCategory(AdapterView<?> parent, int position) {
        // On selecting a spinner item
        selectedCategory = parent.getItemAtPosition(position).toString();
    }

    public void setLocationInfo(String address, Double lat, Double lng) {
        selectedAddress = address;
        selectedLat = lat;
        selectedLng = lng;
        view.locationButton.setText(selectedAddress);
        Toast.makeText(view.getActivity(), selectedAddress + " is selected.", Toast.LENGTH_SHORT).show();
    }

    public void setViewDetailsFromPerformance() {
        name = performanceFromPreviousFragment.getName();
        view.nameInput.setText(name);

        desc = performanceFromPreviousFragment.getDesc();
        view.descInput.setText(desc);

        selectedCategory = performanceFromPreviousFragment.getCategory();
        ArrayAdapter myAdap = (ArrayAdapter) view.categorySpinner.getAdapter();
        int spinnerPosition = myAdap.getPosition(selectedCategory);
        view.categorySpinner.setSelection(spinnerPosition);

        selectedLat = performanceFromPreviousFragment.getLat();
        selectedLng = performanceFromPreviousFragment.getLng();
        selectedAddress = performanceFromPreviousFragment.getAddress();
        view.locationButton.setText(selectedAddress);

        selectedDate = performanceFromPreviousFragment.getDate();
        view.dateButton.setText(selectedDate);

        selectedSTime = performanceFromPreviousFragment.getsTime();
        view.sTimeButton.setText(selectedSTime);

        selectedETime = performanceFromPreviousFragment.geteTime();

        selectedDuration = performanceFromPreviousFragment.getDuration();
        view.durationButton.setText(String.valueOf(selectedDuration));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(String selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    public String getSelectedAddress() {
        return selectedAddress;
    }

    public void setSelectedAddress(String selectedAddress) {
        this.selectedAddress = selectedAddress;
    }

    public Double getSelectedLat() {
        return selectedLat;
    }

    public void setSelectedLat(Double selectedLat) {
        this.selectedLat = selectedLat;
    }

    public Double getSelectedLng() {
        return selectedLng;
    }

    public void setSelectedLng(Double selectedLng) {
        this.selectedLng = selectedLng;
    }

    public String getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }

    public String getSelectedSTime() {
        return selectedSTime;
    }

    public void setSelectedSTime(String selectedSTime) {
        this.selectedSTime = selectedSTime;
    }

    public String getSelectedETime() {
        return selectedETime;
    }

    public void setSelectedETime(String selectedETime) {
        this.selectedETime = selectedETime;
    }

    public int getSelectedDuration() {
        return selectedDuration;
    }

    public void setSelectedDuration(int selectedDuration) {
        this.selectedDuration = selectedDuration;
    }
}
