package ambiesoft.start.presenter.fragment;

import android.app.Fragment;
import android.os.Bundle;

import ambiesoft.start.R;
import ambiesoft.start.model.dataclass.Artwork;
import ambiesoft.start.view.fragment.ArtworkDetailFragment;
import ambiesoft.start.view.fragment.GoogleMapFragment;

import static ambiesoft.start.model.utility.AlertBox.showAlertBox;
import static ambiesoft.start.model.utility.BundleItemChecker.getFilterCategoryFromBundle;
import static ambiesoft.start.model.utility.BundleItemChecker.getFilterDateFromBundle;
import static ambiesoft.start.model.utility.BundleItemChecker.getFilterKeywordFromBundle;
import static ambiesoft.start.model.utility.BundleItemChecker.getFilterTimeFromBundle;
import static ambiesoft.start.model.utility.BundleItemChecker.getPreviousFragmentIDFromBundle;
import static ambiesoft.start.model.utility.BundleItemChecker.getSelectedArtworkFromBundle;

/**
 * Created by Bryanyhy on 23/8/2016.
 */
public class ArtworkDetailFragmentPresenter {
    private ArtworkDetailFragment view;

    private Artwork selectedArtwork;
    private int previousFragmentID;
    private String filterDate;
    private String filterKeyword;
    private String filterCategory;
    private String filterTime;

    public ArtworkDetailFragmentPresenter(ArtworkDetailFragment view) {
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
            selectedArtwork = getSelectedArtworkFromBundle(bundle);
            previousFragmentID = getPreviousFragmentIDFromBundle(bundle);
        }
    }

    public void setTextView() {
        if (selectedArtwork != null) {
            // set the textView from data in bundle accordingly
            view.nameText.setText(selectedArtwork.getName());
            view.assetText.setText(selectedArtwork.getAssetType());
            view.creatorText.setText(selectedArtwork.getArtist());
            view.yearText.setText(selectedArtwork.getArtDate());
            view.locText.setText(selectedArtwork.getAddress());
        }
    }

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
            view.getFragmentManager().beginTransaction().replace(R.id.content_frame_map, googleMapFragment).remove(view).commit();
        } else {
            // show alertbox if the previous fragment ID is not valid
            showAlertBox("Error", "Unexpected error occurs. Please restart the app.", view.getActivity());
        }
    }

}
