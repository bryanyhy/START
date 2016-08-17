package ambiesoft.start.fragment;


import android.Manifest;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
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
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import ambiesoft.start.R;
import ambiesoft.start.dataclass.Artwork;

import static ambiesoft.start.utility.JSON.loadJSONFromAsset;
import static ambiesoft.start.utility.NetworkAvailability.isNetworkAvailable;

/**
 * A simple {@link Fragment} subclass.
 */
public class GoogleMapFragment extends Fragment implements OnMapReadyCallback {

    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;
    private static final int MY_PERMISSIONS_REQUEST_COARSE_LOCATION = 2;
    private GoogleMap mMap;
    private ArrayList<Artwork> artworks;
    private static boolean showArtworks = true;

    private String filterDate;

    private final static String DB_URL = "https://start-c9adf.firebaseio.com/performance";
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
                getFragmentManager().beginTransaction().replace(R.id.content_frame, new HomeFragment()).commit();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey("date")) {
                filterDate = bundle.getString("date");
                Log.i("System.out","Bundle found + " + filterDate);
            }
        }

        artworks = new ArrayList<>();
        if (showArtworks == true) {
            new SetupArtworkMarker().execute();
        }
        MapFragment mapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (mapFragment == null) {
            Toast.makeText(getActivity(), "Map can't be loaded.", Toast.LENGTH_SHORT).show();
        } else if (isNetworkAvailable(getContext()) == false) {
            Toast.makeText(getActivity(), "Network is not available.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Map is loaded.", Toast.LENGTH_SHORT).show();
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
                new SetupArtworkMarker().execute();
                addPerformanceMarker();

            } else {
                // if show artwork is off
                item.setTitle("Show artworks");
                // clear the map, and show only performance as markers on map
                mMap.clear();
                addPerformanceMarker();
            }
        }

        if (id == R.id.action_search) {
            getFragmentManager().beginTransaction().replace(R.id.content_frame, new FilterResultFragment()).commit();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        LatLng defaultMarker = new LatLng(-37.813243, 144.962762);

        //default zoom in
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultMarker, 16));
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }

        addPerformanceMarker();
    }

    public void addPerformanceMarker() {
        //Get firebase instance
        Firebase.setAndroidContext(getContext());
        firebase = new Firebase(DB_URL);
        if (filterDate != null) {
            Query queryRef = firebase.orderByChild("date").equalTo(filterDate);
            //Retrieve latitude and longitude from each post on firebase and add marker on map
            queryRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot ds) {

                    for (DataSnapshot dataSnapshot : ds.getChildren()) {
                        Double latitude = Double.parseDouble(dataSnapshot.child("lat").getValue().toString());
                        Double longitude = Double.parseDouble(dataSnapshot.child("lng").getValue().toString());
                        LatLng googleMapLocations = new LatLng(latitude, longitude);
                        mMap.addMarker(new MarkerOptions().title(String.valueOf(latitude)).snippet(String.valueOf(longitude)).position(googleMapLocations));
                    }

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                    Toast toast = Toast.makeText(getContext(), firebaseError.toString(), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0 ,0);
                    toast.show();

                }
            });
        } else {
            //Retrieve latitude and longitude from each post on firebase and add marker on map
            firebase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot ds) {

                    for (DataSnapshot dataSnapshot : ds.getChildren()) {
                        Double latitude = Double.parseDouble(dataSnapshot.child("lat").getValue().toString());
                        Double longitude = Double.parseDouble(dataSnapshot.child("lng").getValue().toString());
                        LatLng googleMapLocations = new LatLng(latitude, longitude);
                        mMap.addMarker(new MarkerOptions().title(String.valueOf(latitude)).snippet(String.valueOf(longitude)).position(googleMapLocations));
                    }

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                    Toast toast = Toast.makeText(getContext(), firebaseError.toString(), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0 ,0);
                    toast.show();

                }
            });
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
            for (Artwork artwork: artworks) {
                LatLng artworkMarker = new LatLng(artwork.getLat(), artwork.getLng());
                mMap.addMarker(new MarkerOptions().title(artwork.getName()).position(artworkMarker)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            }
        }
    }
}
