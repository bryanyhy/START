package ambiesoft.start.fragment;


import android.Manifest;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.cast.framework.media.uicontroller.UIController;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import ambiesoft.start.R;
import ambiesoft.start.dataclass.Artwork;
import ambiesoft.start.dataclass.Performance;

import static ambiesoft.start.utility.AlertBox.showAlertBox;
import static ambiesoft.start.utility.BundleItemChecker.getFilterCategoryFromBundle;
import static ambiesoft.start.utility.BundleItemChecker.getFilterDateFromBundle;
import static ambiesoft.start.utility.BundleItemChecker.getFilterKeywordFromBundle;
import static ambiesoft.start.utility.BundleItemChecker.getFilterTimeFromBundle;
import static ambiesoft.start.utility.DateFormatter.getTodayDate;
import static ambiesoft.start.utility.FilterResult.advancedFilteringOnPerformanceList;
import static ambiesoft.start.utility.Firebase.getPerformanceListFromFirebaseByDate;
import static ambiesoft.start.utility.Firebase.setupFirebase;
import static ambiesoft.start.utility.JSON.loadJSONFromAsset;
import static ambiesoft.start.utility.NetworkAvailability.isNetworkAvailable;
import static ambiesoft.start.utility.ProgressLoadingDialog.dismissProgressDialog;
import static ambiesoft.start.utility.ProgressLoadingDialog.showProgressDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class GoogleMapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private static final int GOOGLE_MAP_FRAGMENT_ID = 1;
    private static final String DB_URL = "https://start-c9adf.firebaseio.com/performance";

    private GoogleMap mMap;
    private static ArrayList<Artwork> artworks = new ArrayList<>();
    private static boolean showArtworks = false;
    private ArrayList<Performance> performances;
    private static ArrayList<Performance> filteredPerformances;

    private static String selectedDate;
    private String filterKeyword;
    private String filterCategory;
    private String filterTime;

    private Firebase firebase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);
        // display menu of the top action bar
        setHasOptionsMenu(true);

        // setting up the floating action button, to access from map to home fragment
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment homeFragment = new HomeFragment();
                Bundle bundle = new Bundle();
                bundle.putString("dateFromPreviousFragment", selectedDate);
//                bundle.putParcelableArrayList("performancesFromPreviousFragment", filteredPerformances);
                homeFragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.content_frame, homeFragment).commit();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showProgressDialog(getContext());
        Bundle bundle = getArguments();
        if (bundle != null) {
            selectedDate = getFilterDateFromBundle(bundle);
            filterKeyword = getFilterKeywordFromBundle(bundle);
            filterCategory = getFilterCategoryFromBundle(bundle);
            filterTime = getFilterTimeFromBundle(bundle);
        } else {
            // Always runs when the application start and set the filter date to today by default
            selectedDate = getTodayDate();
        }
        setupFirebase(getContext());
        MapFragment mapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (mapFragment == null) {
            Toast.makeText(getActivity(), "Map can't be loaded.", Toast.LENGTH_SHORT).show();
        } else if (isNetworkAvailable(getContext()) == false) {
            Toast.makeText(getActivity(), "Network is not available.", Toast.LENGTH_SHORT).show();
        }
    }

    // setup the menu items on the top action bar
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.map_fragment_menu, menu);
        MenuItem item = menu.findItem(R.id.action_settings);
        // check if the show artwork setting is turn on or off
        if (showArtworks == true) {
            // if show artwork is on
            item.setTitle("Hide artworks");
        } else {
            // if show artwork is off
            item.setTitle("Show artworks");
        }
    }

    // action after menu item on top action bar is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // if the item is clicked
        if (id == R.id.action_settings) {
            // turn on/off the display of artworks on map
            showArtworks = !showArtworks;
            // check if the show artwork setting is turn on or off
            if (showArtworks == true) {
                // if show artwork is on
                item.setTitle("Hide artworks");
                // clear the map, and show all artwork and performance as markers on map
                mMap.clear();
                if (artworks.size() == 0) {
                    new SetupArtworkMarker().execute();
                } else {
                    drawArtworksMarker();
                }
                drawPerformanceMarker();

            } else {
                // if show artwork is off
                item.setTitle("Show artworks");
                // clear the map, and show only performance as markers on map
                mMap.clear();
                drawPerformanceMarker();
            }
        }

        if (id == R.id.action_search) {
            Fragment filterResultFragment = new FilterResultFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("previousFragmentID", GOOGLE_MAP_FRAGMENT_ID);
            bundle.putString("filterDate", selectedDate);
            filterResultFragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.content_frame, filterResultFragment).commit();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        // default center location
        LatLng defaultCenter = new LatLng(-37.813243, 144.962762);
        //default center and zoom in
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultCenter, 15));
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
        mMap.setOnInfoWindowClickListener(this);
        setFireBaseListener();
//        // if the show artworks is switched on
//        if (showArtworks == true) {
//            if (artworks.size() == 0) {
//                // load artworks and setup markers on the map
//                new SetupArtworkMarker().execute();
//            } else {
//                drawArtworksMarker();
//            }
//        }
    }

    public void setFireBaseListener() {
        // Establish connection with Firebase URL
        firebase = new Firebase(DB_URL);
        // get data that match the specific date from Firebase
        Query queryRef = firebase.orderByChild("date").equalTo(selectedDate);
        //Retrieve latitude and longitude from each post on Firebase and add marker on map
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {
                Log.i("System.out","Firebase has update");
                // clear all markers on map if data changed in Firebase
                mMap.clear();
                // initialize performance ArrayList
                performances = new ArrayList<>();
                // get all performance detail and save them into Performance ArrayList as Performance Object
                performances = getPerformanceListFromFirebaseByDate(ds);
                if (performances.size() != 0) {
                    if (filterKeyword != null || filterCategory != null || filterTime != null) {
                        try {
                            filteredPerformances = advancedFilteringOnPerformanceList(performances, filterKeyword, filterCategory, filterTime);
                            if (filteredPerformances.size() != 0) {
                                drawPerformanceMarker();
                            } else {
                                showAlertBox("Sorry", "There is no matching result on " + selectedDate + ".", getActivity());
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else {
                        filteredPerformances = performances;
                        drawPerformanceMarker();
                    }
                } else {
                    showAlertBox("Sorry", "There is no matching result on " + selectedDate + ".", getActivity());
                }
                if (showArtworks == true) {
                    if (artworks.size() == 0) {
                        // load artworks and setup markers on the map
                        new SetupArtworkMarker().execute();
                    } else {
                        drawArtworksMarker();
                    }
                }
                dismissProgressDialog();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast toast = Toast.makeText(getContext(), firebaseError.toString(), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0 ,0);
                toast.show();
                dismissProgressDialog();
            }
        });
    }

    // method to draw the performance markers on map, based on the performance ArrayList
    public void drawPerformanceMarker() {
        if (filteredPerformances.size() != 0) {
            for (Performance performance: filteredPerformances) {
                LatLng googleMapLocations = new LatLng(performance.getLat(), performance.getLng());
                String cat = performance.getCategory();
                switch (cat){
                    case "Instruments":
                        mMap.addMarker(new MarkerOptions().title(performance.getName()).snippet(performance.getDate()).position(googleMapLocations)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                        break;
                    case "Singing":
                        mMap.addMarker(new MarkerOptions().title(performance.getName()).snippet(performance.getDate()).position(googleMapLocations)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                        break;
                    case "Reciting":
                        mMap.addMarker(new MarkerOptions().title(performance.getName()).snippet(performance.getDate()).position(googleMapLocations)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
                        break;
                    case "Conjuring":
                        mMap.addMarker(new MarkerOptions().title(performance.getName()).snippet(performance.getDate()).position(googleMapLocations)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                        break;
                    case "Juggling":
                        mMap.addMarker(new MarkerOptions().title(performance.getName()).snippet(performance.getDate()).position(googleMapLocations)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
                        break;
                    case "Puppetry":
                        mMap.addMarker(new MarkerOptions().title(performance.getName()).snippet(performance.getDate()).position(googleMapLocations)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                        break;
                    case "Miming":
                        mMap.addMarker(new MarkerOptions().title(performance.getName()).snippet(performance.getDate()).position(googleMapLocations)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                        break;
                    case "Dancing":
                        mMap.addMarker(new MarkerOptions().title(performance.getName()).snippet(performance.getDate()).position(googleMapLocations)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
                        break;
                    case "Drawing":
                        mMap.addMarker(new MarkerOptions().title(performance.getName()).snippet(performance.getDate()).position(googleMapLocations)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
                        break;
                    default:
                        mMap.addMarker(new MarkerOptions().title(performance.getName()).snippet(performance.getDate()).position(googleMapLocations)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                        break;
                }
            }
        }
    }

    @Override
    public void onInfoWindowClick(final Marker marker) {

        Log.i("System.out","Info box is clicked");
        double markerLat = marker.getPosition().latitude;
        double markerLng = marker.getPosition().longitude;
        for (Performance performance: filteredPerformances) {
            if (markerLat == performance.getLat() && markerLng == performance.getLng()) {
                Log.i("System.out","Found match");
                Fragment performanceDetailFragment = new PerformanceDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("performancesDetailFromPreviousFragment", performance);
                bundle.putString("dateFromFilter", selectedDate);
                bundle.putString("keywordFromFilter", filterKeyword);
                bundle.putString("categoryFromFilter", filterCategory);
                bundle.putString("timeFromFilter", filterTime);
                bundle.putInt("previousFragmentID", GOOGLE_MAP_FRAGMENT_ID);
                performanceDetailFragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.content_frame, performanceDetailFragment).commit();
                break;
            }
        }
    }

    // class used for setup the markers on artwork in City of Melbourne
    private class SetupArtworkMarker extends AsyncTask<Void, Void, Void> {

        // Achieve the JSON file on the artwork, and get necessary attributes from it
        // Pass those values to a new Artwork object in an Artwork ArrayList
        protected Void doInBackground(Void... voids) {
            try {
                // create a new JSONObject, by getting the artwork dataset in String format through a method in JSON Class
                JSONObject jsonObj = new JSONObject(loadJSONFromAsset(getActivity()));
                JSONArray data = jsonObj.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    JSONArray specificArtwork = data.getJSONArray(i);
                    String assetType = specificArtwork.getString(8);
                    String name = specificArtwork.getString(9);
                    String address = specificArtwork.getString(12);
                    String artist = specificArtwork.getString(13);
                    String artDate = specificArtwork.getString(15);
                    JSONArray latLng = specificArtwork.getJSONArray(19);
                    Double lat = latLng.getDouble(1);
                    Double lng = latLng.getDouble(2);
                    Artwork artwork = new Artwork(assetType, name, address, artist, artDate, lat, lng);
                    artworks.add(artwork);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        // After the background process is finished, we are going to add marker for every artwork onto the map,
        // by making use of their latitude and longitude
        protected void onPostExecute(Void aVoid) {
            Log.i("System.out","Size is " + artworks.size());
            // draw the artworks marker on map
            drawArtworksMarker();
        }
    }

    // draw the artworks marker on map
    private void drawArtworksMarker() {
        if (artworks.size() != 0) {
            for (Artwork artwork: artworks) {
                LatLng artworkMarker = new LatLng(artwork.getLat(), artwork.getLng());
                mMap.addMarker(new MarkerOptions().title(artwork.getName()).position(artworkMarker)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
            }
        }
    }
}
