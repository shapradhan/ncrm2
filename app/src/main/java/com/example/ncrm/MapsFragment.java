package com.example.ncrm;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by shameer on 2018-02-16.
 */

public class MapsFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private boolean mMapReady = false;
    private MapView mMapView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, null, false);

        mMapView = (MapView) view.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        // Set the map ready callback to receive the GoogleMap object
        mMapView.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMapReady=true;
        mMap = googleMap;
        createMap(40.7484, -73.9857, "New York City");

    }

    private void createMap(double latitude, double longitude, String title) {
        Toast.makeText(getContext(), "tHere", Toast.LENGTH_SHORT).show();
        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        mMap.addMarker(markerOptions.position(latLng).title(title));
        CameraPosition target = CameraPosition.builder().target(latLng).zoom(14).bearing(0).tilt(45).build();
        // Move camera without animation
        // mMap.moveCamera(CameraUpdateFactory.newCameraPosition(target));

        // Move camera with animation
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(target), 5000, null);  // i is the duration of animation
        Toast.makeText(getContext(), "Here", Toast.LENGTH_SHORT).show();
    }
}
