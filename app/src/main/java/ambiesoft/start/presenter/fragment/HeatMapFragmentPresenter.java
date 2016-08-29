package ambiesoft.start.presenter.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

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
public class HeatMapFragmentPresenter implements OnMapReadyCallback {

    private HeatMapFragment view;
    private GoogleMap mMap;

    public HeatMapFragmentPresenter(HeatMapFragment view) {
        this.view = view;
        loadGoogleMapFragment();
    }

    public void loadGoogleMapFragment() {
        // Load the Google map fragment
        MapFragment mapFragment = (MapFragment) view.getChildFragmentManager().findFragmentById(R.id.heatmap);
        mapFragment.getMapAsync(this);
        if (mapFragment == null) {
            // if the map can't be loaded
            dismissProgressDialog();
            Toast.makeText(view.getActivity(), "Map can't be loaded.", Toast.LENGTH_SHORT).show();
        } else if (isNetworkAvailable(view.getContext()) == false) {
            // if no network is detected
            dismissProgressDialog();
            showAlertBox("Alert", "There is no internet connection detected. Please check your internet connection " +
                    "in order to experience full functions.", view.getActivity());
        }
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
        // enable the current location button if there is permission to access user's location
        if (ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
    }
}
