package com.example.jmueller.travellingturtle;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AddGeoLocationActivity extends ActionBarActivity implements LocationListener {

    String tripName;
    String id;
    LocationManager locationManager;
    String provider;
    TextView latView;
    TextView longView;

    public void addGeolocation(View view) {

        Log.i("Trip Name", tripName);
        Log.i("ID", id);

        Location location = locationManager.getLastKnownLocation(provider);
        Double lat = location.getLatitude();
        Double lng = location.getLongitude();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_geo_location);

        Intent intent = getIntent();
        tripName = intent.getStringExtra("tripName");
        id = intent.getStringExtra("id");

        latView = (TextView) findViewById(R.id.latView);
        longView = (TextView) findViewById(R.id.longView);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        provider = locationManager.getBestProvider(new Criteria(), false);

        /*
        //Code doesn't seem to work with my emulator, location is null, need to add loctaions with
        //telnet in terminal to get results
        Location location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            Log.i("Location Info", "Location acheived");
        } else {
            Log.i("Location Info", "No location");
        }
        */
    }

    //start getting the location when the user starts the app
    @Override
    protected void onResume() {
        super.onResume();

        locationManager.requestLocationUpdates(provider, 400, 1, this);

    }

    //Stop getting the location when the user pauses the app
    @Override
    protected void onPause() {
        super.onPause();

        locationManager.removeUpdates(this);

    }

    @Override
    public void onLocationChanged(Location location) {

        Double lat = location.getLatitude();
        Double lng = location.getLongitude();

        latView.setText(lat.toString());
        longView.setText(lng.toString());
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
