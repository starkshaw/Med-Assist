package com.group16.medassist;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

public class MapsActivity extends FragmentActivity implements LocationListener {


    GoogleMap googleMap;
    //LatLng myPosition;

    // add all necessary things

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // if  Google Play Services are available then

        // Getting reference to the SupportMapFragment of activity_main.xml
        SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        // Getting GoogleMap object from the fragment
        googleMap = fm.getMap();

        // Enabling MyLocation Layer of Google Map, showing your location (blue dot)
        googleMap.setMyLocationEnabled(true);

        //This gets last known location, and searches maps for 'hospital', showing nearby results
        LocationManager locMgr  = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            Location recentLoc = locMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            double lat = recentLoc.getLatitude();
            double lon = recentLoc.getLongitude();
            String geoURI = String.format("geo:%f,%f?q=hospital", lat, lon);
            Uri geo = Uri.parse(geoURI);
            Intent geoMap = new Intent(Intent.ACTION_VIEW, geo);
            startActivity(geoMap);
        } catch(SecurityException e) {e.printStackTrace();finish();}

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}

