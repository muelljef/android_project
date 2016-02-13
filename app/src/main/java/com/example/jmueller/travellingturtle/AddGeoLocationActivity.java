package com.example.jmueller.travellingturtle;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
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

public class AddGeoLocationActivity extends ActionBarActivity {

    String tripName;
    String id;

    public void addGeolocation() {

        Log.i("Trip Name", tripName);
        Log.i("ID", id);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_geo_location);

        Intent intent = getIntent();
        tripName = intent.getStringExtra("tripName");
        id = intent.getStringExtra("id");

    }

}
