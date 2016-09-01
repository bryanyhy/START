package ambiesoft.start.model.utility;

import android.content.Context;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ambiesoft.start.model.dataclass.Performance;

/**
 * Created by Bryanyhy on 15/8/2016.
 */
// Class fore FirebaseUtility functions
public class FirebaseUtility {

    // reference link for the root of the project
    private static final String REF_LINK = "https://start-c9adf.firebaseio.com/";

    private static ArrayList<Performance> tempPerformanceList;
    private static ArrayList<Performance> buskerPerformanceList;

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

}
