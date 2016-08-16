package ambiesoft.start.fragment;


import android.Manifest;
import android.app.AlertDialog;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
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

    final static String DB_URL = "https://start-c9adf.firebaseio.com/performance";
    private Firebase firebase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);

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
        artworks = new ArrayList<>();
        new SetupArtworkMarker().execute();
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        LatLng defaultMarker = new LatLng(-37.813243, 144.962762);

        //default zoom in
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultMarker, 16));
        mMap.addMarker(new MarkerOptions().title("Default marker in CBD").position(defaultMarker));
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }

        //Get firebase instance
        Firebase.setAndroidContext(getContext());
        firebase = new Firebase(DB_URL);

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
