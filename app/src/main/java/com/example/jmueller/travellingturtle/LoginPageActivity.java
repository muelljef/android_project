package com.example.jmueller.travellingturtle;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;

public class LoginPageActivity extends ActionBarActivity {

    public void login(View view) {

        //Get intent for TripsList so we can change activity pages
        Intent intent = new Intent(getApplicationContext(), TripsListActivity.class);
        //Passing data through with the intent to the page
        intent.putExtra("username", "jeff@email.com");
        //start the page
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
    }
}
