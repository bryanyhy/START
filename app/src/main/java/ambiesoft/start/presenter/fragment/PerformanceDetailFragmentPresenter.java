package ambiesoft.start.presenter.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;

import ambiesoft.start.R;
import ambiesoft.start.model.dataclass.Performance;
import ambiesoft.start.model.dataclass.User;
import ambiesoft.start.view.activity.MainActivity;
import ambiesoft.start.view.fragment.GoogleMapFragment;
import ambiesoft.start.view.fragment.HomeFragment;
import ambiesoft.start.view.fragment.PerformanceDetailFragment;
import ambiesoft.start.view.fragment.PostTweetFragment;
import ambiesoft.start.view.fragment.ProfileFragment;

import static ambiesoft.start.model.utility.AlertBox.showAlertBox;
import static ambiesoft.start.model.utility.BundleItemChecker.getFilterCategoryFromBundle;
import static ambiesoft.start.model.utility.BundleItemChecker.getFilterDateFromBundle;
import static ambiesoft.start.model.utility.BundleItemChecker.getFilterKeywordFromBundle;
import static ambiesoft.start.model.utility.BundleItemChecker.getFilterTimeFromBundle;
import static ambiesoft.start.model.utility.BundleItemChecker.getPreviousFragmentIDFromBundle;
import static ambiesoft.start.model.utility.BundleItemChecker.getSelectedBuskerFromBundle;
import static ambiesoft.start.model.utility.BundleItemChecker.getSelectedPerformanceFromBundle;
import static ambiesoft.start.model.utility.FilterResult.advancedFilteringOnPerformanceList;
import static ambiesoft.start.model.utility.FirebaseUtility.getPerformanceListFromFirebase;
import static ambiesoft.start.model.utility.FirebaseUtility.getUserListFromFirebase;
import static ambiesoft.start.model.utility.FirebaseUtility.setUserPortraitUri;
import static ambiesoft.start.model.utility.ProgressLoadingDialog.dismissProgressDialog;

/**
 * Created by Bryanyhy on 23/8/2016.
 */
public class PerformanceDetailFragmentPresenter {

    private final static String DB_URL = "https://start-c9adf.firebaseio.com/user";
    private final static int PER_DETAIL = 9;

    private PerformanceDetailFragment view;
    private Firebase firebase;
    private ArrayList<User> users;

    private Performance selectedPerformance;
    private User selectedBusker;
    private int previousFragmentID;
    private String filterDate;
    private String filterKeyword;
    private String filterCategory;
    private String filterTime;

    public PerformanceDetailFragmentPresenter(PerformanceDetailFragment view) {
        this.view = view;
        ((MainActivity) view.getActivity()).getNavigationTabBar().hide();
    }

    public void getBundleFromPreviousFragment() {
        Bundle bundle = view.getArguments();
        if (bundle != null) {
            // if bundle exists, get the filter values
            filterDate = getFilterDateFromBundle(bundle);
            filterKeyword = getFilterKeywordFromBundle(bundle);
            filterCategory = getFilterCategoryFromBundle(bundle);
            filterTime = getFilterTimeFromBundle(bundle);
            selectedPerformance = getSelectedPerformanceFromBundle(bundle);
            selectedBusker = getSelectedBuskerFromBundle(bundle);
            previousFragmentID = getPreviousFragmentIDFromBundle(bundle);
            // set the textView from data in bundle accordingly
            view.nameText.setText(selectedPerformance.getName());
            view.categoryText.setText(selectedPerformance.getCategory());
            view.dateText.setText(selectedPerformance.getDate());
            view.timeText.setText(selectedPerformance.getsTime() + " - " + selectedPerformance.geteTime());
            view.descText.setText(selectedPerformance.getDesc());
            view.locText.setText(selectedPerformance.getAddress());
            setUserPortraitUri(selectedPerformance.getEmail(), view.getContext(), view.portrait);
            setFireBaseListenerOnUser();
        }
    }

    public void postTweet() {
        Fragment postTweetFragment = new PostTweetFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("buskerDetailFromPreviousFragment", selectedBusker);
        bundle.putParcelable("performancesDetailFromPreviousFragment", selectedPerformance);
        bundle.putInt("previousFragmentID", PER_DETAIL);
        postTweetFragment.setArguments(bundle);
        view.getFragmentManager().beginTransaction().replace(R.id.content_frame, postTweetFragment).addToBackStack(null).commit();
    }


    // Method called when user clicked the "back" button
    public void backToPreviousFragment() {
        Log.i("System.out","ID:" + previousFragmentID);
        // check the previous fragment ID
        if (previousFragmentID == 0) {
            // back to HomeFragment if it is the previous one
//            view.getFragmentManager().popBackStack();
            Fragment homeFragment = new HomeFragment();
            // create the bundle with previous filter settings
            Bundle bundle = new Bundle();
            bundle.putString("dateFromFilter", filterDate);
            bundle.putString("keywordFromFilter", filterKeyword);
            bundle.putString("categoryFromFilter", filterCategory);
            bundle.putString("timeFromFilter", filterTime);
            homeFragment.setArguments(bundle);
            view.getFragmentManager().beginTransaction().replace(R.id.content_frame, homeFragment).remove(view).commit();
        } else if (previousFragmentID == 1) {
            // if it is GoogleMapFragment, pop back will call errors
            // so we have to restart the googleMapFragment
            Fragment googleMapFragment = new GoogleMapFragment();
            // create the bundle with previous filter settings
            Bundle bundle = new Bundle();
            bundle.putString("dateFromFilter", filterDate);
            bundle.putString("keywordFromFilter", filterKeyword);
            bundle.putString("categoryFromFilter", filterCategory);
            bundle.putString("timeFromFilter", filterTime);
            googleMapFragment.setArguments(bundle);
            // pass the bundle to a new googleMapFragment
            view.getFragmentManager().beginTransaction().replace(R.id.content_frame_map, googleMapFragment).remove(view).commit();
        } else if (previousFragmentID == 3) {
            Fragment profileFragment = new ProfileFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("buskerDetailFromPreviousFragment", selectedBusker);
            bundle.putInt("previousFragmentID", previousFragmentID);
            profileFragment.setArguments(bundle);
            view.getFragmentManager().beginTransaction().replace(R.id.content_frame, profileFragment).remove(view).commit();
        }
        else {
            // show alertbox if the previous fragment ID is not valid
            showAlertBox("Error", "Unexpected error occurs. Please restart the app.", view.getActivity());
        }
    }

    // set the Firebase data listener, and update the data retrieved in the application
    public void setFireBaseListenerOnUser() {
        //establish connection to firebase
        firebase = new Firebase(DB_URL);
        // get data that match the specific email from Firebase
        Query queryRef = firebase.orderByChild("email").equalTo(selectedPerformance.getEmail());
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
                    view.buskerName.setText(users.get(0).getUsername());
                    selectedBusker = users.get(0);
                } else {
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
