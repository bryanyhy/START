package ambiesoft.start.activity;

import android.Manifest;
import android.app.FragmentManager;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import ambiesoft.start.R;
import ambiesoft.start.fragment.CreatePerformanceFragment;
import ambiesoft.start.fragment.GoogleMapFragment;
import ambiesoft.start.fragment.HomeFragment;

/**
 * Created by Bryanyhy on 22/8/2016.
 */
public class MainActivityPresenter {

    private MainActivity view;

    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;
    private static final int MY_PERMISSIONS_REQUEST_COARSE_LOCATION = 2;
    private FragmentManager fm;


    public MainActivityPresenter(FragmentManager fm) {
        this.fm = fm;
    }

    public void onTakeView(MainActivity view) {
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

    public void checkMenuTabItemSelection(int menuItemId) {
        if (menuItemId == R.id.bottomBarItemOne) {
            // The user selected first item in tab.
            // Show a new HomeFragment
            fm.beginTransaction().replace(R.id.content_frame, new HomeFragment()).commit();
        } else if (menuItemId == R.id.bottomBarItemTwo) {
            // The user reselected item number one, scroll your content to top.
            fm.beginTransaction().replace(R.id.content_frame_map, new GoogleMapFragment()).commit();
        }
        else if (menuItemId == R.id.bottomBarItemThree) {
            fm.beginTransaction().replace(R.id.content_frame, new CreatePerformanceFragment()).commit();
        }
    }

}
