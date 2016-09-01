package ambiesoft.start.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.core.Tag;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ambiesoft.start.R;

import static ambiesoft.start.model.utility.AlertBox.showAlertBox;

/**
 * Created by Zelta on 31/08/16.
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RegisterActivity";
    private static final int NON_REG_USER = 0;
    private static final int REG_USER = 1;

    public EditText etEmail, etPwd;
    public TextView tvLogon, tvSkip;
    public Button btRegister;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Intent intent;
    private int userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etEmail = (EditText) findViewById(R.id.registerEmailInput);
        etPwd = (EditText) findViewById(R.id.registerPwdInput);
        btRegister = (Button) findViewById(R.id.registerButton);
        tvSkip = (TextView) findViewById(R.id.tvRegisterSkip);
        tvLogon = (TextView) findViewById(R.id.tvTurnToLogOn);

        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipSignIn();
            }
        });

        tvLogon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LogOnActivity.class));
            }
        });

        mFirebaseAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.i("System.out", "onAuthStateChanged:signed_in:" + user.getUid());
                    setupBundleToMainActivity(user, userType);
                    // Pass intent to MainActivity
                    startActivity(intent);
                } else {
                    // User is signed out
                    Log.i("System.out", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        btRegister.setOnClickListener(this);
        // For passing bundle from activity to activity
        intent = new Intent(RegisterActivity.this, MainActivity.class);
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

    private void createAccount(String email, String password) {
        if (email.trim().matches("") || password.trim().matches("")) {
            showAlertBox("Error", "Email or password cannot be empty.", this);
        } else {
            userType = REG_USER;
            mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Registration is failed.", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Registration Success.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    @Override
    public  void onClick(View view) {
        switch (view.getId()) {
            case R.id.registerButton:
                createAccount(etEmail.getText().toString(),etPwd.getText().toString());
        }
    }

    public void skipSignIn() {
        userType = NON_REG_USER;
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
                            Toast.makeText(RegisterActivity.this, "Skip Sign In failed.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Anonymous Login Success.", Toast.LENGTH_LONG).show();
                        }
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

}
