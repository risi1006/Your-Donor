package com.example.aditya.bustrack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class dashboard extends AppCompatActivity {

    private ImageView certi;
    private ImageView nearby;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);



        certi = (ImageView)findViewById(R.id.usrcerti);
        nearby = (ImageView)findViewById(R.id.usrblood);


        certi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(dashboard.this, DriverMapsActivity.class);
                startActivity(intent);
            }
        });

        nearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(dashboard.this, StudentMapsActivity.class);
                startActivity(intent);
            }
        });

    }
}
