package ambiesoft.start.view.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import ambiesoft.start.R;
import ambiesoft.start.presenter.fragment.GoogleMapFragmentPresenter;
import ambiesoft.start.presenter.fragment.HeatMapFragmentPresenter;
import ambiesoft.start.view.activity.MainActivity;

/**
 * Created by Bryanyhy on 28/8/2016.
 */
public class HeatMapFragment extends Fragment {

    private HeatMapFragmentPresenter presenter;
    private PlaceAutocompleteFragment autocompleteFragment;
    private AppBarLayout abl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_heatmap, container, false);
        // display menu of the top action bar
//        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // collapse the app bar layout
        abl = ((MainActivity) getActivity()).getAppBarLayout();
        abl.setExpanded(false, false);
        abl.setActivated(false);
        if (presenter == null) {
            presenter = new HeatMapFragmentPresenter(this);
        }

        autocompleteFragment = (PlaceAutocompleteFragment)
                getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i("System.out", "Place: " + place.getName());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("System.out", "An error occurred: " + status);
            }
        });
    }



}
