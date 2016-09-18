package ambiesoft.start.presenter.fragment;

import android.app.Activity;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import ambiesoft.start.model.dataclass.User;
import ambiesoft.start.model.utility.RecyclerViewBuskerAdapter;
import ambiesoft.start.model.utility.RecyclerViewEditableAdapter;
import ambiesoft.start.view.activity.MainActivity;
import ambiesoft.start.view.fragment.BuskerListFragment;

import static ambiesoft.start.model.utility.AlertBox.showAlertBox;
import static ambiesoft.start.model.utility.DateFormatter.sortPerformanceListByDateTimeAndDuration;
import static ambiesoft.start.model.utility.FirebaseUtility.getPerformanceListFromFirebase;
import static ambiesoft.start.model.utility.FirebaseUtility.getUserListFromFirebase;
import static ambiesoft.start.model.utility.FirebaseUtility.setupFirebase;
import static ambiesoft.start.model.utility.NetworkAvailability.isNetworkAvailable;
import static ambiesoft.start.model.utility.ProgressLoadingDialog.dismissProgressDialog;
import static ambiesoft.start.model.utility.ProgressLoadingDialog.showProgressDialog;

/**
 * Created by Bryanyhy on 18/9/2016.
 */
public class BuskerListFragmentPresenter {

    private static final int BUSKING_LIST_FRAGMENT_ID = 3;
    private final static String DB_URL = "https://start-c9adf.firebaseio.com/user";

    private BuskerListFragment view;
    private ArrayList<User> buskers;
    private Firebase firebase;

    public BuskerListFragmentPresenter(BuskerListFragment view) {
        this.view = view;
        // initialize busker ArrayList
        buskers = new ArrayList<>();
        ((MainActivity) view.getActivity()).getNavigationTabBar().show();
        checkNetworkAvailability();
    }

    public void checkNetworkAvailability() {
        // check if there is network connection
        if (isNetworkAvailable(view.getContext()) == false) {
            // if no network is detected, dismiss the progress dialog and show alertbox to user
            dismissProgressDialog();
            showAlertBox("Alert", "There is no internet connection. All functions are disabled.", view.getActivity());
        } else {
            // if network is available
            // show the loading progress dialog, when retrieving data from Firebase
            showProgressDialog(view.getContext());
            // setup the firebase
            setupFirebase(view.getContext());
            // set the FirebaseUtility data listener, and get the performance data
            setFireBaseListener();
        }
    }

    // set the Firebase data listener, and update the data retrieved in the application
    public void setFireBaseListener() {
        //establish connection to firebase
        firebase = new Firebase(DB_URL);
        // value event listener that is triggered everytime data in Firebase's Performance root is updated
        // Retrieve all performance's attributes from each post on Firebase, when any data is updated in the Firebase
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {
                Log.i("System.out","Firebase has update");
                // initialize performance ArrayList
                buskers = new ArrayList<>();
                // get all performance detail and save them into Performance ArrayList as Performance Object
                buskers = getUserListFromFirebase(ds);
                // check if any matching result is retrieved
                if (buskers.size() != 0) {

                } else {
                    // if no matching result is found from Firebase
                    showAlertBox("Sorry", "There is no busker yet.", (Activity) view.getContext());
                }
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        // update the recyclerView
                        setRecyclerViewEditableAdapter();
                        // dismiss the progress dialog after all the updates
                        dismissProgressDialog();
                    }
                }, 100);
            }

            // Handle FirebaseUtility error
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast toast = Toast.makeText(view.getContext(), firebaseError.toString(), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0 ,0);
                toast.show();
                dismissProgressDialog();
            }
        });
    }

    // for setting the recycler view adapter
    public void setRecyclerViewEditableAdapter() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this.view.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        view.recyclerView.setLayoutManager(layoutManager);
        // adapter for recycler view, to get all performance result and show them in cardview
        view.adapter = new RecyclerViewBuskerAdapter(buskers, view.getActivity(), BUSKING_LIST_FRAGMENT_ID);
        view.recyclerView.setAdapter(view.adapter);
        view.recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

}
