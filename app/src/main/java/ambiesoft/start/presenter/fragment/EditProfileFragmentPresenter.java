package ambiesoft.start.presenter.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import ambiesoft.start.R;
import ambiesoft.start.model.dataclass.User;
import ambiesoft.start.view.fragment.EditProfileFragment;
import ambiesoft.start.view.fragment.ProfileFragment;

import static ambiesoft.start.model.utility.AlertBox.showAlertBox;
import static ambiesoft.start.model.utility.BundleItemChecker.getSelectedBuskerFromBundle;
import static ambiesoft.start.model.utility.FirebaseUtility.setUserPortraitUri;
import static ambiesoft.start.model.utility.FirebaseUtility.updateUser;
import static ambiesoft.start.model.utility.FirebaseUtility.uploadUserPortrait;

/**
 * Created by Bryanyhy on 20/9/2016.
 */
public class EditProfileFragmentPresenter {

    private static final int MY_PERMISSIONS_REQUEST_READ_MEDIA = 3;
    private static final int GALLERY_INTENT = 2;
    private EditProfileFragment view;
    private User selectedBusker;
    private Uri portraitUri;
    private String username, category, desc;

    public EditProfileFragmentPresenter() {}

    public EditProfileFragmentPresenter(EditProfileFragment view){
        this.view = view;
        getBundleFromPreviousFragment();
    }

    public void getBundleFromPreviousFragment() {
        Bundle bundle = view.getArguments();
        if (bundle != null) {
            // if bundle exists, get the filter values
            selectedBusker = getSelectedBuskerFromBundle(bundle);
            // set the textView from data in bundle accordingly
            view.username.setText(selectedBusker.getUsername());
            view.userCategory.setText(selectedBusker.getCategory());
            view.userDesc.setText(selectedBusker.getDesc());
            setUserPortraitUri(selectedBusker.getEmail(), view.getContext(), view.portrait);
        }
    }

    public void saveChanges() {
        category = view.userCategory.getText().toString();
        desc = view.userDesc.getText().toString();
        User user = new User(selectedBusker.getEmail(), selectedBusker.getUsername(), category, desc, selectedBusker.getHashtag());
        updateUser(user, selectedBusker.getKey());
        if (portraitUri != null) {
            uploadUserPortrait(portraitUri, selectedBusker.getEmail(), view.getActivity());
        }
        Toast.makeText(view.getActivity(), "Profile is updated.", Toast.LENGTH_SHORT).show();
        view.getFragmentManager().beginTransaction().replace(R.id.content_frame, new ProfileFragment()).remove(view).commit();
    }

    public void backToPreviousFragment() {
        view.getFragmentManager().beginTransaction().replace(R.id.content_frame, new ProfileFragment()).remove(view).commit();
    }

    public void updatePortrait() {
        askForReadExternalPermission();
    }

    public void askForReadExternalPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(view.getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(view.getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_MEDIA);
        } else {
            selectPortrait();
        }
    }

    // once the select image button is clicked, show Gallery to user
    public void selectPortrait() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        view.getActivity().startActivityForResult(intent, GALLERY_INTENT);
    }

    public void getPortraitUri(Intent data) {
        portraitUri = data.getData();
        view.portrait.setImageURI(portraitUri);
    }

    //    // save the uri once image is selected by user from Gallery
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
//            portraitUri = data.getData();
//            portraitView.setImageURI(portraitUri);
//        }
//    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case MY_PERMISSIONS_REQUEST_READ_MEDIA:
//                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
////                    imageButton.setEnabled(true);
//                }
//                break;
//            default:
//                break;
//        }
//    }
}
