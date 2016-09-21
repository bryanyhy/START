package ambiesoft.start.view.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gigamole.navigationtabbar.ntb.NavigationTabBar;

import java.util.ArrayList;

import ambiesoft.start.R;
import ambiesoft.start.model.dataclass.Performance;
import ambiesoft.start.model.utility.RecyclerViewBuskerAdapter;
import ambiesoft.start.model.utility.RecyclerViewEditableAdapter;
import ambiesoft.start.presenter.fragment.BuskerListFragmentPresenter;
import ambiesoft.start.view.activity.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuskerListFragment extends Fragment {

    private static final int BUSKING_LIST_FRAGMENT_ID = 3;
    private BuskerListFragmentPresenter presenter;

    public RecyclerView recyclerView;
    public RecyclerViewBuskerAdapter adapter;

    private AppBarLayout abl;
    private NavigationTabBar navigationTabBar;

    public BuskerListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_busker_list, container, false);
        // recycleView to hold all the cardview
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
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
        banner.setBackgroundResource(R.drawable.busker_banner);

        setRecyclerViewEditableAdapter();
        // hide the floating action button in main activity
        FloatingActionButton fab = ((MainActivity) getActivity()).getFloatingActionButton();
        fab.hide();
        if (presenter == null) {
            presenter = new BuskerListFragmentPresenter(this);
        } else {
            presenter.setRecyclerViewEditableAdapter();
        }
    }

    // for setting the recycler view adapter
    public void setRecyclerViewEditableAdapter() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        // adapter for recycler view, to get all performance result and show them in cardview
        adapter = new RecyclerViewBuskerAdapter(new ArrayList<Performance>(), getActivity(), BUSKING_LIST_FRAGMENT_ID);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }


}
