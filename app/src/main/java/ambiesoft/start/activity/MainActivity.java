package ambiesoft.start.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import ambiesoft.start.fragment.CreatePerformanceFragment;
import ambiesoft.start.fragment.GoogleMapFragment;
import ambiesoft.start.fragment.HomeFragment;
import ambiesoft.start.R;

import static ambiesoft.start.utility.AlertBox.showAlertBox;
import static ambiesoft.start.utility.NetworkAvailability.isNetworkAvailable;
import static ambiesoft.start.utility.ProgressLoadingDialog.dismissProgressDialog;

/**
 * Main activity for the tab bar and navigation bar, and also the content frame for showing all fragments
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private MainActivityPresenter presenter;

    private BottomBar mBottomBar;
    private android.app.FragmentManager fm = getFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        // Ask for location permission once user start the application
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
//                    MY_PERMISSIONS_REQUEST_COARSE_LOCATION);
//        }

        setContentView(R.layout.activity_main);

        if (presenter == null) {
            presenter = new MainActivityPresenter(fm);
        }
        presenter.onTakeView(this);
        presenter.askForLocationPermission();

        // setting up the bottom navigation bar
        mBottomBar = BottomBar.attach(findViewById(R.id.content_frame), savedInstanceState);
        mBottomBar.noTopOffset();
        mBottomBar.noNavBarGoodness();
        // set the items in the bottom bar
        mBottomBar.setItems(R.menu.bottombar_menu);
        mBottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
            // when a menu tab is selected
            // By default the first tab button is selected, which shows the HomeFragment
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                presenter.checkMenuTabItemSelection(menuItemId);
            }

            // when a menu tab is reselected
            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
            }
        });

        // Setting colors for different tabs when there's more than three of them.
//        mBottomBar.mapColorForTab(0, ContextCompat.getColor(this, R.color.colorAccent));
//        mBottomBar.mapColorForTab(1, 0xFF5D4037);
//        mBottomBar.mapColorForTab(2, "#7B1FA2");
//        mBottomBar.mapColorForTab(3, "#FF5252");
//        mBottomBar.mapColorForTab(4, "#FF9800");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // hide the floating action button. We only need them on fragment but not activity.
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        mBottomBar.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_map) {
            // check if network is available
            if (isNetworkAvailable(this) == false) {
                // if no network is detected
                dismissProgressDialog();
                showAlertBox("Alert", "There is no internet connection detected. Map access is disabled.", this);
            } else {
                // map is selected in navigation bar
                fm.beginTransaction().replace(R.id.content_frame, new GoogleMapFragment()).commit();
            }

        } else if (id == R.id.nav_crePer) {
            // check if network is available
            if (isNetworkAvailable(this) == false) {
                // if no network is detected
                dismissProgressDialog();
                showAlertBox("Alert", "There is no internet connection detected. Create performance is disabled.", this);
            } else {
                // create performance is selected in navigation bar
                Toast.makeText(this, "Create Performance", Toast.LENGTH_SHORT).show();
                fm.beginTransaction().replace(R.id.content_frame, new CreatePerformanceFragment()).commit();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
