package com.example.jmueller.travellingturtle;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class TripsListActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips_list);

        Intent intent = getIntent();

        TextView username = (TextView) findViewById(R.id.username);
        username.setText(intent.getStringExtra("username"));

        ListView tripsView = (ListView) findViewById(R.id.tripsView);

        final ArrayList<String> trips = new ArrayList<String>();
        trips.add("Hawaii");
        trips.add("New Zealand");
        trips.add("Fiji");
        trips.add("Europe");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, trips);
        tripsView.setAdapter(arrayAdapter);
        tripsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(getApplicationContext(), TripActivity.class);
                i.putExtra("tripName", trips.get(position));
                startActivity(i);

            }
        });
    }

}
