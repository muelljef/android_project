package com.example.jmueller.travellingturtle;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginPageActivity extends ActionBarActivity {

    public void login(View view) {

        DownloadTask task = new DownloadTask();
        //task.execute("http://54.191.221.125/users");
        task.execute("http://54.200.119.101/appUsers");
        /*
        //Get intent for TripsList so we can change activity pages
        Intent intent = new Intent(getApplicationContext(), TripsListActivity.class);
        //Passing data through with the intent to the page
        intent.putExtra("username", "jeff@email.com");
        //start the page
        startActivity(intent);
        */
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

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

                String email = jsonObject.getString("email");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //String email = jsonObject.getString("email");

            if(result != null) {
                Log.i("Website Content", result);
            } else {
                Log.i("error", "call didn't work");
            }

        }
    }
}
