package com.group16.medassist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.List;

public class MapsActivity extends FragmentActivity implements LocationListener {


    GoogleMap googleMap;
    LocationManager mLocationManager;
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
        mLocationManager  = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location recentLoc = null;
        try{recentLoc =getLastKnownLocation();}
        catch(SecurityException e) { cantGetLastLocation(); return;}
        if(recentLoc == null) {cantGetLastLocation(); return;}
        double lat = recentLoc.getLatitude();
        double lon = recentLoc.getLongitude();
        String geoURI = String.format("geo:%f,%f?q=hospital", lat, lon);
        Uri geo = Uri.parse(geoURI);
        Intent geoMap = new Intent(Intent.ACTION_VIEW, geo);
        startActivity(geoMap);

    }
    private Location getLastKnownLocation() {
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l;
            try{l = mLocationManager.getLastKnownLocation(provider);}
            catch(SecurityException e) {continue;}
           // ALog.d("last known location, provider: %s, location: %s", provider,
                    //l);

            if (l == null) {
                continue;
            }
            if (bestLocation == null
                    || l.getAccuracy() < bestLocation.getAccuracy()) {
         //       ALog.d("found best last known location: %s", l);
                bestLocation = l;
            }
        }
        if (bestLocation == null) {
            return null;
        }
        return bestLocation;
    }
    public void cantGetLastLocation() {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("Can't show maps because can't get last location")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .create().show();
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

