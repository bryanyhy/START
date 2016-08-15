package ambiesoft.start.utility;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import ambiesoft.start.dataclass.Performance;

/**
 * Created by Bryanyhy on 15/8/2016.
 */
public class Firebase {

    private static final String REF_LINK = "https://start-c9adf.firebaseio.com/";

    public static void setupFirebase(Context context) {

        // initialised Firebase
        com.firebase.client.Firebase.setAndroidContext(context);
    }

    public static void savePerformance(Performance performance) {
        // create a firebase reference
        com.firebase.client.Firebase ref = new com.firebase.client.Firebase(REF_LINK);
        com.firebase.client.Firebase performanceRef = ref.child("performance");
        performanceRef.push().setValue(performance);
    }



}
