package com.example.jmueller.travellingturtle;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

public class TripActivity extends ActionBarActivity {

    String tripName;
    String id;
    Intent i;
    TextView ratingView;
    TextView blogView;
    TextView locationView;

    public void gotoGeo(View view) {
        //start the page
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        Intent intent = getIntent();
        tripName = intent.getStringExtra("tripName");
        id = intent.getStringExtra("id");

        ratingView = (TextView) findViewById(R.id.ratingView);
        blogView = (TextView) findViewById(R.id.blogView);
        locationView = (TextView) findViewById(R.id.locationView);

        TextView tripNameView = (TextView) findViewById(R.id.tripName);
        tripNameView.setText(tripName);

        //Get intent for TripsList so we can change activity pages
        i = new Intent(getApplicationContext(), AddGeoLocationActivity.class);
        //Passing data through with the intent to the page
        i.putExtra("tripName", tripName);
        i.putExtra("id", id);
    }

    @Override
    protected void onResume() {
        super.onResume();

        String url = "http://54.200.119.101/appTrips/";
        url = url + id;

        DownloadTask tripInfoTask = new DownloadTask();
        tripInfoTask.execute(url);

    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        //get the data from the url the request
        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);

                urlConnection = (HttpURLConnection) url.openConnection();
                int status = urlConnection.getResponseCode();
                InputStream in;
                if(status >= HttpStatus.SC_BAD_REQUEST) {
                    in = urlConnection.getErrorStream();
                } else {
                    in = urlConnection.getInputStream();
                }

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

            try {
                JSONObject jsonObject = new JSONObject(result);
                if(jsonObject.has("error")) {
                    Toast.makeText(TripActivity.this, "Sorry something went wrong", Toast.LENGTH_LONG).show();
                } else {
                    String rating = "";
                    String blog = "";
                    String locations = "";

                    Log.i("json", jsonObject.toString());

                    if(jsonObject != null) {
                        if(jsonObject.has("rating")){
                            rating = jsonObject.getString("rating");
                            Log.i("rating", rating);
                        }

                        if(jsonObject.has("blog")){
                            blog = jsonObject.getString("blog");
                            Log.i("blog", blog);
                        }

                        if(jsonObject.has("locations")) {
                            locations = jsonObject.getString("locations");
                            Log.i("locations", locations);
                        }
                    }

                    ratingView.setText(rating);
                    blogView.setText(blog);
                    locationView.setText(locations);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}
