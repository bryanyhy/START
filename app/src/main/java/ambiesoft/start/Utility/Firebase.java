package ambiesoft.start.Utility;

import android.content.Context;

/**
 * Created by Bryanyhy on 15/8/2016.
 */
public class Firebase {

    public void setupFirebase(Context context) {

        // initialised Firebase
        com.firebase.client.Firebase.setAndroidContext(context);
        // create a firebase reference
        com.firebase.client.Firebase myFirebaseRef = new com.firebase.client.Firebase("https://start-c9adf.firebaseio.com/");
    }

}
