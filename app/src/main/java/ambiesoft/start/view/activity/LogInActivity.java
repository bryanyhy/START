package ambiesoft.start.view.activity;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import ambiesoft.start.R;
import ambiesoft.start.model.utility.MovingImageView;
import io.fabric.sdk.android.Fabric;

import static ambiesoft.start.model.utility.ProgressLoadingDialog.dismissProgressDialog;
import static ambiesoft.start.model.utility.ProgressLoadingDialog.showProgressDialog;

/**
 * Created by Zelta on 31/08/16.
 */
public class LogInActivity extends AppCompatActivity{
//
    private static final int NON_REG_USER = 0;
//
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mFirebaseUser;
//
//    private Intent in;

    private static final String TAG = "Welcome Page";

    MovingImageView image;
    boolean toggleState = true;
    boolean toggleCustomMovement = true;
    int[] imageList = {R.drawable.anotherworld, R.drawable.futurecity, R.drawable.spacecargo, R.drawable.city};
    int pos = 0;
    private int userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
//
        mFirebaseAuth = FirebaseAuth.getInstance();

        //REALLY IMPORTANT
        // Sign Out every time application start
        mFirebaseAuth.signOut();
        // must be null as we signed out
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        // For passing bundle from activity to activity

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    Bundle bundle = new Bundle();

                    bundle.putString("email", "Anonymous");

                    bundle.putInt("userType", userType);
//                    in.putExtras(bundle);
                    //setupBundleToMainActivity(user, userType);
                    // Pass intent to MainActivity
                    Intent in = new Intent(LogInActivity.this, MainActivity.class);
                    in.putExtras(bundle);
                    startActivity(in);
                    finish();
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

//        MovingImageView image = (MovingImageView) findViewById(R.id.imageBg);
//        image.getMovingAnimator().setInterpolator(new BounceInterpolator());
//        image.getMovingAnimator().setSpeed(100);
//        image.getMovingAnimator().addCustomMovement().
//                addDiagonalMoveToDownRight().
//                addHorizontalMoveToLeft().
//                addDiagonalMoveToUpRight().
//                addVerticalMoveToDown().
//                addHorizontalMoveToLeft().
//                addVerticalMoveToUp().
//                start();



        image = (MovingImageView) findViewById(R.id.imageBg);
        image.getMovingAnimator().addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                Log.i("Sample MovingImageView", "Start");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.i("Sample MovingImageView", "End");
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                Log.i("Sample MovingImageView", "Cancel");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                Log.i("Sample MovingImageView", "Repeat");
            }
        });
    }

//    public void clickImage(View v) {
//        if (toggleState) {
//            image.getMovingAnimator().pause();
//            Toast.makeText(this, "Pause", Toast.LENGTH_SHORT).show();
//        } else {
//            image.getMovingAnimator().resume();
//            Toast.makeText(this, "Resume", Toast.LENGTH_SHORT).show();
//        }
//        toggleState = !toggleState;
//    }

//    public void clickTitle(View v) {
//        pos = (pos + 1) >= imageList.length ? 0 : pos + 1;
//        image.setImageResource(imageList[pos]);
//        toggleCustomMovement = true;
//        Toast.makeText(this, "Next picture", Toast.LENGTH_SHORT).show();
//    }

//    public void clickText(View v) {
//        if(toggleCustomMovement) {
//            image.getMovingAnimator().addCustomMovement().addDiagonalMoveToDownRight().addHorizontalMoveToLeft().addDiagonalMoveToUpRight()
//                    .addVerticalMoveToDown().addHorizontalMoveToLeft().addVerticalMoveToUp().start();
//            Toast.makeText(this, "Custom movement", Toast.LENGTH_SHORT).show();
//        } else {
//            image.getMovingAnimator().clearCustomMovement();
//            Toast.makeText(this, "Default movement", Toast.LENGTH_SHORT).show();
//        }
//        toggleCustomMovement = !toggleCustomMovement;
//    }

    public void clickBuskerText(View v){
        Intent intent = new Intent(LogInActivity.this, LogOnActivity.class);
        startActivity(intent);
    }

    public void clickFanText(View v){
        userType = NON_REG_USER;
        showProgressDialog(LogInActivity.this);
        mFirebaseAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInAnonymously:onComplete:" + task.isSuccessful());
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInAnonymously", task.getException());
                            Toast.makeText(LogInActivity.this, "Skip Sign In failed.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Anonymous Login Success.", Toast.LENGTH_LONG).show();
                        }
                        dismissProgressDialog();
                    }
                });
    }

    //setting auth listener
    @Override
    public void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        if(image != null)
//            image.getMovingAnimator().cancel();
//    }

//     setup the content in intent send to MainActivity
//    public void setupBundleToMainActivity(FirebaseUser user, int userType) {
//        if (user != null) {
//            Bundle bundle = new Bundle();
//
//            bundle.putString("email", "Anonymous");
//
//            bundle.putInt("userType", userType);
//            in.putExtras(bundle);
//        }
//    }
}
