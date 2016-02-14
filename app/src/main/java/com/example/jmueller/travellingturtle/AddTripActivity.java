package com.example.jmueller.travellingturtle;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;

public class AddTripActivity extends ActionBarActivity {

    String id;
    String username;
    EditText tripNameEditText ;
    String tripName;
    EditText ratingEditText;
    String rating;
    EditText blogEditText;
    String blog;

    public void addTrip(View view) {
        Log.i("id", id);
        Log.i("username", username);

        tripName = tripNameEditText.getText().toString().trim();
        rating = ratingEditText.getText().toString().trim();
        blog = blogEditText.getText().toString().trim();

        String url = "http://54.200.119.101/appTrips/?createdBy=" + id;

        //http://stackoverflow.com/questions/6290531/check-if-edittext-is-empty
        if(tripName.matches("")) {
            Toast.makeText(AddTripActivity.this, "Trip Name is required", Toast.LENGTH_LONG).show();
            return;
        } else {
            try {
                url = url + "name=" + URLEncoder.encode(tripName, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.i("Trip Name Url", url);
        }
        if(rating.matches("")) {
            Log.i("Rating", "no value");
        } else {
            Log.i("rating", rating);
        }
        if(blog.matches("")) {
            Log.i("blog", "no value");
        } else {
            Log.i("blog", blog);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        tripNameEditText = (EditText) findViewById(R.id.tripNameEditText);
        ratingEditText = (EditText) findViewById(R.id.ratingEditText);
        blogEditText = (EditText) findViewById(R.id.blogEditText);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        id = intent.getStringExtra("id");
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        //get the data from the url the request
        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            InputStream in = null;

                /*
            try {
                //reference following link for example code on HttpClient
                //http://stackoverflow.com/questions/25782582/send-put-request-in-android-to-rest-api
                HttpClient httpClient = new DefaultHttpClient();
                //HttpPut httpPut = new HttpPut(urls[0]);

                //httpPut.addHeader("Accept", "application/json");
                //httpPut.addHeader("Content-type", "application/json");

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
                */

            return null;
        }

        //This gets passed the result from doInBackground method
        //The do in background method cannot interact with the UI so we do it here
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            /*
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
            */
        }
    }
}
