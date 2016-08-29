package ambiesoft.start.view.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import ambiesoft.start.R;
import ambiesoft.start.view.activity.MainActivity;
import ambiesoft.start.presenter.fragment.GoogleMapFragmentPresenter;

/**
 * Class for showing the performance or artworks on Google Map
 */
public class GoogleMapFragment extends Fragment {

    private GoogleMapFragmentPresenter presenter;

    private FloatingActionButton fab;
    private AppBarLayout abl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);
        // display menu of the top action bar
        setHasOptionsMenu(true);
        // setting up the floating action button, to access from map to home fragment
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // show the floating action button in main activity
        fab = ((MainActivity) getActivity()).getFloatingActionButton();
        fab.show();
        fab.setImageResource(R.drawable.ic_menu_1);
        // collapse the app bar layout
        abl = ((MainActivity) getActivity()).getAppBarLayout();
                abl.setExpanded(false);

        if (presenter == null) {
            presenter = new GoogleMapFragmentPresenter(this);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FrameLayout fl = (FrameLayout) getActivity().findViewById(R.id.content_frame_map);
                fl.removeAllViews();
                presenter.transactToHomeFragment();
            }
        });
    }

    // setup the menu items on the top action bar
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.map_fragment_menu, menu);
        MenuItem item = menu.findItem(R.id.action_settings);
        // TODO: icon
        item.setIcon(R.drawable.ic_filter_landmark);
        // check if the show artwork setting is turn on or off
        if (presenter.getShowArtworkOption() == true) {
            // if show artwork is on
            item.setTitle("Hide artworks");
        } else {
            // if show artwork is off
            item.setTitle("Show artworks");
        }
    }

    // action after menu item on top action bar is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // if the setting icon is clicked
        if (id == R.id.action_settings) {
            if (presenter.checkIfNetworkIsAvailable()) {
                presenter.redrawMarkersWhenActionBarItemIsSelected();
                if (presenter.getShowArtworkOption() == true) {
                    // if show artwork is on
                    item.setTitle("Hide artworks");
                } else {
                    // if show artwork is off
                    item.setTitle("Show artworks");
                }
            } else {
            }
        }
        // if search button is clicked
        if (id == R.id.action_search) {
            if (presenter.checkIfNetworkIsAvailable()) {
                FrameLayout fl = (FrameLayout) getActivity().findViewById(R.id.content_frame_map);
                fl.removeAllViews();
                presenter.transactToFilterFragment();
            } else {
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
