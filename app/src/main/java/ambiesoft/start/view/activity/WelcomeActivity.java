package ambiesoft.start.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Timer;
import java.util.TimerTask;

import ambiesoft.start.R;
import ambiesoft.start.view.fragment.HomeFragment;

/**
 * Created by Zelta on 30/08/16.
 */
public class WelcomeActivity extends Activity {

    private Handler handler;

    private long delay = 3000;
    private int i = 0;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        // Sign Out every time application start
        mFirebaseAuth.signOut();
        // must be null as we signed out
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        Timer timer = new Timer();
        timer.schedule(task, delay);
    }

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            Intent intent;
            if (mFirebaseUser == null) {
                // Not signed in, launch the Sign In activity
                intent = new Intent().setClass(WelcomeActivity.this,
                        LogOnActivity.class).addFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TOP);
            } else {
//                mUsername = mFirebaseUser.getDisplayName();
//                if (mFirebaseUser.getPhotoUrl() != null) {
//                    mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
//                }
                intent = new Intent().setClass(WelcomeActivity.this,
                        MainActivity.class).addFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
            startActivity(intent);
            finish();
        }
    };

}
