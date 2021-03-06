package ambiesoft.start.view.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
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
import android.widget.ImageView;

import com.gigamole.navigationtabbar.ntb.NavigationTabBar;

import java.util.ArrayList;

import ambiesoft.start.R;
import ambiesoft.start.model.dataclass.User;
import ambiesoft.start.view.activity.MainActivity;
import ambiesoft.start.model.dataclass.Performance;
import ambiesoft.start.presenter.fragment.HomeFragmentPresenter;
import ambiesoft.start.model.utility.RecyclerViewAdapter;

import static ambiesoft.start.model.utility.ProgressLoadingDialog.dismissProgressDialog;

/**
 * Class for the Home Fragment, which shows the result in an list of cardview
 */
public class HomeFragment extends Fragment {

    private static final int HOME_FRAGMENT_ID = 0;
    private HomeFragmentPresenter presenter;

    public RecyclerView recyclerView;
    public RecyclerViewAdapter adapter;

    private ImageView searchBar;

    private FloatingActionButton fab;
    private NavigationTabBar ntb;
    private AppBarLayout abl;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        // display menu of the top action bar
        setHasOptionsMenu(true);
        // recycleView to hold all the cardview
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        // setting up the floating action button, to access from home to map fragment
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        searchBar = (ImageView) view.findViewById(R.id.searchbar);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // extend the app bar layout
        abl = ((MainActivity) getActivity()).getAppBarLayout();
        abl.setExpanded(true, true);
        abl.setActivated(true);

        ntb = ((MainActivity) getActivity()).getNavigationTabBar();
        ntb.show();
        
        //set backdrop image
        ImageView banner = ((MainActivity) getActivity()).getBackdrop();
        banner.setBackgroundResource(R.drawable.home_banner);

        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.filterBarSelection();
            }
        });

        setRecyclerViewAdapter();
        // show the floating action button in main activity
        fab = ((MainActivity) getActivity()).getFloatingActionButton();
        fab.show();
        fab.setImageResource(R.drawable.ic_floating_map);
        if (presenter == null) {
            Log.i("System.out","Create home presenter");
            presenter = new HomeFragmentPresenter(this, fab);
        }
        presenter.getBundleFromPreviousFragment();
        presenter.checkNetworkAvailability();
    }

    // for setting the recycler view adapter
    public void setRecyclerViewAdapter() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        // adapter for recycler view, to get all performance result and show them in cardview
        adapter = new RecyclerViewAdapter(new ArrayList<Performance>(), getActivity(), HOME_FRAGMENT_ID, new User());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        Handler handler = new Handler();
//        final Runnable r = new Runnable() {
//            public void run() {
//                Log.i("System.out","Adapter 1 updated.");
//                adapter.notifyDataSetChanged();
//            }
//        };
//        handler.post(r);
    }

    // setup the menu items on the top action bar
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.home_fragment_menu, menu);
    }

    // action after menu item on top action bar is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        presenter.actionBarItemSelection(item);
        return super.onOptionsItemSelected(item);
    }

}
