package com.example.jmueller.travellingturtle;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

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

        http://stackoverflow.com/questions/6290531/check-if-edittext-is-empty
        if(tripName.matches("")) {
            Log.i("Trip Name", "no value");
        } else {
            Log.i("Trip Name", tripName);
        }

        //Log.i("blog", blog);
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
}
