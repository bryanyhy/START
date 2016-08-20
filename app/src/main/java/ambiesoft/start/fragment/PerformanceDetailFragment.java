package ambiesoft.start.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ambiesoft.start.R;
import ambiesoft.start.dataclass.Performance;

import static ambiesoft.start.utility.AlertBox.showAlertBox;
import static ambiesoft.start.utility.BundleItemChecker.getFilterCategoryFromBundle;
import static ambiesoft.start.utility.BundleItemChecker.getFilterDateFromBundle;
import static ambiesoft.start.utility.BundleItemChecker.getFilterKeywordFromBundle;
import static ambiesoft.start.utility.BundleItemChecker.getFilterTimeFromBundle;
import static ambiesoft.start.utility.BundleItemChecker.getPreviousFragmentIDFromBundle;
import static ambiesoft.start.utility.BundleItemChecker.getSelectedPerformanceFromBundle;

/**
 *  Class for showing the performance detail which user selected
 */
public class PerformanceDetailFragment extends Fragment {

    private TextView nameText;
    private TextView categoryText;
    private TextView dateText;
    private TextView timeText;
    private TextView descText;
    private Button backButton;

    private Performance selectedPerformance;
    private int previousFragmentID;
    private String filterDate;
    private String filterKeyword;
    private String filterCategory;
    private String filterTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_performance_detail, container, false);
        nameText = (TextView) view.findViewById(R.id.nameText);
        categoryText = (TextView) view.findViewById(R.id.categoryText);
        dateText = (TextView) view.findViewById(R.id.dateText);
        timeText = (TextView) view.findViewById(R.id.timeText);
        descText = (TextView) view.findViewById(R.id.descText);
        backButton = (Button) view.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                backToPreviousFragment();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // get bundle from previous fragment
        Bundle bundle = getArguments();
        if (bundle != null) {
            // if bundle exists, get the filter values
            filterDate = getFilterDateFromBundle(bundle);
            filterKeyword = getFilterKeywordFromBundle(bundle);
            filterCategory = getFilterCategoryFromBundle(bundle);
            filterTime = getFilterTimeFromBundle(bundle);
            selectedPerformance = getSelectedPerformanceFromBundle(bundle);
            previousFragmentID = getPreviousFragmentIDFromBundle(bundle);
            // set the textView from data in bundle accordingly
            nameText.setText(selectedPerformance.getName());
            categoryText.setText(selectedPerformance.getCategory());
            dateText.setText(selectedPerformance.getDate());
            timeText.setText(selectedPerformance.getsTime() + " - " + selectedPerformance.geteTime());
            descText.setText(selectedPerformance.getDesc());
        }
    }

    // Method called when user clicked the "back" button
    public void backToPreviousFragment() {
        Log.i("System.out","ID:" + previousFragmentID);
        // check the previous fragment ID
        if (previousFragmentID == 0) {
            // back to HomeFragment if it is the previous one
            getFragmentManager().popBackStack();
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
            getFragmentManager().beginTransaction().replace(R.id.content_frame, googleMapFragment).commit();
        } else {
            // show alertbox if the previous fragment ID is not valid
            showAlertBox("Error", "Unexpected error occurs. Please restart the app.", getActivity());
        }
    }
}
