package ambiesoft.start.view.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.core.Context;
import com.firebase.client.core.Tag;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import ambiesoft.start.R;
import ambiesoft.start.model.dataclass.User;
import ambiesoft.start.model.utility.PasswordValidator;

import static ambiesoft.start.model.utility.AlertBox.showAlertBox;
import static ambiesoft.start.model.utility.FirebaseUtility.saveUser;
import static ambiesoft.start.model.utility.FirebaseUtility.setupFirebase;
import static ambiesoft.start.model.utility.FirebaseUtility.uploadUserPortrait;
import static ambiesoft.start.model.utility.ProgressLoadingDialog.dismissProgressDialog;
import static ambiesoft.start.model.utility.ProgressLoadingDialog.showProgressDialog;
import static ambiesoft.start.model.utility.SoftKeyboard.hideSoftKeyboard;

/**
 * Created by Zelta on 31/08/16.
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    private static final int MY_PERMISSIONS_REQUEST_READ_MEDIA = 3;
    private static final int GALLERY_INTENT = 2;
    private static final String TAG = "RegisterActivity";
    private static final int NON_REG_USER = 0;
    private static final int REG_USER = 1;

    public EditText etEmail, etPwd, nameInput, etConPwd;
    public TextView tvLogon, tvSkip;
    public Button btRegister;
    public Button imageButton;
    public ImageView portraitView;
    public CheckBox checkBox;

    private PasswordValidator passwordValidator;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Intent intent;
    private int userType;
    private String emailInput;
    private String pwdInput;
    private String confirmPwdInput;
    private String usernameInput;
    private Uri portraitUri;
    private boolean confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // disable the screen orientation sensor, so the whole activity will be in Portrait mode
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        passwordValidator = new PasswordValidator();
        confirm = false;

        etEmail = (EditText) findViewById(R.id.registerEmailInput);
        etPwd = (EditText) findViewById(R.id.registerPwdInput);
        etConPwd = (EditText) findViewById(R.id.registerPwdConfirm);
        nameInput = (EditText) findViewById(R.id.nameInput);
        btRegister = (Button) findViewById(R.id.registerButton);
        imageButton = (Button) findViewById(R.id.imageButton);
        tvSkip = (TextView) findViewById(R.id.tvRegisterSkip);
        tvLogon = (TextView) findViewById(R.id.tvTurnToLogOn);
        portraitView = (ImageView) findViewById(R.id.portraitView);
        checkBox = (CheckBox) findViewById(R.id.checkBox);

        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(RegisterActivity.this);
                skipSignIn();
            }
        });

        tvLogon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(RegisterActivity.this);
                startActivity(new Intent(RegisterActivity.this, LogOnActivity.class));
            }
        });

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm = !confirm;
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
                    finish();
                } else {
                    // User is signed out
                    Log.i("System.out", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        btRegister.setOnClickListener(this);
        imageButton.setOnClickListener(this);
        imageButton.setEnabled(false);
        askForReadExternalPermission();
        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView2);
        // set on click listener for hiding soft keyboard
        scrollView.setOnTouchListener(this);
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

    private void createAccount(final String email, String password, String confirmPassword, final String username) {
        if (email.trim().matches("") || password.trim().matches("") || confirmPassword.trim().matches("") || username.trim().matches("")) {
            showAlertBox("Error", "No empty input is allowed.", this);
        } else {
            if (passwordValidator.validate(password)) {
                if (password.trim().matches(confirmPassword.trim())) {
                    if (confirm) {
                        userType = REG_USER;
                        showProgressDialog(RegisterActivity.this);
                        mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        // If sign in fails, display a message to the user. If sign in succeeds
                                        // the auth state listener will be notified and logic to handle the
                                        // signed in user can be handled in the listener.
                                        if (!task.isSuccessful()) {
                                            try {
                                                throw task.getException();
                                            } catch (FirebaseAuthUserCollisionException e) {
                                                showAlertBox("Error", "Registered email already exists.", RegisterActivity.this);
                                            } catch (FirebaseAuthWeakPasswordException e) {
                                                showAlertBox("Error", "Password must contain 6 to 20 characters, and at least 1 digit and 1 uppercase character.", RegisterActivity.this);
                                            }
                                            catch (Exception e) {
                                                showAlertBox("Error", "Registration is failed. Please try again.", RegisterActivity.this);
                                            }
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Registration Success.", Toast.LENGTH_LONG).show();
                                            setupFirebase(RegisterActivity.this);
                                            saveUser(new User(email, username, "No category yet", "No description yet"));
                                            if (portraitUri != null) {
                                                uploadUserPortrait(portraitUri, emailInput, RegisterActivity.this);
                                            }
                                        }
                                        dismissProgressDialog();
                                    }
                                });
                    } else {
                        showAlertBox("Error", "You must read and agree on the Busking Guidelines 2011 issued by City of Melbourne before registration.", this);
                    }
                } else {
                    showAlertBox("Error", "Password and confirm password are not matching.", this);
                }
            } else {
                showAlertBox("Error", "Password must contain 6 to 20 characters, and at least 1 digit and 1 uppercase character.", this);
            }
        }
    }

    @Override
    public  void onClick(View view) {
        switch (view.getId()) {
            case R.id.registerButton:
                hideSoftKeyboard(RegisterActivity.this);
                emailInput = etEmail.getText().toString().trim();
                pwdInput = etPwd.getText().toString().trim();
                confirmPwdInput = etConPwd.getText().toString().trim();
                usernameInput = nameInput.getText().toString().trim();
                createAccount(emailInput, pwdInput, confirmPwdInput, usernameInput);
                break;
            case R.id.imageButton:
                hideSoftKeyboard(RegisterActivity.this);
                selectPortrait();
                break;
        }
    }

    // once the select image button is clicked, show Gallery to user
    public void selectPortrait() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_INTENT);
    }

    // save the uri once image is selected by user from Gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            portraitUri = data.getData();
            portraitView.setImageURI(portraitUri);
        }
    }

    public void skipSignIn() {
        userType = NON_REG_USER;
        showProgressDialog(RegisterActivity.this);
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

    public void askForReadExternalPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_MEDIA);
        } else {
            imageButton.setEnabled(true);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_MEDIA:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    imageButton.setEnabled(true);
                }
                break;
            default:
                break;
        }
    }

    // hide soft keyboard when clicking blank space
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow((null == getCurrentFocus()) ?
                null : getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        return false;
    }

    @Override
    public void onBackPressed() {

    }
}
