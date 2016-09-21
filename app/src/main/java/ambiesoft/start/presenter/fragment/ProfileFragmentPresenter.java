package ambiesoft.start.presenter.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import ambiesoft.start.R;
import ambiesoft.start.model.dataclass.Performance;
import ambiesoft.start.model.dataclass.User;
import ambiesoft.start.model.utility.RecyclerViewAdapter;
import ambiesoft.start.view.activity.MainActivity;
import ambiesoft.start.view.fragment.BuskerListFragment;
import ambiesoft.start.view.fragment.EditProfileFragment;
import ambiesoft.start.view.fragment.MyBuskingFragment;
import ambiesoft.start.view.fragment.PerformanceDetailFragment;
import ambiesoft.start.view.fragment.ProfileFragment;

import static ambiesoft.start.model.utility.AlertBox.showAlertBox;
import static ambiesoft.start.model.utility.BundleItemChecker.getFilterCategoryFromBundle;
import static ambiesoft.start.model.utility.BundleItemChecker.getFilterDateFromBundle;
import static ambiesoft.start.model.utility.BundleItemChecker.getFilterKeywordFromBundle;
import static ambiesoft.start.model.utility.BundleItemChecker.getFilterTimeFromBundle;
import static ambiesoft.start.model.utility.BundleItemChecker.getPreviousFragmentIDFromBundle;
import static ambiesoft.start.model.utility.BundleItemChecker.getSelectedBuskerFromBundle;
import static ambiesoft.start.model.utility.BundleItemChecker.getSelectedPerformanceFromBundle;
import static ambiesoft.start.model.utility.DateFormatter.sortPerformanceListByDateTimeAndDuration;
import static ambiesoft.start.model.utility.FirebaseUtility.getPerformanceListFromFirebase;
import static ambiesoft.start.model.utility.FirebaseUtility.getUserListFromFirebase;
import static ambiesoft.start.model.utility.FirebaseUtility.setUserPortraitUri;
import static ambiesoft.start.model.utility.ProgressLoadingDialog.dismissProgressDialog;

/**
 * Created by Zelta on 7/09/16.
 */
public class ProfileFragmentPresenter {

    private final static int BUSKER_LIST_TRANSACTION = 3;
    private final static int TAB_BAR_TRANSACTION = 2;
    private final static String DB_URL_PERFORMANCE = "https://start-c9adf.firebaseio.com/performance";
    private final static String DB_URL_USER = "https://start-c9adf.firebaseio.com/user";

    private ProfileFragment view;
    private Firebase firebase;
    private ArrayList<User> users;
    private String email;

    private User selectedBusker;
    private ArrayList<Performance> performances;
    private int previousFragmentID = 2;

    public ProfileFragmentPresenter(ProfileFragment view){
        this.view = view;
        getBundleFromPreviousFragment();
        setRecyclerViewEditableAdapter(new ArrayList<Performance>());
        checkPreviousFragmentForHidingContent();
//        setFireBaseListenerOnUser();
//        setUserPortraitUri(this.email, view.getContext(), view.portrait);
    }

    public void getBundleFromPreviousFragment() {
        Bundle bundle = view.getArguments();
        if (bundle != null) {
            // if bundle exists, get the filter values
            selectedBusker = getSelectedBuskerFromBundle(bundle);
            previousFragmentID = getPreviousFragmentIDFromBundle(bundle);
            if (previousFragmentID == BUSKER_LIST_TRANSACTION) {
                // set the textView from data in bundle accordingly
                view.buskerName.setText(selectedBusker.getUsername());
                view.buskerHashtag.setText(selectedBusker.getHashtag());
                view.buskerCategory.setText(selectedBusker.getCategory());
                view.buskerDesc.setText(selectedBusker.getDesc());
                setUserPortraitUri(selectedBusker.getEmail(), view.getContext(), view.portrait);
            }
        }
    }

    public void checkPreviousFragmentForHidingContent() {
        if (previousFragmentID == BUSKER_LIST_TRANSACTION) {
            // hide the edit button if transaction is from busker list
            view.editProfile.setVisibility(View.INVISIBLE);
            view.buskerPerformance.setVisibility(View.VISIBLE);
            setFireBaseListenerForPerformance();
        } else {
            view.editProfile.setVisibility(View.VISIBLE);
            view.buskerPerformance.setVisibility(View.INVISIBLE);
            setFireBaseListenerOnUser();
        }
    }

    // for setting the recycler view adapter
    public void setRecyclerViewEditableAdapter(ArrayList<Performance> performances) {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this.view.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        view.recyclerView.setLayoutManager(layoutManager);
        // adapter for recycler view, to get all performance result and show them in cardview
        view.adapter = new RecyclerViewAdapter(performances, view.getActivity(), previousFragmentID, selectedBusker);
        view.recyclerView.setAdapter(view.adapter);
        view.recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    public void editProfile() {
        Fragment editProfileFragment = new EditProfileFragment();
        // create bundle, with data added into it
        Bundle bundle = new Bundle();
        bundle.putParcelable("buskerDetailFromPreviousFragment", selectedBusker);
        editProfileFragment.setArguments(bundle);
        // transact to performanceDetailFragment
        view.getFragmentManager().beginTransaction().replace(R.id.content_frame, editProfileFragment, "EditProfileFragment").commit();
    }

    public void backToPreviousFragment() {
        if (previousFragmentID == BUSKER_LIST_TRANSACTION) {
            view.getFragmentManager().beginTransaction().replace(R.id.content_frame, new BuskerListFragment()).remove(view).commit();
        } else {
            view.getFragmentManager().beginTransaction().replace(R.id.content_frame, new MyBuskingFragment()).remove(view).commit();
        }
    }

    // set the Firebase data listener, and update the data retrieved in the application
    public void setFireBaseListenerForPerformance() {
        //establish connection to firebase
        firebase = new Firebase(DB_URL_PERFORMANCE);
        // get data that match the specific date from Firebase
        Query queryRef = firebase.orderByChild("email").equalTo(selectedBusker.getEmail());
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
                        setRecyclerViewEditableAdapter(performances);
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


    // set the FirebaseUtility data listener, and update the data retrieved in the application
    public void setFireBaseListenerOnUser() {
        //establish connection to firebase
        firebase = new Firebase(DB_URL_USER);
        // get data that match the specific email from Firebase
        Query queryRef = firebase.orderByChild("email").equalTo(((MainActivity) view.getActivity()).getUserEmail());
        // value event listener that is triggered everytime data in Firebase's Performance root is updated
        // Retrieve all performance's attributes from each post on Firebase, when any data is updated in the FirebaseUtility
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {
                Log.i("System.out", "Firebase has update");
                // initialize performance ArrayList
                users = new ArrayList<>();
                // get all performance detail and save them into Performance ArrayList as Performance Object
                users = getUserListFromFirebase(ds);
                // check if any matching result is retrieved
                if (users.size() != 0) {
                    selectedBusker = users.get(0);
                    view.buskerName.setText(selectedBusker.getUsername());
                    view.buskerHashtag.setText(selectedBusker.getHashtag());
                    view.buskerCategory.setText(selectedBusker.getCategory());
                    view.buskerDesc.setText(selectedBusker.getDesc());
                    setUserPortraitUri(selectedBusker.getEmail(), view.getContext(), view.portrait);
                } else {
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
