package ambiesoft.start.view.activity;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.proxy.ProxyApi;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import ambiesoft.start.R;
import ambiesoft.start.model.utility.MovingImageView;
import ambiesoft.start.model.utility.ProgressGenerator;

import static ambiesoft.start.model.utility.AlertBox.showAlertBox;
import static ambiesoft.start.model.utility.ProgressLoadingDialog.dismissProgressDialog;
import static ambiesoft.start.model.utility.ProgressLoadingDialog.showProgressDialog;
import static ambiesoft.start.model.utility.SoftKeyboard.hideSoftKeyboard;

/**
 * Created by Zelta on 31/08/16.
 */
public class LogOnActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, ProgressGenerator.OnCompleteListener {

    private static final String TAG = "LogOnActivity";
    private static final int RC_SIGN_IN = 9001;
    private static final int NON_REG_USER = 0;
    private static final int REG_USER = 1;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient mGoogleApiClient;
    private Intent intent;
    private int userType;

    private EditText etEmail, etPwd;
    private TextView tvRegister, tvSkip;
    private Button normalSignInButton, googleSignInButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logon);

        etEmail = (EditText) findViewById(R.id.loginEmail);
        etPwd = (EditText) findViewById(R.id.loginPwd);
        tvRegister = (TextView) findViewById(R.id.tvTurnToRegister);
        tvSkip = (TextView) findViewById(R.id.tvLoginSkip);
        tvRegister = (TextView) findViewById(R.id.tvTurnToRegister);
//        normalSignInButton = (Button) findViewById(R.id.normalSignInButton);
        googleSignInButton = (Button) findViewById(R.id.googleSignInButton);

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(LogOnActivity.this);
                startActivity(new Intent(LogOnActivity.this, RegisterActivity.class));
            }
        });

        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(LogOnActivity.this);
                skipSignIn();
            }
        });

        //Use PorgressGenerator and android process generator library
        //Initial login button
        final ProgressGenerator progressGenerator = new ProgressGenerator(this);
        final ActionProcessButton normalSignInButtonAction = (ActionProcessButton) findViewById(R.id.normalSignInButton);

        //Set process model as endless modle
        normalSignInButtonAction.setMode(ActionProcessButton.Mode.ENDLESS);

        normalSignInButtonAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(LogOnActivity.this);

                //When auth success, start progress bar with sign in button

                progressGenerator.start(normalSignInButtonAction);
                normalSignInButtonAction.setEnabled(true);
                normalSignIn(etEmail.getText().toString(),etPwd.getText().toString());
            }
        });

        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(LogOnActivity.this);
                googleSignIn();
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Initialize FirebaseAuth
        mFirebaseAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    setupBundleToMainActivity(user, userType);
                    // Pass intent to MainActivity
                    startActivity(intent);
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        // For passing bundle from activity to activity
        intent = new Intent(LogOnActivity.this, MainActivity.class);


    }

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

    // Called when sign in button is clicked, authenticate the email and password
    public void normalSignIn(String email, String password) {
        userType = REG_USER;
        if (email.trim().matches("") || password.trim().matches("")) {
            showAlertBox("Error", "Email or password cannot be empty.", this);
        } else {
            showProgressDialog(LogOnActivity.this);
            mFirebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "signInWithEmail:failed", task.getException());
                                Toast.makeText(LogOnActivity.this, "Normal Login failed.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Normal Login Success.", Toast.LENGTH_LONG).show();

                            }
                            dismissProgressDialog();
                        }
                    });
        }
    }

    public void skipSignIn() {
        userType = NON_REG_USER;
        showProgressDialog(LogOnActivity.this);
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
                            Toast.makeText(LogOnActivity.this, "Skip Sign In failed.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Anonymous Login Success.", Toast.LENGTH_LONG).show();
                        }
                        dismissProgressDialog();
                    }
                });
    }

    // setup the content in intent send to MainActivity
    public void setupBundleToMainActivity(FirebaseUser user, int userType) {
        if (user != null) {
            Bundle bundle = new Bundle();
            if (userType == REG_USER) {
                String email = user.getEmail();
                bundle.putString("email", email);
            } else {
                bundle.putString("email", "Anonymous");
            }
            bundle.putInt("userType", userType);
            intent.putExtras(bundle);
        }
    }

    // ---------------------------------- Belows are for Google Login ----------------------------------

    public void googleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed
                Log.e(TAG, "Google Sign In failed.");
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        userType = REG_USER;
        showProgressDialog(LogOnActivity.this);
        Log.d(TAG, "firebaseAuthWithGooogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LogOnActivity.this, "Google Login failed.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Google Login Success.", Toast.LENGTH_LONG).show();
                        }
                        dismissProgressDialog();
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onComplete() {

    }
}
