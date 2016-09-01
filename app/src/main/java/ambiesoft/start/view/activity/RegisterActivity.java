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

/**
 * Created by Zelta on 31/08/16.
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    public EditText etEmail, etPwd;
    public TextView tvLogon, tvSkip;
    public Button btRegister;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    final static String DB_URL = "https://start-c9adf.firebaseio.com/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
//        firebase.setAndroidContext(this);

        etEmail = (EditText) findViewById(R.id.registerEmailInput);
        etPwd = (EditText) findViewById(R.id.registerPwdInput);
        btRegister = (Button) findViewById(R.id.registerButton);
        tvSkip = (TextView) findViewById(R.id.tvRegisterSkip);
        tvLogon = (TextView) findViewById(R.id.tvTurnToLogOn);

        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }
        });

        tvLogon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LogOnActivity.class));
            }
        });






        mAuth = FirebaseAuth.getInstance();


        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in

                } else {
                    // User is signed out

                }
                // [START_EXCLUDE]

                // [END_EXCLUDE]
            }
        };
        // [END auth_state_listener]


        btRegister.setOnClickListener(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void createAccount(String email, String password) {



        //TODO: 09-01 15:11:34.165 2915-2928/ambiesoft.start W/DynamiteModule: Local module descriptor class for com.google.firebase.auth not found.

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(RegisterActivity.this, LogOnActivity.class));

                        }


                    }
                });
        // [END create_user_with_email]
    }

    @Override
    public  void onClick(View view) {
        switch (view.getId()) {
            case R.id.registerButton:
                createAccount(etEmail.getText().toString(),etPwd.getText().toString());
        }
    }

}
