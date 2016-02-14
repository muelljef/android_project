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

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URI;

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
        if(location != null) {
            Double lat = location.getLatitude();
            Double lng = location.getLongitude();
            String addLocation = lat.toString() + "%20" + lng.toString();
            String urlString = "http://54.200.119.101/appTrips/" + id + "?addLocation=" + addLocation;
            DownloadTask addLocationTask = new DownloadTask();
            addLocationTask.execute(urlString);
        } else {

            Toast.makeText(AddGeoLocationActivity.this, "Error: no location details", Toast.LENGTH_LONG).show();
            return;
        }

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
        Location location = locationManager.getLastKnownLocation(provider);
        if(location != null) {
            Double lat = location.getLatitude();
            Double lng = location.getLongitude();

            latView.setText(lat.toString());
            longView.setText(lng.toString());
        }
        //location through locationManager.getLastKnownLocation(provider) is null at onCreate
        //so it is handled in update and addGeolocation
        //I have to "telnet localhost 5554" then "geo fix 10 10" to set the location
        //the getLastKnownLocation(provider) will work once the location is set through telnet

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


    public class DownloadTask extends AsyncTask<String, Void, String> {

        //get the data from the url the request
        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            InputStream in = null;
            URL url;

            //HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);
                Log.i("url", urls[0]);

                //reference following link for example code on HttpClient
                //http://stackoverflow.com/questions/25782582/send-put-request-in-android-to-rest-api
                HttpClient httpClient = new DefaultHttpClient();
                HttpPut httpPut = new HttpPut(urls[0]);

                httpPut.addHeader("Accept", "application/json");
                httpPut.addHeader("Content-type", "application/json");

                HttpResponse httpResponse = httpClient.execute(httpPut);

                in = httpResponse.getEntity().getContent();

                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while(data != -1) {
                    char current = (char) data;

                    result += current;

                    data = reader.read();
                }

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        //This gets passed the result from doInBackground method
        //The do in background method cannot interact with the UI so we do it here
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(result);
                if(jsonObject.has("error")) {
                    Toast.makeText(AddGeoLocationActivity.this, "Sorry an error occurred", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(AddGeoLocationActivity.this, "Trip updated successfully", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
