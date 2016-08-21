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
import ambiesoft.start.dataclass.Artwork;
import ambiesoft.start.dataclass.Performance;

import static ambiesoft.start.utility.AlertBox.showAlertBox;
import static ambiesoft.start.utility.BundleItemChecker.getFilterCategoryFromBundle;
import static ambiesoft.start.utility.BundleItemChecker.getFilterDateFromBundle;
import static ambiesoft.start.utility.BundleItemChecker.getFilterKeywordFromBundle;
import static ambiesoft.start.utility.BundleItemChecker.getFilterTimeFromBundle;
import static ambiesoft.start.utility.BundleItemChecker.getPreviousFragmentIDFromBundle;
import static ambiesoft.start.utility.BundleItemChecker.getSelectedArtworkFromBundle;
import static ambiesoft.start.utility.BundleItemChecker.getSelectedPerformanceFromBundle;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArtworkDetailFragment extends Fragment {


    private TextView nameText;
    private TextView assetText;
    private TextView creatorText;
    private TextView yearText;
    private TextView locText;
    private Button backButton;

    private Artwork selectedArtwork;
    private int previousFragmentID;
    private String filterDate;
    private String filterKeyword;
    private String filterCategory;
    private String filterTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_artwork_detail, container, false);
        nameText = (TextView) view.findViewById(R.id.nameText);
        assetText = (TextView) view.findViewById(R.id.assetText);
        creatorText = (TextView) view.findViewById(R.id.creatorText);
        yearText = (TextView) view.findViewById(R.id.yearText);
        locText = (TextView) view.findViewById(R.id.locText);
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
            selectedArtwork = getSelectedArtworkFromBundle(bundle);
            previousFragmentID = getPreviousFragmentIDFromBundle(bundle);
            // set the textView from data in bundle accordingly
            nameText.setText(selectedArtwork.getName());
            assetText.setText(selectedArtwork.getAssetType());
            creatorText.setText(selectedArtwork.getArtist());
            yearText.setText(selectedArtwork.getArtDate());
            locText.setText(selectedArtwork.getAddress());
        }
    }

    // Method called when user clicked the "back" button
    public void backToPreviousFragment() {
        if (previousFragmentID == 1) {
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
