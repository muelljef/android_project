package com.example.jmueller.travellingturtle;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

public class LoginPageActivity extends ActionBarActivity {

    public void login(View view) {

        EditText emailView = (EditText) findViewById(R.id.emailText);
        String email = emailView.getText().toString().trim();

        if (email.length() == 0) {
            Toast.makeText(LoginPageActivity.this, "You did not enter a username/email", Toast.LENGTH_LONG).show();
            return;
        } else {
            DownloadTask task = new DownloadTask();
            //task.execute("http://54.191.221.125/users");
            String url = "http://54.200.119.101/appUsers/";
            url += email;
            //task.execute("http://54.200.119.101/appUsers/jeff@email.com");
            task.execute(url);
        }
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
                if(jsonObject.has("error")) {
                    Toast.makeText(LoginPageActivity.this, "Sorry user not found", Toast.LENGTH_LONG).show();
                } else {
                    String email = jsonObject.getString("email");
                    String id = jsonObject.getString("_id");

                    Log.i("email", email);
                    Log.i("id", id);

                    //Get intent for TripsList so we can change activity pages
                    Intent intent = new Intent(getApplicationContext(), TripsListActivity.class);
                    //Passing data through with the intent to the page
                    intent.putExtra("username", email);
                    intent.putExtra("id", id);
                    //start the page
                    startActivity(intent);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
