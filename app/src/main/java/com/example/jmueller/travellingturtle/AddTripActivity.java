package com.example.jmueller.travellingturtle;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    public void addTripButton(View view) {

        tripName = tripNameEditText.getText().toString().trim();
        rating = ratingEditText.getText().toString().trim();
        blog = blogEditText.getText().toString().trim();

        String url = "http://54.200.119.101/appTrips?createdBy=" + id;

        //references for checking edit text is empty and urlencoding of parameters
        //http://stackoverflow.com/questions/6290531/check-if-edittext-is-empty
        //http://stackoverflow.com/questions/10786042/java-url-encoding-of-query-string-parameters
        if(tripName.matches("")) {
            Toast.makeText(AddTripActivity.this, "Trip Name is required", Toast.LENGTH_LONG).show();
            return;
        } else {
            try {
                url = url + "&name=" + URLEncoder.encode(tripName, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        if(rating.matches("")) {
            Log.i("Rating", "no value");
        } else {
            try {
                url = url + "&rating=" + URLEncoder.encode(rating, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        if(blog.matches("")) {
            Log.i("blog", "no value");
        } else {
            try {
                url = url + "&blog=" + URLEncoder.encode(blog, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        DownloadTask addTripTask = new DownloadTask();
        addTripTask.execute(url);

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

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(urls[0]);

            try {
                HttpResponse httpResponse = httpClient.execute(httpPost);

                in = httpResponse.getEntity().getContent();

                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while(data != -1) {
                    char current = (char) data;

                    result += current;

                    data = reader.read();
                }

                return result;
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
                    Toast.makeText(AddTripActivity.this, "Sorry an error occurred", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(AddTripActivity.this, "Trip updated successfully", Toast.LENGTH_LONG).show();
                    tripNameEditText.setText("");
                    ratingEditText.setText("");
                    blogEditText.setText("");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
