package ambiesoft.start.model.utility;

import android.content.Context;

import com.firebase.client.DataSnapshot;

import java.util.ArrayList;

import ambiesoft.start.model.dataclass.Performance;

/**
 * Created by Bryanyhy on 15/8/2016.
 */
// Class fore Firebase functions
public class Firebase {

    // reference link for the root of the project
    private static final String REF_LINK = "https://start-c9adf.firebaseio.com/";

    private static ArrayList<Performance> tempPerformanceList;

    // setup the Firebase, necessary before calling other function on Firebase
    public static void setupFirebase(Context context) {
        // initialised Firebase
        com.firebase.client.Firebase.setAndroidContext(context);
    }

    // save the performance to Firebase
    public static void savePerformance(Performance performance) {
        // create a firebase reference
        com.firebase.client.Firebase ref = new com.firebase.client.Firebase(REF_LINK);
        // the child of the root is performance
        com.firebase.client.Firebase performanceRef = ref.child("performance");
        performanceRef.push().setValue(performance);
    }

    // get all the performance result that match a specific date, create Performance object accordingly and add them to ArrayList
    public static ArrayList<Performance> getPerformanceListFromFirebaseByDate (DataSnapshot ds) {
        // initialize performance ArrayList
        tempPerformanceList = new ArrayList<>();
        // get all performance detail and save them into Performance ArrayList as Performance Object
        for (DataSnapshot dataSnapshot : ds.getChildren()) {
            String name = dataSnapshot.child("name").getValue().toString();
            String category = dataSnapshot.child("category").getValue().toString();
            String desc = dataSnapshot.child("desc").getValue().toString();
            String date = dataSnapshot.child("date").getValue().toString();
            String sTime = dataSnapshot.child("sTime").getValue().toString();
            String eTime = dataSnapshot.child("eTime").getValue().toString();
//            String address = dataSnapshot.child("address").getValue().toString();
            Double latitude = Double.parseDouble(dataSnapshot.child("lat").getValue().toString());
            Double longitude = Double.parseDouble(dataSnapshot.child("lng").getValue().toString());
            Performance performance;
//            if (address != null) {
//                performance = new Performance(name, category, desc, date, sTime, eTime, latitude, longitude, address);
//            } else {
                performance = new Performance(name, category, desc, date, sTime, eTime, latitude, longitude);
//            }
            tempPerformanceList.add(performance);
        }
        // return the ArrayList
        return tempPerformanceList;
    }

}
