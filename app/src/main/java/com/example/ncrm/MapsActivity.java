package com.example.ncrm;

import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by shameer on 2018-02-16.
 */

public class MapsActivity extends MainActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private boolean mMapReady = false;
    private Button mBtnNormal;
    private Button mBtnSatellite;
    private Button mBtnHybrid;
    private Button mBtnSeattle;
    private Button mBtnTokyo;
    private Button mBtnDublin;
    private Button mBtnStreetView;

    @Override
    public void onMapReady(GoogleMap map) {
        mMapReady = true;
        mMap = map;
        createMap(40.7484, -73.9857, "New York City");
    }


    private void createMap(double latitude, double longitude, String title) {
        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        mMap.addMarker(markerOptions.position(latLng).title(title));
        CameraPosition target = CameraPosition.builder().target(latLng).zoom(14).bearing(0).tilt(45).build();
        // Move camera without animation
         mMap.moveCamera(CameraUpdateFactory.newCameraPosition(target));
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_maps, frameLayout);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
}