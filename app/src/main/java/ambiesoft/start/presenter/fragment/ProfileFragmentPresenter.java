package ambiesoft.start.presenter.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

import ambiesoft.start.model.dataclass.Performance;
import ambiesoft.start.model.dataclass.User;
import ambiesoft.start.view.activity.MainActivity;
import ambiesoft.start.view.fragment.ProfileFragment;

import static ambiesoft.start.model.utility.BundleItemChecker.getFilterCategoryFromBundle;
import static ambiesoft.start.model.utility.BundleItemChecker.getFilterDateFromBundle;
import static ambiesoft.start.model.utility.BundleItemChecker.getFilterKeywordFromBundle;
import static ambiesoft.start.model.utility.BundleItemChecker.getFilterTimeFromBundle;
import static ambiesoft.start.model.utility.BundleItemChecker.getPreviousFragmentIDFromBundle;
import static ambiesoft.start.model.utility.BundleItemChecker.getSelectedBuskerFromBundle;
import static ambiesoft.start.model.utility.BundleItemChecker.getSelectedPerformanceFromBundle;
import static ambiesoft.start.model.utility.FirebaseUtility.getUserListFromFirebase;
import static ambiesoft.start.model.utility.FirebaseUtility.setUserPortraitUri;

/**
 * Created by Zelta on 7/09/16.
 */
public class ProfileFragmentPresenter {
    private final static String DB_URL = "https://start-c9adf.firebaseio.com/user";

    private ProfileFragment view;
    private Firebase firebase;
    private ArrayList<User> users;
    private String email;

    private User selectedBusker;
    private int previousFragmentID;

    public ProfileFragmentPresenter(ProfileFragment view){
        this.view = view;
        getBundleFromPreviousFragment();
//        setFireBaseListenerOnUser();
//        setUserPortraitUri(this.email, view.getContext(), view.portrait);
    }

    public void getBundleFromPreviousFragment() {
        Bundle bundle = view.getArguments();
        if (bundle != null) {
            // if bundle exists, get the filter values
            selectedBusker = getSelectedBuskerFromBundle(bundle);
            previousFragmentID = getPreviousFragmentIDFromBundle(bundle);
            // set the textView from data in bundle accordingly
            view.buskerName.setText(selectedBusker.getUsername());
            setUserPortraitUri(selectedBusker.getEmail(), view.getContext(), view.portrait);
//            setFireBaseListenerOnUser();
        }
    }

//    // set the FirebaseUtility data listener, and update the data retrieved in the application
//    public void setFireBaseListenerOnUser() {
//        //establish connection to firebase
//        firebase = new Firebase(DB_URL);
//        // get data that match the specific email from Firebase
//        Query queryRef = firebase.orderByChild("email").equalTo(email);
//        // value event listener that is triggered everytime data in Firebase's Performance root is updated
//        // Retrieve all performance's attributes from each post on Firebase, when any data is updated in the FirebaseUtility
//        queryRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot ds) {
//                Log.i("System.out", "Firebase has update");
//                // initialize performance ArrayList
//                users = new ArrayList<>();
//                // get all performance detail and save them into Performance ArrayList as Performance Object
//                users = getUserListFromFirebase(ds);
//                // check if any matching result is retrieved
//                if (users.size() != 0) {
//                    view.buskerName.setText(users.get(0).getUsername());
//                } else {
//                }
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });
//    }
}
