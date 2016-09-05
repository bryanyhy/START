package ambiesoft.start.presenter.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.gigamole.navigationtabbar.ntb.NavigationTabBar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ambiesoft.start.R;
import ambiesoft.start.presenter.fragment.GoogleMapFragmentPresenter;
import ambiesoft.start.view.activity.MainActivity;
import ambiesoft.start.view.fragment.CreatePerformanceFragment;
import ambiesoft.start.view.fragment.GoogleMapFragment;
import ambiesoft.start.view.fragment.HomeFragment;
import ambiesoft.start.view.fragment.MoreFragment;
import ambiesoft.start.view.fragment.MyBuskingFragment;

import static ambiesoft.start.model.utility.AlertBox.showAlertBox;
import static ambiesoft.start.model.utility.NetworkAvailability.isNetworkAvailable;

/**
 * Created by Bryanyhy on 22/8/2016.
 */
public class MainActivityPresenter {


    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;
    private static final int MY_PERMISSIONS_REQUEST_COARSE_LOCATION = 2;
    private MainActivity view;

    public MainActivityPresenter(MainActivity view) {
        this.view = view;
    }

    public void askForLocationPermission() {
        // Ask for location permission once user start the application
        if (ActivityCompat.checkSelfPermission(view, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(view, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(view,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            ActivityCompat.requestPermissions(view,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_COARSE_LOCATION);
        }
    }

    public void checkMenuTabItemSelection(NavigationTabBar.Model model) {
        final String[] tabBarItemName = view.getResources().getStringArray(R.array.tab_bar_array);

        if (isNetworkAvailable(view)) {
            if (model.getTitle().matches(tabBarItemName[0])) {
                view.getFragmentManager().beginTransaction().replace(R.id.content_frame, new HomeFragment()).commit();
            } else if (model.getTitle().matches(tabBarItemName[1])) {
                showAlertBox("Sorry", "Functions will be added in next version.", view);
            } else if (model.getTitle().matches(tabBarItemName[2])) {
                if (GoogleMapFragmentPresenter.getCurrentGoogleMapFragment() != null) {
                    view.getFragmentManager().beginTransaction().remove(GoogleMapFragmentPresenter.getCurrentGoogleMapFragment())
                            .replace(R.id.content_frame, new MyBuskingFragment()).commit();
                } else {
                    view.getFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, new MyBuskingFragment()).commit();
                }
            } else if (model.getTitle().matches(tabBarItemName[3])) {
                showAlertBox("Sorry", "Functions will be added in next version.", view);
            } else if (model.getTitle().matches(tabBarItemName[4])) {
//                showAlertBox("Sorry", "Functions will be added in next version.", view);
                view.getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new MoreFragment()).commit();
            }
        } else {
            showAlertBox("Alert", "There is no internet connection. All functions are disabled.", view);
        }
    }

}
