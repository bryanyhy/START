package ambiesoft.start.view.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gigamole.navigationtabbar.ntb.NavigationTabBar;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.util.ArrayList;

import ambiesoft.start.R;
import ambiesoft.start.presenter.activity.MainActivityPresenter;
import ambiesoft.start.presenter.fragment.HeatMapFragmentPresenter;
import ambiesoft.start.view.fragment.CreatePerformanceFragment;
import ambiesoft.start.view.fragment.FilterResultFragment;
import ambiesoft.start.view.fragment.HeatMapFragment;
import io.fabric.sdk.android.Fabric;

/**
 * Main activity for the tab bar and navigation bar, and also the content frame for showing all fragments
 */
public class MainActivity extends AppCompatActivity implements HeatMapFragmentPresenter.OnHeadlineSelectedListener, View.OnTouchListener {

    private static final int GALLERY_INTENT = 2;

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "0s4cAM3C26UFDVI47BWLuc2VZ";
    private static final String TWITTER_SECRET = "TDrjsuV65t9IyCzGEr3DgPW0RBJLXDmcFpeGlCtHKu9ZiUeMYn";

    private static final int NON_REG_USER = 0;
    private static final int REG_USER = 1;

    private MainActivityPresenter presenter;

    public ImageView backdrop;
    public FloatingActionButton fab;
    public AppBarLayout abl;
    private android.app.FragmentManager fm = getFragmentManager();
    private NavigationTabBar navigationTabBar;
    private NestedScrollView nsv;
    private FrameLayout fl;


    private String email;
    private int userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // for twitter
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig), new TweetComposer());

        setContentView(R.layout.activity_main);
//        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // disable the screen orientation sensor, so the whole activity will be in Portrait mode
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("email")) {
                email = extras.getString("email");
            }
            if (extras.containsKey("userType")) {
                userType = extras.getInt("userType");
            }
        }
        Toast.makeText(getApplicationContext(), "Welcome!", Toast.LENGTH_LONG).show();

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
        backdrop = (ImageView) findViewById(R.id.backdrop);
        fab.hide();


        abl = (AppBarLayout) findViewById(R.id.appbar);
        nsv = (NestedScrollView) findViewById(R.id.content_frame);
        // set on click listener for hiding soft keyboard
        nsv.setOnTouchListener(this);
        fl = (FrameLayout) findViewById(R.id.content_frame_map);
        // collapsing toolbar setting
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setExpandedTitleColor(Color.parseColor("#009F90AF"));
        collapsingToolbar.setCollapsedTitleTextColor(Color.parseColor("#9f90af"));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (userType == NON_REG_USER) {
            // default selection on the NTB, once application starts
            navigationTabBar.setModelIndex(0, true);
        } else if (userType == REG_USER) {
            // default selection on the NTB, once application starts
            navigationTabBar.setModelIndex(3, true);
        } else {
        }
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
    public ImageView getBackdrop (){
        return backdrop;
    }
    public NavigationTabBar getNavigationTabBar() {return navigationTabBar;}
    //TODO
    public String getUserEmail() {return email;}

    public void navigationTabBarSetting() {
        final String[] tabBarItemName = getResources().getStringArray(R.array.tab_bar_array);
        final String[] colors = getResources().getStringArray(R.array.default_preview);

//        final NavigationTabBar navigationTabBar = (NavigationTabBar) findViewById(R.id.bottomBar);
        navigationTabBar = (NavigationTabBar) findViewById(R.id.bottomBar);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        if (userType == NON_REG_USER) {

            models.add(
                    new NavigationTabBar.Model.Builder(
                            getResources().getDrawable(R.drawable.ic_menu_1),
                            Color.parseColor(colors[0]))
                            .title(tabBarItemName[0])
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
                            getResources().getDrawable(R.drawable.twitter),
                            Color.parseColor(colors[2]))
                            .title(tabBarItemName[5])
                            .build()
            );
            models.add(
                    new NavigationTabBar.Model.Builder(
                            getResources().getDrawable(R.drawable.ic_menu_5),
                            Color.parseColor(colors[4]))
                            .title(tabBarItemName[4])
                            .build()
            );
        } else {

            models.add(
                    new NavigationTabBar.Model.Builder(
                            getResources().getDrawable(R.drawable.ic_menu_1),
                            Color.parseColor(colors[0]))
                            .title(tabBarItemName[0])
                            .build()
            );
//            models.add(
//                    new NavigationTabBar.Model.Builder(
//                            getResources().getDrawable(R.drawable.ic_menu_2),
//                            Color.parseColor(colors[1]))
//                            .title(tabBarItemName[1])
//                            .build()
//            );
            models.add(
                    new NavigationTabBar.Model.Builder(
                            getResources().getDrawable(R.drawable.twitter),
                            Color.parseColor(colors[2]))
                            .title(tabBarItemName[5])
                            .build()
            );
            models.add(
                    new NavigationTabBar.Model.Builder(
                            getResources().getDrawable(R.drawable.ic_menu_3),
                            Color.parseColor(colors[1]))
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
        }
        navigationTabBar.setModels(models);

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

    public void onLocationSelected(String address, Double lat, Double lng) {
        CreatePerformanceFragment cpf = (CreatePerformanceFragment)
                getFragmentManager().findFragmentById(R.id.content_frame);

        if (cpf != null) {
            // If article frag is available, we're in two-pane layout...

            // Call a method in the ArticleFragment to update its content
            cpf.updateLocationInfo(address, lat, lng);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Fragment fragment = getFragmentManager().findFragmentByTag("TwitterFragment");
//        if (fragment != null) {
//            fragment.onActivityResult(requestCode, resultCode, data);
//        }
//        else Log.d("Twitter", "fragment is null");
        Fragment fragment = getFragmentManager().findFragmentByTag("PostTweetFragment");
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            fragment = getFragmentManager().findFragmentByTag("EditProfileFragment");
            if (fragment != null) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }


    }
}
