package ambiesoft.start.view.fragment;


import android.app.FragmentManager;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.gigamole.navigationtabbar.ntb.NavigationTabBar;

import java.util.ArrayList;

import ambiesoft.start.R;
import ambiesoft.start.model.dataclass.Performance;
import ambiesoft.start.model.utility.RecyclerViewAdapter;
import ambiesoft.start.model.utility.RecyclerViewEditableAdapter;
import ambiesoft.start.presenter.fragment.HomeFragmentPresenter;
import ambiesoft.start.presenter.fragment.MyBuskingFragmentPresenter;
import ambiesoft.start.view.activity.MainActivity;

import static ambiesoft.start.model.utility.AlertBox.showAlertBox;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyBuskingFragment extends Fragment {

    private static final int MY_BUSKING_FRAGMENT_ID = 2;
    private MyBuskingFragmentPresenter presenter;

    public RecyclerView recyclerView;
    public RecyclerViewEditableAdapter adapter;
    public ImageButton createNew, heatMap, myProfile;

    private AppBarLayout abl;
    private NavigationTabBar navigationTabBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_busking, container, false);
        // display menu of the top action bar
        setHasOptionsMenu(true);
        // recycleView to hold all the cardview
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        createNew = (ImageButton) view.findViewById(R.id.createNewButton);
        heatMap = (ImageButton) view.findViewById(R.id.heatMapButton);
        myProfile = (ImageButton) view.findViewById(R.id.myProfileButton);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // extend the app bar layout
        abl = ((MainActivity) getActivity()).getAppBarLayout();
        abl.setExpanded(true);
//        abl.setActivated(true);

        navigationTabBar = ((MainActivity) getActivity()).getNavigationTabBar();
        navigationTabBar.show();
        navigationTabBar.setBehaviorEnabled(true);

        //set backdrop image
        ImageView banner = ((MainActivity) getActivity()).getBackdrop();
        banner.setBackgroundResource(R.drawable.banner_busking);

        setRecyclerViewEditableAdapter();
        // hide the floating action button in main activity
        FloatingActionButton fab = ((MainActivity) getActivity()).getFloatingActionButton();
        fab.hide();
        if (presenter == null) {
            presenter = new MyBuskingFragmentPresenter(this);
        } else {
            presenter.setRecyclerViewEditableAdapter();
        }
        createNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.imageButtonSelection(createNew);

            }
        });
        heatMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showAlertBox("Sorry", "Functions will be added in next version.", MyBuskingFragment.this.getActivity());
                presenter.transactToHeatMap();

            }
        });

        myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.transactToProfile();
            }
        });
    }

    // for setting the recycler view adapter
    public void setRecyclerViewEditableAdapter() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        // adapter for recycler view, to get all performance result and show them in cardview
        adapter = new RecyclerViewEditableAdapter(new ArrayList<Performance>(), getActivity(), MY_BUSKING_FRAGMENT_ID);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    // setup the menu items on the top action bar
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.my_busking_fragment_menu, menu);
    }

    // action after menu item on top action bar is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        presenter.actionBarItemSelection(item);
        return super.onOptionsItemSelected(item);
    }

}
