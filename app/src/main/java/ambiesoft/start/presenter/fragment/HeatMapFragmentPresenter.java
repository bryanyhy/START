package ambiesoft.start.presenter.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Locale;

import ambiesoft.start.R;
import ambiesoft.start.view.fragment.GoogleMapFragment;
import ambiesoft.start.view.fragment.HeatMapFragment;

import static ambiesoft.start.model.utility.AlertBox.showAlertBox;
import static ambiesoft.start.model.utility.Firebase.setupFirebase;
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
        Toast.makeText(view.getActivity(), locationAddress, Toast.LENGTH_LONG).show();
    }

    public void confirmLocation() {
        mCallback.onLocationSelected(locationAddress, selectedLat, selectedLng);
        view.getFragmentManager().popBackStack();
    }



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
