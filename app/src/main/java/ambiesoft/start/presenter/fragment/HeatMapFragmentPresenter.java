package ambiesoft.start.presenter.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import ambiesoft.start.R;
import ambiesoft.start.model.dataclass.Artwork;
import ambiesoft.start.model.dataclass.PedCount;
import ambiesoft.start.model.dataclass.PedSensor;
import ambiesoft.start.view.fragment.GoogleMapFragment;
import ambiesoft.start.view.fragment.HeatMapFragment;

import static ambiesoft.start.model.utility.AlertBox.showAlertBox;
import static ambiesoft.start.model.utility.Firebase.setupFirebase;
import static ambiesoft.start.model.utility.JSON.loadArtworkJSONFromAsset;
import static ambiesoft.start.model.utility.JSON.loadPedCountJSONFromAsset;
import static ambiesoft.start.model.utility.JSON.loadSensorLocationJSONFromAsset;
import static ambiesoft.start.model.utility.NetworkAvailability.isNetworkAvailable;
import static ambiesoft.start.model.utility.ProgressLoadingDialog.dismissProgressDialog;
import static ambiesoft.start.model.utility.ProgressLoadingDialog.showProgressDialog;

/**
 * Created by Bryanyhy on 28/8/2016.
 */
public class HeatMapFragmentPresenter implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private HeatMapFragment view;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Double selectedLat;
    private Double selectedLng;
    private Marker locationMarker;
    private String locationAddress;
    private Geocoder geocoder;
    private static ArrayList<PedSensor> pedSensors = new ArrayList<>();
    private static ArrayList<PedCount> pedCounts = new ArrayList<>();
    private HeatmapTileProvider mProvider;
    private TileOverlay mOverlay;
    OnHeadlineSelectedListener mCallback;


    // Container Activity must implement this interface
    public interface OnHeadlineSelectedListener {
        public void onLocationSelected(String address, Double lat, Double lng);
    }

    public HeatMapFragmentPresenter(HeatMapFragment view) {
        this.view = view;
        connectGoogleApiClient();
        geocoder = new Geocoder(view.getContext(), Locale.getDefault());
        loadGoogleMapFragment();
        try {
            mCallback = (OnHeadlineSelectedListener) view.getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(view.getActivity().toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    public void connectGoogleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(view.getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();
    }

    public void loadGoogleMapFragment() {
        // Load the Google map fragment
        MapFragment mapFragment = (MapFragment) view.getChildFragmentManager().findFragmentById(R.id.heatmap);
        mapFragment.getMapAsync(this);
        if (mapFragment == null) {
            Toast.makeText(view.getActivity(), "Map can't be loaded.", Toast.LENGTH_SHORT).show();
        } else if (isNetworkAvailable(view.getContext()) == false) {
            showAlertBox("Alert", "There is no internet connection detected. Please check your internet connection " +
                    "in order to experience full functions.", view.getActivity());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        // enable the current location button if there is permission to access user's location
        if (ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
        // default center location
        selectedLat = -37.813243;
        selectedLng = 144.962762;
        LatLng defaultCenter = new LatLng(selectedLat, selectedLng);
        setMarkerLocation(defaultCenter);
        setMarkerLocationAddress();
        setMarkerDragListener();
        new SetupSensorLocation().execute();
    }

    public void setMarkerLocation(LatLng location) {
        locationMarker = mMap.addMarker(new MarkerOptions()
                .position(location)
                .title("Drag it to your performance location")
                .draggable(true));
        //default center and zoom in
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
    }

    public void setMarkerDragListener() {
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

            @Override
            public void onMarkerDragStart(Marker marker) {
                Toast.makeText(view.getActivity(), "Dragging Start", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                LatLng position = marker.getPosition();
                selectedLat = position.latitude;
                selectedLng = position.longitude;
                setMarkerLocationAddress();

            }

            @Override
            public void onMarkerDrag(Marker marker) {
                System.out.println("Dragging");
            }
        });
    }

    public void setMarkerLocationAddress() {
        try {
            locationAddress = geocoder.getFromLocation(selectedLat, selectedLng, 1).get(0).getAddressLine(0) + ", "
                    + geocoder.getFromLocation(selectedLat, selectedLng, 1).get(0).getLocality() + " "
                    + geocoder.getFromLocation(selectedLat, selectedLng, 1).get(0).getAdminArea() + " "
                    + geocoder.getFromLocation(selectedLat, selectedLng, 1).get(0).getPostalCode() + ", "
                    + geocoder.getFromLocation(selectedLat, selectedLng, 1).get(0).getCountryName();
            Toast.makeText(view.getActivity(), locationAddress, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMarkerOnSearchedPlace(LatLng location, String address) {
        mMap.clear();
        setMarkerLocation(location);
        locationAddress = address;
        addHeatMap();
        Toast.makeText(view.getActivity(), locationAddress, Toast.LENGTH_LONG).show();
    }

    public void confirmLocation() {
        mCallback.onLocationSelected(locationAddress, selectedLat, selectedLng);
        view.getFragmentManager().popBackStack();
    }

    // class used for setup the Pedestrian Sensor location in City of Melbourne
    public class SetupSensorLocation extends AsyncTask<Void, Void, Void> {

        // Achieve the JSON file of sensor location, and get necessary attributes from it
        // Pass those values to a new Sensor object in an Sensor ArrayList
        protected Void doInBackground(Void... voids) {
            try {
                // create a new JSONObject, by getting the sensor dataset in String format through a method in JSON Class
                JSONObject jsonObj = new JSONObject(loadSensorLocationJSONFromAsset(view.getActivity()));
                JSONArray data = jsonObj.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    JSONArray specificSensor = data.getJSONArray(i);
                    String name = specificSensor.getString(10);
                    Double lat = specificSensor.getDouble(16);
                    Double lng = specificSensor.getDouble(17);
                    PedSensor sensor = new PedSensor(name, lat, lng);
                    pedSensors.add(sensor);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            Log.i("System.out","Size is " + pedSensors.size());
            new SetupPedCount().execute();
        }
    }

    // class used for setup the Pedestrian Count in City of Melbourne
    public class SetupPedCount extends AsyncTask<Void, Void, Void> {
        // Achieve the JSON file of ped count, and get necessary attributes from it
        // Pass those values to a new PedCount object in an PedCount ArrayList
        protected Void doInBackground(Void... voids) {
            try {
                // create a new JSONObject, by getting the PedCount dataset in String format through a method in JSON Class
                JSONArray data = new JSONArray(loadPedCountJSONFromAsset(view.getActivity()));
                for (int i = 0; i < data.length(); i++) {
                    JSONArray specificSensor = data.getJSONArray(i);
                    String name = specificSensor.getString(0);
                    ArrayList<Double> countInPlace = new ArrayList<>();
                    for (int j = 1; j < 25; j++) {
                        String countInString = specificSensor.getString(j);
                        Double countInEveryHour;
                        if (!countInString.equals("N/A")) {
                            countInEveryHour = specificSensor.getDouble(j);
                        } else {
                            countInEveryHour = 0.0;
                        }
                        countInPlace.add(countInEveryHour);
                    }
                    PedCount allCountPerPlace = new PedCount(name, countInPlace);
                    pedCounts.add(allCountPerPlace);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            Log.i("System.out","Size is " + pedCounts.size());
            addHeatMap();

        }
    }

    private void addHeatMap() {
        ArrayList<WeightedLatLng> dataList = new ArrayList<WeightedLatLng>();
        for (PedSensor sensor: pedSensors) {
            for (PedCount count: pedCounts) {
                if (sensor.getName().equals(count.getName())) {
                    WeightedLatLng data = new WeightedLatLng(new LatLng(sensor.getLat(), sensor.getLng()), count.getCount().get(17) );
                    dataList.add(data);
                }
            }
        }
        // Create a heat map tile provider
        mProvider = new HeatmapTileProvider.Builder()
                .weightedData(dataList)
                .radius(50)
                .build();
        // Add a tile overlay to the map, using the heat map tile provider.
        mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
        dismissProgressDialog();
    }

//    // draw the artworks marker on map
//    public void drawSensorsMarker() {
//        // ensure there is artwork result
//        if (pedSensors.size() != 0) {
//            for (PedSensor sensor: pedSensors) {
//                LatLng sensorMarker = new LatLng(sensor.getLat(), sensor.getLng());
//                mMap.addMarker(new MarkerOptions().title(sensor.getName()).position(sensorMarker)
//                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
//            }
//        }
//    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}