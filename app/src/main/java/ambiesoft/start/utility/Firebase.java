package ambiesoft.start.utility;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;

import ambiesoft.start.dataclass.Performance;

import static ambiesoft.start.utility.AlertBox.showAlertBox;
import static ambiesoft.start.utility.FilterResult.advancedFilteringOnPerformanceList;

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
        Log.i("System.out","Firebase has update.");
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
            Double latitude = Double.parseDouble(dataSnapshot.child("lat").getValue().toString());
            Double longitude = Double.parseDouble(dataSnapshot.child("lng").getValue().toString());
            Performance performance = new Performance(name, category, desc, date, sTime, eTime, latitude, longitude);
            tempPerformanceList.add(performance);
        }
        // return the ArrayList
        return tempPerformanceList;
    }

}
