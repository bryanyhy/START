package ambiesoft.start.presenter.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;

import ambiesoft.start.R;
import ambiesoft.start.model.dataclass.Performance;
import ambiesoft.start.model.utility.RecyclerViewAdapter;
import ambiesoft.start.model.utility.RecyclerViewEditableAdapter;
import ambiesoft.start.view.activity.MainActivity;
import ambiesoft.start.view.fragment.CreatePerformanceFragment;
import ambiesoft.start.view.fragment.FilterResultFragment;
import ambiesoft.start.view.fragment.HeatMapFragment;
import ambiesoft.start.view.fragment.MyBuskingFragment;
import ambiesoft.start.view.fragment.ProfileFragment;

import static ambiesoft.start.model.utility.AlertBox.showAlertBox;
import static ambiesoft.start.model.utility.AlertBox.showAlertBoxWithUnderline;
import static ambiesoft.start.model.utility.DateFormatter.sortPerformanceListByDateTimeAndDuration;
import static ambiesoft.start.model.utility.FilterResult.advancedFilteringOnPerformanceList;
import static ambiesoft.start.model.utility.FirebaseUtility.getPerformanceListFromFirebase;
import static ambiesoft.start.model.utility.FirebaseUtility.setupFirebase;
import static ambiesoft.start.model.utility.NetworkAvailability.isNetworkAvailable;
import static ambiesoft.start.model.utility.ProgressLoadingDialog.dismissProgressDialog;
import static ambiesoft.start.model.utility.ProgressLoadingDialog.showProgressDialog;

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
        // get data that match the specific date from Firebase
        Query queryRef = firebase.orderByChild("email").equalTo(((MainActivity) view.getActivity()).getUserEmail());
        // value event listener that is triggered everytime data in Firebase's Performance root is updated
        // Retrieve all performance's attributes from each post on Firebase, when any data is updated in the Firebase
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
                    sortPerformanceListByDateTimeAndDuration(performances);
                    Collections.reverse(performances);
                } else {
                    // if no matching result is found from Firebase
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

            // Handle Firebase error
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
        view.adapter = new RecyclerViewEditableAdapter(performances, view.getActivity(), MY_BUSKING_FRAGMENT_ID);
        view.recyclerView.setAdapter(view.adapter);
        view.recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    public void transactToHeatMap() {
        Fragment heatMapFragment = new HeatMapFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("previousFragmentID", MY_BUSKING_FRAGMENT_ID);
        heatMapFragment.setArguments(bundle);
        view.getFragmentManager().beginTransaction().replace(R.id.content_frame_map, heatMapFragment).remove(view).commit();
    }

    public void transactToProfile() {
        Fragment myProfile = new ProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("previousFragmentID", MY_BUSKING_FRAGMENT_ID);
        myProfile.setArguments(bundle);
        view.getFragmentManager().beginTransaction().replace(R.id.content_frame, myProfile).commit();
    }

    public void imageButtonSelection(ImageButton imageButton){
        int id = imageButton.getId();

        if (id == R.id.createNewButton) {
            Fragment createPerformanceFragment = new CreatePerformanceFragment();
            // create bundle, add all performance information into it
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("performanceListFromPreviousFragment", performances);
            createPerformanceFragment.setArguments(bundle);
            // pass bundle to the new createPerformanceFragment
            view.getFragmentManager().beginTransaction().replace(R.id.content_frame, createPerformanceFragment).commit();
        }
    }

    public void actionBarItemSelection(MenuItem item) {
        int id = item.getItemId();
        // if search button is clicked
        if (id == R.id.action_info) {
            String title1 = "<b><u>Create Performance</u></b>";
            String title2 = "<b><u>Edit/Delete Performance</u></b>";
            showAlertBoxWithUnderline("Info", Html.fromHtml(title1 + "<br>Click on the New Busking button to create a performance. <br><br>" +
                    title2 + "<br>Swipe on the performance list item to edit or delete."), view.getActivity());
        }
    }
}
