package ambiesoft.start.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
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
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import ambiesoft.start.R;
import ambiesoft.start.activity.MainActivity;
import ambiesoft.start.dataclass.Performance;
import ambiesoft.start.utility.RecyclerViewAdapter;

import static ambiesoft.start.utility.AlertBox.showAlertBox;
import static ambiesoft.start.utility.FilterResult.advancedFilteringOnPerformanceList;

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

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey("dateFromFilter")) {
                selectedDate = bundle.getString("dateFromFilter");
            } else if (bundle.containsKey("dateFromPreviousFragment")) {
                selectedDate = bundle.getString("dateFromPreviousFragment");
            } else  {
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                selectedDate = df.format(c.getTime());
            }
            if (bundle.containsKey("keywordFromFilter")) {
                filterKeyword = bundle.getString("keywordFromFilter");
            } else {
                filterKeyword = null;
            }
            if (bundle.containsKey("categoryFromFilter")) {
                filterCategory = bundle.getString("categoryFromFilter");
            } else {
                filterCategory = null;
            }
            if (bundle.containsKey("timeFromFilter")) {
                filterTime = bundle.getString("timeFromFilter");
            } else {
                filterTime = null;
            }
//            if (bundle.containsKey("performancesFromPreviousFragment")) {
//                filteredPerformances = bundle.getParcelableArrayList("performancesFromPreviousFragment");
//                setRecyclerViewAdapter();
//                if (filteredPerformances.size() == 0) {
//                    showAlertBox("Sorry", "There is no matching result on " + selectedDate + ".", getActivity());
//                }
//            }
            setFireBaseListenerOnPerformance();
        } else {
            // Always runs when the application start and set the filter date to today by default
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            selectedDate = df.format(c.getTime());
            Log.i("System.out","Today is " + selectedDate);
            setFireBaseListenerOnPerformance();
        }
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
            bundle.putInt("requestFragment", HOME_FRAGMENT_ID);
            bundle.putString("filterDate", selectedDate);
            filterResultFragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.content_frame, filterResultFragment).commit();
        }

        return super.onOptionsItemSelected(item);
    }

    public void setFireBaseListenerOnPerformance() {
        //Get firebase instance
        Firebase.setAndroidContext(getContext());
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
                for (DataSnapshot dataSnapshot : ds.getChildren()) {
                    String name = dataSnapshot.child("name").getValue().toString();
                    String category = dataSnapshot.child("category").getValue().toString();
                    String desc = dataSnapshot.child("desc").getValue().toString();
                    String date = dataSnapshot.child("date").getValue().toString();
                    String sTime = dataSnapshot.child("sTime").getValue().toString();
                    String eTime = dataSnapshot.child("eTime").getValue().toString();
                    Double latitude = Double.parseDouble(dataSnapshot.child("lat").getValue().toString());
                    Double longitude = Double.parseDouble(dataSnapshot.child("lng").getValue().toString());
                    Performance performance = new Performance(name, category, desc, date, sTime, eTime, latitude, longitude);
                    performances.add(performance);
                }
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
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast toast = Toast.makeText(getContext(), firebaseError.toString(), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0 ,0);
                toast.show();
            }
        });
    }
}
