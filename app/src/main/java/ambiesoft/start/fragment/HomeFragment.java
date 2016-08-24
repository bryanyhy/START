package ambiesoft.start.fragment;


import android.app.FragmentManager;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
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
import static ambiesoft.start.utility.NetworkAvailability.isNetworkAvailable;
import static ambiesoft.start.utility.ProgressLoadingDialog.dismissProgressDialog;
import static ambiesoft.start.utility.ProgressLoadingDialog.showProgressDialog;

/**
 * Class for the Home Fragment, which shows the result in an list of cardview
 */
public class HomeFragment extends Fragment {

    private HomeFragmentPresenter presenter;

    // ID for this fragment, for fragment transact identification
    private static final int HOME_FRAGMENT_ID = 0;
    // Firebase link for the performance root
    private final static String DB_URL = "https://start-c9adf.firebaseio.com/performance";

    public RecyclerView recyclerView;
    public RecyclerViewAdapter adapter;

    private ArrayList<Performance> performances;
    private static ArrayList<Performance> filteredPerformances;

    private Firebase firebase;

    private static String selectedDate;
    private String filterKeyword;
    private String filterCategory;
    private String filterTime;

    private FloatingActionButton fab;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        // display menu of the top action bar
        setHasOptionsMenu(true);
//        // initialize performance ArrayList
//        performances = new ArrayList<>();
//        filteredPerformances = new ArrayList<>();
        // recycleView to hold all the cardview
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        setRecyclerViewAdapter();
        // setting up the floating action button, to access from home to map fragment
        fab = (FloatingActionButton) view.findViewById(R.id.fab);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        if (presenter == null) {
            Log.i("System.out","Create home presenter");
            presenter = new HomeFragmentPresenter(this, fab);
        }
//        presenter.onTakeView(this);
//        presenter.onTakeFab(fab);
//        presenter.setFloatingActionButton();

//        // show the loading progress dialog, when retrieving data from Firebase
//        showProgressDialog(getContext());
//        // get bundle from previous fragment
//        Bundle bundle = getArguments();
//        if (bundle != null) {
//            // if bundle exists, get the filter values
//            selectedDate = getFilterDateFromBundle(bundle);
//            filterKeyword = getFilterKeywordFromBundle(bundle);
//            filterCategory = getFilterCategoryFromBundle(bundle);
//            filterTime = getFilterTimeFromBundle(bundle);
//        } else {
//            // Always runs when the application start and set the filter date to today by default
//            selectedDate = getTodayDate();
//        }
        presenter.getBundleFromPreviousFragment();
        presenter.checkNetworkAvailability();
//        // check if there is network connection
//        if (isNetworkAvailable(getContext()) == false) {
//            // if no network is detected, dismiss the progress dialog and show alertbox to user
//            dismissProgressDialog();
//            showAlertBox("Alert", "There is no internet connection.", getActivity());
//        } else {
//            // if network is available
//            // setup the firebase
//            setupFirebase(getContext());
//            // set the Firebase data listener, and get the performance data
//            setFireBaseListener();
//        }
    }

    // for setting the recycler view adapter
    public void setRecyclerViewAdapter() {
        // adapter for recycler view, to get all performance result and show them in cardview
        adapter = new RecyclerViewAdapter(new ArrayList<Performance>(), getActivity());
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
        presenter.actionBarItemSelection(item);
        return super.onOptionsItemSelected(item);
    }

//    // set the Firebase data listener, and update the data retrieved in the application
//    public void setFireBaseListener() {
//        //establish connection to firebase
//        firebase = new Firebase(DB_URL);
//        // get data that match the specific date from Firebase
//        Query queryRef = firebase.orderByChild("date").equalTo(selectedDate);
//        // value event listener that is triggered everytime data in Firebase's Performance root is updated
//        // Retrieve all performance's attributes from each post on Firebase, when any data is updated in the Firebase
//        queryRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot ds) {
//                Log.i("System.out","Firebase has update");
//                // initialize performance ArrayList
//                performances = new ArrayList<>();
//                // get all performance detail and save them into Performance ArrayList as Performance Object
//                performances = getPerformanceListFromFirebaseByDate(ds);
//                // check if any matching result is retrieved
//                if (performances.size() != 0) {
//                    // if there is matching result, check if there are any advanced filter option other than date
//                    if (filterKeyword != null || filterCategory != null || filterTime != null) {
//                        // if there is other parameter as the filter requirement
//                        try {
//                            // do the advanced filtering, and get the final result in ArrayList
//                            filteredPerformances = advancedFilteringOnPerformanceList(performances, filterKeyword, filterCategory, filterTime);
//                            // check if there is matching result after advanced filtering
//                            if (filteredPerformances.size() == 0) {
//                                // if no, show alertbox
//                                showAlertBox("Sorry", "There is no matching result on " + selectedDate + ".", getActivity());
//                            }
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                    } else {
//                        // if only date is the filter parameters, the final result is what we retrieved from Firebase
//                        filteredPerformances = performances;
//                    }
//                } else {
//                    // if no matching result is found from Firebase
//                    showAlertBox("Sorry", "There is no matching result on " + selectedDate + ".", getActivity());
//                }
//                // update the recyclerView
//                setRecyclerViewAdapter();
//                // dismiss the progress dialog after all the updates
//                dismissProgressDialog();
//            }
//
//            // Handle Firebase error
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//                Toast toast = Toast.makeText(getContext(), firebaseError.toString(), Toast.LENGTH_SHORT);
//                toast.setGravity(Gravity.CENTER, 0 ,0);
//                toast.show();
//                dismissProgressDialog();
//            }
//        });
//    }
}
