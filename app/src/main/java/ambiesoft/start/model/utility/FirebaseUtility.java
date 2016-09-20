package ambiesoft.start.model.utility;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import ambiesoft.start.R;
import ambiesoft.start.model.dataclass.Performance;
import ambiesoft.start.model.dataclass.User;
import ambiesoft.start.view.activity.RegisterActivity;

import static ambiesoft.start.model.utility.ProgressLoadingDialog.dismissProgressDialog;

/**
 * Created by Bryanyhy on 15/8/2016.
 */
// Class fore FirebaseUtility functions
public class FirebaseUtility {

    // reference link for the root of the project
    private static final String REF_LINK = "https://start-c9adf.firebaseio.com/";

    private static ArrayList<Performance> tempPerformanceList;
    private static ArrayList<User> tempUserList;

    // setup the FirebaseUtility, necessary before calling other function on FirebaseUtility
    public static void setupFirebase(Context context) {
        // initialised FirebaseUtility
        com.firebase.client.Firebase.setAndroidContext(context);
    }

    // save the performance to FirebaseUtility
    public static void savePerformance(Performance performance) {
        // create a firebase reference
        com.firebase.client.Firebase ref = new com.firebase.client.Firebase(REF_LINK);
        // the child of the root is performance
        com.firebase.client.Firebase performanceRef = ref.child("performance");
        performanceRef.push().setValue(performance);
    }

    public static void updatePerformance(Performance performance, String key) {
        // create a firebase reference
        com.firebase.client.Firebase ref = new com.firebase.client.Firebase(REF_LINK);
        // the child of the root is performance
        Log.i("System.out", "" + performance.getKey());
        com.firebase.client.Firebase performanceRef = ref.child("performance").child(key);
        performanceRef.setValue(performance);
    }

    public static void deletePerformanceFromFirebase(String key) {
        // create a firebase reference
        com.firebase.client.Firebase ref = new com.firebase.client.Firebase(REF_LINK);
        com.firebase.client.Firebase performanceRef = ref.child("performance").child(key);
        performanceRef.removeValue();
    }

    // get all the performance result that match a specific date, create Performance object accordingly and add them to ArrayList
    public static ArrayList<Performance> getPerformanceListFromFirebase (DataSnapshot ds) {
        // initialize performance ArrayList
        tempPerformanceList = new ArrayList<>();
        // get all performance detail and save them into Performance ArrayList as Performance Object
        for (DataSnapshot dataSnapshot : ds.getChildren()) {
            String key = dataSnapshot.getKey();
            String name = dataSnapshot.child("name").getValue().toString();
            String category = dataSnapshot.child("category").getValue().toString();
            String desc = dataSnapshot.child("desc").getValue().toString();
            String date = dataSnapshot.child("date").getValue().toString();
            String sTime = dataSnapshot.child("sTime").getValue().toString();
            String eTime = dataSnapshot.child("eTime").getValue().toString();
            int duration = Integer.parseInt(dataSnapshot.child("duration").getValue().toString());
            String address = dataSnapshot.child("address").getValue().toString();
            Double latitude = Double.parseDouble(dataSnapshot.child("lat").getValue().toString());
            Double longitude = Double.parseDouble(dataSnapshot.child("lng").getValue().toString());
            String email = dataSnapshot.child("email").getValue().toString();
            Performance performance;
//            if (address != null) {
            performance = new Performance(key, name, category, desc, date, sTime, eTime, duration, latitude, longitude, address, email);
//            } else {
//                performance = new Performance(name, category, desc, date, sTime, eTime, latitude, longitude);
//            }
            tempPerformanceList.add(performance);
        }

        // return the ArrayList
        return tempPerformanceList;
    }

    public static void saveUser(User user) {
        // create a firebase reference
        com.firebase.client.Firebase ref = new com.firebase.client.Firebase(REF_LINK);
        // the child of the root is performance
        com.firebase.client.Firebase userRef = ref.child("user");
        userRef.push().setValue(user);
    }

    public static void updateUser(User user, String key) {
        // create a firebase reference
        com.firebase.client.Firebase ref = new com.firebase.client.Firebase(REF_LINK);
        // the child of the root is performance
        Log.i("System.out", "" + user.getKey());
        com.firebase.client.Firebase userRef = ref.child("user").child(key);
        userRef.setValue(user);
    }

    // upload the portrait onto Firebase storage
    public static void uploadUserPortrait(Uri uri, String emailInput, final Activity activity) {
        StorageReference mStorage = FirebaseStorage.getInstance().getReference();
        StorageReference userImagePortraitStorage = mStorage.child(emailInput).child("image").child("portrait");
        userImagePortraitStorage.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(activity, "Image upload done.", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, "Image upload fail.", Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void setUserPortraitUri(String email, final Context context, final ImageView imageView) {
        StorageReference mStorage = FirebaseStorage.getInstance().getReference();
        StorageReference userImagePortraitStorage = mStorage.child(email).child("image").child("portrait");
        userImagePortraitStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for Firebase
                Picasso.with(context).load(uri).into(imageView);
                Log.i("System.out", "Can get image");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Picasso.with(context).load(R.drawable.portrait_thumb).into(imageView);
                Log.i("System.out", "Can't get image");
            }
        });
    }

    // get all the performance result that match a specific date, create Performance object accordingly and add them to ArrayList
    public static ArrayList<User> getUserListFromFirebase (DataSnapshot ds) {
        // initialize performance ArrayList
        tempUserList = new ArrayList<>();
        // get all performance detail and save them into Performance ArrayList as Performance Object
        for (DataSnapshot dataSnapshot : ds.getChildren()) {
            String key = dataSnapshot.getKey();
            String username = dataSnapshot.child("username").getValue().toString();
            String email = dataSnapshot.child("email").getValue().toString();
            String category = dataSnapshot.child("category").getValue().toString();
            String desc = dataSnapshot.child("desc").getValue().toString();
            User user;
            user = new User(key, email, username, category, desc);
            tempUserList.add(user);
        }
        // return the ArrayList
        return tempUserList;
    }
}
