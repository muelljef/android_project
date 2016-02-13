package com.example.jmueller.travellingturtle;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.TextView;

public class TripActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        Intent intent = getIntent();

        TextView tripName = (TextView) findViewById(R.id.tripName);
        tripName.setText(intent.getStringExtra("tripName"));

        TextView idView = (TextView) findViewById(R.id.idView);
        idView.setText(intent.getStringExtra("id"));
    }
}
