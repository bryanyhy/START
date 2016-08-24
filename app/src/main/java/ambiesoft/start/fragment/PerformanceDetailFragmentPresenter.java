package ambiesoft.start.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;

import ambiesoft.start.R;
import ambiesoft.start.dataclass.Performance;

import static ambiesoft.start.utility.AlertBox.showAlertBox;
import static ambiesoft.start.utility.BundleItemChecker.getFilterCategoryFromBundle;
import static ambiesoft.start.utility.BundleItemChecker.getFilterDateFromBundle;
import static ambiesoft.start.utility.BundleItemChecker.getFilterKeywordFromBundle;
import static ambiesoft.start.utility.BundleItemChecker.getFilterTimeFromBundle;
import static ambiesoft.start.utility.BundleItemChecker.getPreviousFragmentIDFromBundle;
import static ambiesoft.start.utility.BundleItemChecker.getSelectedPerformanceFromBundle;
import static ambiesoft.start.utility.DateFormatter.getTodayDate;

/**
 * Created by Bryanyhy on 23/8/2016.
 */
public class PerformanceDetailFragmentPresenter {

    private PerformanceDetailFragment view;

    private Performance selectedPerformance;
    private int previousFragmentID;
    private String filterDate;
    private String filterKeyword;
    private String filterCategory;
    private String filterTime;

    public PerformanceDetailFragmentPresenter(PerformanceDetailFragment view) {
        this.view = view;
    }

    public void getBundleFromPreviousFragment() {
        Bundle bundle = view.getArguments();
        if (bundle != null) {
            // if bundle exists, get the filter values
            filterDate = getFilterDateFromBundle(bundle);
            filterKeyword = getFilterKeywordFromBundle(bundle);
            filterCategory = getFilterCategoryFromBundle(bundle);
            filterTime = getFilterTimeFromBundle(bundle);
            selectedPerformance = getSelectedPerformanceFromBundle(bundle);
            previousFragmentID = getPreviousFragmentIDFromBundle(bundle);
            // set the textView from data in bundle accordingly
            view.nameText.setText(selectedPerformance.getName());
            view.categoryText.setText(selectedPerformance.getCategory());
            view.dateText.setText(selectedPerformance.getDate());
            view.timeText.setText(selectedPerformance.getsTime() + " - " + selectedPerformance.geteTime());
            view.descText.setText(selectedPerformance.getDesc());
        }
    }

    // Method called when user clicked the "back" button
    public void backToPreviousFragment() {
        Log.i("System.out","ID:" + previousFragmentID);
        // check the previous fragment ID
        if (previousFragmentID == 0) {
            // back to HomeFragment if it is the previous one
            view.getFragmentManager().popBackStack();
        } else if (previousFragmentID == 1) {
            // if it is GoogleMapFragment, pop back will call errors
            // so we have to restart the googleMapFragment
            Fragment googleMapFragment = new GoogleMapFragment();
            // create the bundle with previous filter settings
            Bundle bundle = new Bundle();
            bundle.putString("dateFromFilter", filterDate);
            bundle.putString("keywordFromFilter", filterKeyword);
            bundle.putString("categoryFromFilter", filterCategory);
            bundle.putString("timeFromFilter", filterTime);
            googleMapFragment.setArguments(bundle);
            // pass the bundle to a new googleMapFragment
            view.getFragmentManager().beginTransaction().replace(R.id.content_frame, googleMapFragment).commit();
        } else {
            // show alertbox if the previous fragment ID is not valid
            showAlertBox("Error", "Unexpected error occurs. Please restart the app.", view.getActivity());
        }
    }
}