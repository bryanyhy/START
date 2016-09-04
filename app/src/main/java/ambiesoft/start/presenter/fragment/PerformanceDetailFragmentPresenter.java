package ambiesoft.start.presenter.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;

import ambiesoft.start.R;
import ambiesoft.start.model.dataclass.Performance;
import ambiesoft.start.view.activity.MainActivity;
import ambiesoft.start.view.fragment.GoogleMapFragment;
import ambiesoft.start.view.fragment.HomeFragment;
import ambiesoft.start.view.fragment.PerformanceDetailFragment;

import static ambiesoft.start.model.utility.AlertBox.showAlertBox;
import static ambiesoft.start.model.utility.BundleItemChecker.getFilterCategoryFromBundle;
import static ambiesoft.start.model.utility.BundleItemChecker.getFilterDateFromBundle;
import static ambiesoft.start.model.utility.BundleItemChecker.getFilterKeywordFromBundle;
import static ambiesoft.start.model.utility.BundleItemChecker.getFilterTimeFromBundle;
import static ambiesoft.start.model.utility.BundleItemChecker.getPreviousFragmentIDFromBundle;
import static ambiesoft.start.model.utility.BundleItemChecker.getSelectedPerformanceFromBundle;

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
        ((MainActivity) view.getActivity()).getNavigationTabBar().hide();
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
            view.locText.setText(selectedPerformance.getAddress());
        }
    }

    // Method called when user clicked the "back" button
    public void backToPreviousFragment() {
        Log.i("System.out","ID:" + previousFragmentID);
        // check the previous fragment ID
        if (previousFragmentID == 0) {
            // back to HomeFragment if it is the previous one
//            view.getFragmentManager().popBackStack();
            Fragment homeFragment = new HomeFragment();
            // create the bundle with previous filter settings
            Bundle bundle = new Bundle();
            bundle.putString("dateFromFilter", filterDate);
            bundle.putString("keywordFromFilter", filterKeyword);
            bundle.putString("categoryFromFilter", filterCategory);
            bundle.putString("timeFromFilter", filterTime);
            homeFragment.setArguments(bundle);
            view.getFragmentManager().beginTransaction().replace(R.id.content_frame, homeFragment).remove(view).commit();
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
            view.getFragmentManager().beginTransaction().replace(R.id.content_frame_map, googleMapFragment).remove(view).commit();
        } else {
            // show alertbox if the previous fragment ID is not valid
            showAlertBox("Error", "Unexpected error occurs. Please restart the app.", view.getActivity());
        }
    }
}
