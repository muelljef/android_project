package com.example.jmueller.travellingturtle;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class TripsListActivity extends ActionBarActivity {

    ListView tripsView;
    ArrayAdapter arrayAdapter;
    ArrayList<String> trips;
    ArrayList<String> ids;
    String username;
    String id;


    public void gotoAddTrip(View view) {
        //Get intent for TripsList so we can change activity pages
        Intent i = new Intent(getApplicationContext(), AddTripActivity.class);
        //Passing data through with the intent to the page
        i.putExtra("username", username);
        i.putExtra("id", id);
        //start the page
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips_list);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        id = intent.getStringExtra("id");

        TextView usernameView = (TextView) findViewById(R.id.username);
        usernameView.setText(username);

        tripsView = (ListView) findViewById(R.id.tripsView);

        trips = new ArrayList<String>();
        ids = new ArrayList<String>();

        arrayAdapter = new ArrayAdapter(TripsListActivity.this, android.R.layout.simple_list_item_1, trips);
        tripsView.setAdapter(arrayAdapter);
        tripsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(getApplicationContext(), TripActivity.class);
                i.putExtra("tripName", trips.get(position));
                i.putExtra("id", ids.get(position));
                startActivity(i);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        trips.clear();

        DownloadTask task = new DownloadTask();
        String url = "http://54.200.119.101/appTrips/?createdById=" + id;
        task.execute(url);
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
                if (status >= HttpStatus.SC_BAD_REQUEST) {
                    in = urlConnection.getErrorStream();
                } else {
                    in = urlConnection.getInputStream();
                }

                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while (data != -1) {
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
                JSONArray jsonTrips = new JSONArray(result);
                if (jsonTrips.isNull(0)) {
                    Toast.makeText(TripsListActivity.this, "Sorry there was an error", Toast.LENGTH_LONG).show();
                } else {

                    for (int i = 0; i < jsonTrips.length(); i++) {
                        JSONObject trip = jsonTrips.getJSONObject(i);
                        trips.add(trip.getString("name"));
                        ids.add(trip.getString("_id"));
                    }

                    Log.i("name array", trips.toString());
                    Log.i("id array", ids.toString());

                    arrayAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}
