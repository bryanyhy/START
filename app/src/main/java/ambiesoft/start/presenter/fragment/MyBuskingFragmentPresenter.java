package ambiesoft.start.presenter.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;

import ambiesoft.start.R;
import ambiesoft.start.model.dataclass.Performance;
import ambiesoft.start.model.utility.RecyclerViewAdapter;
import ambiesoft.start.model.utility.RecyclerViewEditableAdapter;
import ambiesoft.start.view.activity.MainActivity;
import ambiesoft.start.view.fragment.CreatePerformanceFragment;
import ambiesoft.start.view.fragment.FilterResultFragment;
import ambiesoft.start.view.fragment.MyBuskingFragment;

import static ambiesoft.start.model.utility.AlertBox.showAlertBox;
import static ambiesoft.start.model.utility.FilterResult.advancedFilteringOnPerformanceList;
import static ambiesoft.start.model.utility.FirebaseUtility.getPerformanceListFromFirebase;
import static ambiesoft.start.model.utility.NetworkAvailability.isNetworkAvailable;
import static ambiesoft.start.model.utility.ProgressLoadingDialog.dismissProgressDialog;

/**
 * Created by Bryanyhy on 30/8/2016.
 */
public class MyBuskingFragmentPresenter {

    private static final int MY_BUSKING_FRAGMENT_ID = 2;
    private final static String DB_URL = "https://start-c9adf.firebaseio.com/performance";

    private MyBuskingFragment view;
    private ArrayList<Performance> performances;
    private Firebase firebase;

    public MyBuskingFragmentPresenter(MyBuskingFragment view) {
        this.view = view;
        // initialize performance ArrayList
        performances = new ArrayList<>();
        setFireBaseListener();
    }

    // set the FirebaseUtility data listener, and update the data retrieved in the application
    public void setFireBaseListener() {
        //establish connection to firebase
        firebase = new Firebase(DB_URL);
        // get data that match the specific date from FirebaseUtility
        Query queryRef = firebase.orderByChild("email").equalTo(((MainActivity) view.getActivity()).getUserEmail());
        // value event listener that is triggered everytime data in FirebaseUtility's Performance root is updated
        // Retrieve all performance's attributes from each post on FirebaseUtility, when any data is updated in the FirebaseUtility
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {
                Log.i("System.out","Firebase has update");
                // initialize performance ArrayList
                performances = new ArrayList<>();
                // get all performance detail and save them into Performance ArrayList as Performance Object
                performances = getPerformanceListFromFirebase(ds);
                // check if any matching result is retrieved
                if (performances.size() != 0) {
                } else {
                    // if no matching result is found from FirebaseUtility
                    showAlertBox("Sorry", "There is no performance created yet.", (Activity) view.getContext());
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
        // adapter for recycler view, to get all performance result and show them in cardview
        view.adapter = new RecyclerViewEditableAdapter(performances, view.getActivity(), MY_BUSKING_FRAGMENT_ID);
        view.recyclerView.setAdapter(view.adapter);
        view.recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    public void actionBarItemSelection(MenuItem item) {
        int id = item.getItemId();
        // if search button is clicked
        if (id == R.id.action_create_per) {
            Fragment createPerformanceFragment = new CreatePerformanceFragment();
            // create bundle, add all performance information into it
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("performanceListFromPreviousFragment", performances);
            createPerformanceFragment.setArguments(bundle);
            // pass bundle to the new createPerformanceFragment
            view.getFragmentManager().beginTransaction().replace(R.id.content_frame, createPerformanceFragment).addToBackStack(null).commit();
        } else if (id == R.id.action_info) {
            showAlertBox("Info", "Click on the icon on top right corner to create a performance. \n\nSwipe on the performance list item to edit or delete.", view.getActivity());
        }
    }
}
