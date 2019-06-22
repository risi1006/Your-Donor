package com.example.risi.your_donor;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class developer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer);

//*****************************************************Adding timer for Donor activity***************************************************************
        Handler mHandler = new Handler();
        mHandler.postDelayed(() -> {
            Intent intent = new Intent(developer.this, DonorMapsActivity.class);
            startActivity(intent);
            finish();
        }, 60000L);



//*****************************************************This is the details of the card**********************************************************************************

    }
}
