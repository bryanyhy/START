package ambiesoft.start.view.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.gigamole.navigationtabbar.ntb.NavigationTabBar;

import java.util.ArrayList;

import ambiesoft.start.R;
import ambiesoft.start.presenter.activity.MainActivityPresenter;
import ambiesoft.start.view.fragment.FilterResultFragment;

/**
 * Main activity for the tab bar and navigation bar, and also the content frame for showing all fragments
 */
public class MainActivity extends AppCompatActivity {

    private MainActivityPresenter presenter;

    public FloatingActionButton fab;
    public AppBarLayout abl;
    private android.app.FragmentManager fm = getFragmentManager();
    private NavigationTabBar navigationTabBar;
    private NestedScrollView nsv;
    private FrameLayout fl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        // setting up the bottom navigation bar
        navigationTabBarSetting();

        if (presenter == null) {
            // setup presenter class
            presenter = new MainActivityPresenter(this);
        }
        // ask for location permission
        presenter.askForLocationPermission();
        // set the floating action button and hide it
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.hide();

////        mBottomBar = (BottomBar) findViewById(R.id.bottomBar);
//        mBottomBar = (BottomBar) findViewById(R.id.bottomBar);
////        if (true) {
////            mBottomBar = (BottomBar) findViewById(R.id.bottomBar);
////        } else {
////            mBottomBar = (BottomBar) findViewById(R.id.bottomBar_normal);
////        }
////        mBottomBar.noTopOffset();
////        mBottomBar.noNavBarGoodness();
////        mBottomBar.setBackgroundColor(Color.parseColor("#9f90af"));
//        // set the items in the bottom bar
////        mBottomBar.setItems(R.menu.bottombar_menu);
//        mBottomBar.setOnTabSelectListener(new OnTabSelectListener() {
//            @Override
//            public void onTabSelected(@IdRes int tabId) {
//                FrameLayout fl = (FrameLayout) findViewById(R.id.content_frame_map);
//                fl.removeAllViews();
////                presenter.checkMenuTabItemSelection(tabId);
//            }
//        });
//        mBottomBar.setOnTabReselectListener(new OnTabReselectListener() {
//            @Override
//            public void onTabReSelected(@IdRes int tabId) {
//            }
//        });
//
//
////        mBottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
////            // when a menu tab is selected
////            // By default the first tab button is selected, which shows the HomeFragment
////            @Override
////            public void onMenuTabSelected(@IdRes int menuItemId) {
////                presenter.checkMenuTabItemSelection(menuItemId);
////            }
////
////            // when a menu tab is reselected
////            @Override
////            public void onMenuTabReSelected(@IdRes int menuItemId) {
////            }
////        });
//
//        // Setting colors for different tabs when there's more than three of them.
////        mBottomBar.mapColorForTab(0, ContextCompat.getColor(this, R.color.colorAccent));
////        mBottomBar.mapColorForTab(1, 0xFF5D4037);
////        mBottomBar.mapColorForTab(2, "#7B1FA2");
////        mBottomBar.mapColorForTab(3, "#FF5252");
////        mBottomBar.mapColorForTab(4, "#FF9800");


        abl = (AppBarLayout) findViewById(R.id.appbar);
        nsv = (NestedScrollView) findViewById(R.id.content_frame);
        fl = (FrameLayout) findViewById(R.id.content_frame_map);
        // collapsing toolbar setting
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setExpandedTitleColor(Color.parseColor("#009F90AF"));
        collapsingToolbar.setCollapsedTitleTextColor(Color.parseColor("#9f90af"));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // default selection on the NTB, once application starts
        navigationTabBar.setModelIndex(0, true);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

//        // Necessary to restore the BottomBar's state, otherwise we would
//        // lose the current tab on orientation change.
//        mBottomBar.onSaveInstanceState(outState);
    }

    public FloatingActionButton getFloatingActionButton() {
        return fab;
    }
    public AppBarLayout getAppBarLayout() {
        return abl;
    }
    public NestedScrollView getNestedScrollView() {return nsv;}
    public FrameLayout getFrameLayout() {return fl;}

    public void navigationTabBarSetting() {
        final String[] tabBarItemName = getResources().getStringArray(R.array.tab_bar_array);
        final String[] colors = getResources().getStringArray(R.array.default_preview);

//        final NavigationTabBar navigationTabBar = (NavigationTabBar) findViewById(R.id.bottomBar);
        navigationTabBar = (NavigationTabBar) findViewById(R.id.bottomBar);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_menu_1),
                        Color.parseColor(colors[0]))
                        .title(tabBarItemName[0])
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_menu_2),
                        Color.parseColor(colors[1]))
                        .title(tabBarItemName[1])
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_menu_3),
                        Color.parseColor(colors[2]))
                        .title(tabBarItemName[2])
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_menu_4),
                        Color.parseColor(colors[3]))
                        .title(tabBarItemName[3])
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_menu_5),
                        Color.parseColor(colors[4]))
                        .title(tabBarItemName[4])
                        .build()
        );
        navigationTabBar.setModels(models);
//        navigationTabBar.setViewPager(viewPager, 2);

        //IMPORTANT: ENABLE SCROLL BEHAVIOUR IN COORDINATOR LAYOUT
        navigationTabBar.setBehaviorEnabled(true);
        navigationTabBar.setIsTitled(true);

        navigationTabBar.setOnTabBarSelectedIndexListener(new NavigationTabBar.OnTabBarSelectedIndexListener() {
            @Override
            public void onStartTabSelected(final NavigationTabBar.Model model, final int index) {
                FrameLayout fl = (FrameLayout) findViewById(R.id.content_frame_map);
                fl.removeAllViews();
                presenter.checkMenuTabItemSelection(model);
            }

            @Override
            public void onEndTabSelected(final NavigationTabBar.Model model, final int index) {
                model.hideBadge();
            }
        });
        navigationTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {

            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }
        });
    }

    @Override
    public void onBackPressed() {
//        FilterResultFragment fragment = (FilterResultFragment) getFragmentManager().findFragmentByTag("filterFromMap");
//        if (fragment != null) {
//            return;
//        }
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        }
    }

//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }
//
//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//
//        int id = item.getItemId();
//
//        if (id == R.id.nav_map) {
//            // check if network is available
//            if (isNetworkAvailable(this) == false) {
//                // if no network is detected
//                dismissProgressDialog();
//                showAlertBox("Alert", "There is no internet connection detected. Map access is disabled.", this);
//            } else {
//                // map is selected in navigation bar
//                fm.beginTransaction().replace(R.id.content_frame, new GoogleMapFragment()).commit();
//            }
//
//        } else if (id == R.id.nav_crePer) {
//            // check if network is available
//            if (isNetworkAvailable(this) == false) {
//                // if no network is detected
//                dismissProgressDialog();
//                showAlertBox("Alert", "There is no internet connection detected. Create performance is disabled.", this);
//            } else {
//                // create performance is selected in navigation bar
//                Toast.makeText(this, "Create Performance", Toast.LENGTH_SHORT).show();
//                fm.beginTransaction().replace(R.id.content_frame, new CreatePerformanceFragment()).commit();
//            }
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }
}
