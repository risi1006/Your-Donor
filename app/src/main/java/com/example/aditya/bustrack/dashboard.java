package com.example.aditya.bustrack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class dashboard extends AppCompatActivity {

    private ImageView certi;
    private ImageView nearby;
    private ImageView reward;
    private ImageView profile;
    private ImageView developer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);



        certi = (ImageView)findViewById(R.id.usrcerti);
        nearby = (ImageView)findViewById(R.id.usrblood);
        reward = (ImageView)findViewById(R.id.usrreward);
        profile = (ImageView)findViewById(R.id.usrprofile);
        developer = (ImageView)findViewById(R.id.developer);


        certi.setOnClickListener(view -> {
            Intent intent = new Intent(dashboard.this, DriverMapsActivity.class);
            startActivity(intent);
        });

        nearby.setOnClickListener(view -> {
            Intent intent = new Intent(dashboard.this, StudentMapsActivity.class);
            startActivity(intent);
        });

        reward.setOnClickListener(view -> {
            Intent intent = new Intent(dashboard.this, StudentMapsActivity.class);
            startActivity(intent);
        });
        profile.setOnClickListener(view -> {
            FirebaseDatabase.getInstance().getReference().child("Users").child("Donor").child("Detail").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChildren())
                    {
                        for(DataSnapshot cSnapshot:dataSnapshot.getChildren())
                        {
                            User user2 = cSnapshot.getValue(User.class);
                            Toast.makeText(dashboard.this,user2.getName(),Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        });

        developer.setOnClickListener(view -> {
            Intent intent = new Intent(dashboard.this, StudentMapsActivity.class);
            startActivity(intent);
        });


    }
}
