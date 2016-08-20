package ambiesoft.start.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;

import ambiesoft.start.R;
import ambiesoft.start.dataclass.Performance;
import ambiesoft.start.utility.RecyclerViewAdapter;

import static ambiesoft.start.utility.AlertBox.showAlertBox;
import static ambiesoft.start.utility.BundleItemChecker.getFilterCategoryFromBundle;
import static ambiesoft.start.utility.BundleItemChecker.getFilterDateFromBundle;
import static ambiesoft.start.utility.BundleItemChecker.getFilterKeywordFromBundle;
import static ambiesoft.start.utility.BundleItemChecker.getFilterTimeFromBundle;
import static ambiesoft.start.utility.DateFormatter.getTodayDate;
import static ambiesoft.start.utility.FilterResult.advancedFilteringOnPerformanceList;
import static ambiesoft.start.utility.Firebase.getPerformanceListFromFirebaseByDate;
import static ambiesoft.start.utility.Firebase.setupFirebase;
import static ambiesoft.start.utility.ProgressLoadingDialog.dismissProgressDialog;
import static ambiesoft.start.utility.ProgressLoadingDialog.showProgressDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private static final int HOME_FRAGMENT_ID = 0;
    private final static String DB_URL = "https://start-c9adf.firebaseio.com/performance";

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;

    private ArrayList<Performance> performances;
    private static ArrayList<Performance> filteredPerformances;

    private Firebase firebase;

    private static String selectedDate;
    private String filterKeyword;
    private String filterCategory;
    private String filterTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        // display menu of the top action bar
        setHasOptionsMenu(true);

        // initialize performance ArrayList
        performances = new ArrayList<>();
        filteredPerformances = new ArrayList<>();

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        setRecyclerViewAdapter();

        // setting up the floating action button, to access from home to map fragment
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment googleMapFragment = new GoogleMapFragment();
                Bundle bundle = new Bundle();
                bundle.putString("dateFromPreviousFragment", selectedDate);
//                bundle.putParcelableArrayList("performancesFromPreviousFragment", filteredPerformances);
                googleMapFragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.content_frame, googleMapFragment).commit();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showProgressDialog(getContext());
        Bundle bundle = getArguments();
        if (bundle != null) {
            selectedDate = getFilterDateFromBundle(bundle);
            filterKeyword = getFilterKeywordFromBundle(bundle);
            filterCategory = getFilterCategoryFromBundle(bundle);
            filterTime = getFilterTimeFromBundle(bundle);
        } else {
            // Always runs when the application start and set the filter date to today by default
            selectedDate = getTodayDate();
        }
        setupFirebase(getContext());
        setFireBaseListener();
    }

    public void setRecyclerViewAdapter() {
        adapter = new RecyclerViewAdapter(filteredPerformances, getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_search) {
            Fragment filterResultFragment = new FilterResultFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("previousFragmentID", HOME_FRAGMENT_ID);
            bundle.putString("filterDate", selectedDate);
            filterResultFragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.content_frame, filterResultFragment).commit();
        }

        return super.onOptionsItemSelected(item);
    }

    public void setFireBaseListener() {
        //establish connection to firebase
        firebase = new Firebase(DB_URL);
        // get data that match the specific date from Firebase
        Query queryRef = firebase.orderByChild("date").equalTo(selectedDate);
        // value event listener that is triggered everytime data in Firebase's Performance root is updated
        //Retrieve latitude and longitude from each post on Firebase and add marker on map
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {
                Log.i("System.out","Firebase has update");
                // initialize performance ArrayList
                performances = new ArrayList<>();
                // get all performance detail and save them into Performance ArrayList as Performance Object
                performances = getPerformanceListFromFirebaseByDate(ds);
                if (performances.size() != 0) {
                    if (filterKeyword != null || filterCategory != null || filterTime != null) {
                        try {
                            filteredPerformances = advancedFilteringOnPerformanceList(performances, filterKeyword, filterCategory, filterTime);
                            if (filteredPerformances.size() == 0) {
                                showAlertBox("Sorry", "There is no matching result on " + selectedDate + ".", getActivity());
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else {
                        filteredPerformances = performances;
                    }
                } else {
                    showAlertBox("Sorry", "There is no matching result on " + selectedDate + ".", getActivity());
                }
                setRecyclerViewAdapter();
                dismissProgressDialog();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast toast = Toast.makeText(getContext(), firebaseError.toString(), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0 ,0);
                toast.show();
                dismissProgressDialog();
            }
        });
    }
}
