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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import ambiesoft.start.R;
import ambiesoft.start.presenter.fragment.GoogleMapFragmentPresenter;
import ambiesoft.start.presenter.fragment.HeatMapFragmentPresenter;
import ambiesoft.start.view.activity.MainActivity;

import static ambiesoft.start.model.utility.ProgressLoadingDialog.showProgressDialog;

/**
 * Created by Bryanyhy on 28/8/2016.
 */
public class HeatMapFragment extends Fragment {

    private HeatMapFragmentPresenter presenter;
    private PlaceAutocompleteFragment autocompleteFragment;
    private AppBarLayout abl;
    public Button confirmButton;
    private Spinner daySpinner;
    private Spinner timeSpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_heatmap, container, false);
        setHasOptionsMenu(true);
        confirmButton = (Button) view.findViewById(R.id.confirmButton);
        daySpinner = (Spinner) view.findViewById(R.id.daySpinner);
        timeSpinner = (Spinner) view.findViewById(R.id.timeSpinner);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // collapse the app bar layout
        abl = ((MainActivity) getActivity()).getAppBarLayout();
        abl.setExpanded(false, false);
        abl.setActivated(false);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                presenter.confirmLocation();
            }
        });

        if (presenter == null) {
            presenter = new HeatMapFragmentPresenter(this);
        }

        autocompleteFragment = (PlaceAutocompleteFragment)
                getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                presenter.setMarkerOnSearchedPlace(place.getLatLng(), place.getAddress().toString());
            }

            @Override
            public void onError(Status status) {
                Log.i("System.out", "An error occurred: " + status);
            }
        });
        setDaySpinner();
        setTimeSpinner();
    }

    public void setDaySpinner() {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.day_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        daySpinner.setAdapter(adapter);
        daySpinner.setSelection(0,false);

        daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                presenter.setDaySelected(position);
            }
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    public void setTimeSpinner() {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.time_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        timeSpinner.setAdapter(adapter);
        timeSpinner.setSelection(13,false);
        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                presenter.setTimeSelected(position);
            }
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    // setup the menu items on the top action bar
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.heat_map_fragment_menu, menu);
    }

    // action after menu item on top action bar is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        presenter.actionBarItemSelection(item);
        return super.onOptionsItemSelected(item);
    }

}
