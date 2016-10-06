package ambiesoft.start.view.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gigamole.navigationtabbar.ntb.NavigationTabBar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ambiesoft.start.R;
import ambiesoft.start.model.utility.PasswordValidator;
import ambiesoft.start.view.activity.MainActivity;
import static ambiesoft.start.model.utility.AlertBox.showAlertBox;
import static ambiesoft.start.model.utility.ProgressLoadingDialog.dismissProgressDialog;

/**
 * Created by Zelta on 21/09/16.
 */
public class MyAccountFragment extends Fragment{
    private NavigationTabBar navigationTabBar;
    private Button backButton;
    private static final String TAG = "ResetFragment";
    private Button resetButton;
    private EditText etPwd, etConPwd;

    private FirebaseAuth mFirebaseAuth;

    private PasswordValidator passwordValidator;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        passwordValidator = new PasswordValidator();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_myaccount, container, false);
        backButton = (Button) view.findViewById(R.id.resetBack);
        resetButton = (Button) view.findViewById(R.id.resetPwdButton);
        etPwd = (EditText) view.findViewById(R.id.resetPwdInput);
        etConPwd = (EditText) view.findViewById(R.id.resetPwdConfirm);

        mFirebaseAuth = FirebaseAuth.getInstance();


        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNewPwd(etPwd.getText().toString(),etConPwd.getText().toString());

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment moreFragment = new MoreFragment();
                getFragmentManager().beginTransaction().replace(R.id.content_frame, moreFragment).commit();

            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navigationTabBar = ((MainActivity) getActivity()).getNavigationTabBar();
        navigationTabBar.hide();
    }

    public void setNewPwd(String newPwd, String conPwd) {
        if (newPwd.trim().matches("") || conPwd.trim().matches("")) {
            showAlertBox("Error", "No empty input is allowed.", MyAccountFragment.this.getActivity());
        } else {
            if (passwordValidator.validate(newPwd)) {
                if (newPwd.trim().matches(conPwd.trim())) {
                    FirebaseUser user = mFirebaseAuth.getCurrentUser();
                    user.updatePassword(newPwd)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "User password updated.");
                                    }
                                }
                            });
                    showAlertBox("Success", "You have updated a new password", MyAccountFragment.this.getActivity());
                    Fragment moreFragment = new MoreFragment();
                    getFragmentManager().beginTransaction().replace(R.id.content_frame, moreFragment).addToBackStack(null).commit();


                } else {
                    showAlertBox("Error", "Password and confirm password are not matching.", MyAccountFragment.this.getActivity());
                }
            } else {
                showAlertBox("Error", "Password must contain 6 to 20 characters, and at least 1 digit and 1 uppercase character.", MyAccountFragment.this.getActivity());
            }
        }
    }



}
