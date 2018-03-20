package com.example.ncrm;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

/**
 * Created by shameer on 2018-02-16.
 */

public class MapsActivity extends MainActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String mStreetAddress;
    private String mCity;
    private String mCountry;

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        String fullAddress = mStreetAddress + ", " + mCity + ", " + mCountry;
        LatLng addressLatLng = convertAddressToLatLng(this, fullAddress);
        if (addressLatLng != null) {
            createMap(addressLatLng, mStreetAddress);
        }
        else {
            Utility.showMessageDialog(MapsActivity.this, getString(R.string.no_lat_lng_message));
        }
    }

    private void createMap(LatLng latLng, String title) {
        MarkerOptions markerOptions = new MarkerOptions();
        mMap.addMarker(markerOptions.position(latLng).title(title));
        CameraPosition target = CameraPosition.builder().target(latLng).zoom(17).bearing(0).tilt(45).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(target));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_maps, frameLayout);

        Intent intent = getIntent();
        mStreetAddress = intent.getStringExtra("streetAddress");
        mCity = intent.getStringExtra("city");
        mCountry = intent.getStringExtra("country");

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private LatLng convertAddressToLatLng(Context context, String address) {
        Geocoder geocoder = new Geocoder(context);
        List<Address> addressList;
        LatLng latLng = null;

        try {
            addressList = geocoder.getFromLocationName(address, 10);
            if (addressList.size() > 0) {
                Address location = addressList.get(0);
                latLng = new LatLng(location.getLatitude(), location.getLongitude());
            } else {
                return null;
            }
            if (addressList == null) {
                return null;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return latLng;
    }
}