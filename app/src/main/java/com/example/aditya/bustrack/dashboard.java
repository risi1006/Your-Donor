package com.example.aditya.bustrack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class dashboard extends AppCompatActivity {

    private ImageView certi;
    private ImageView nearby;
    private ImageView reward;
    private ImageView profile;
    private ImageView developer;
    private ImageView bloodbank;
    private ImageView hospital;
    private ImageView lifeline;
    private ImageView userimage;

    int i=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);



        certi = (ImageView)findViewById(R.id.usrcerti);
        nearby = (ImageView)findViewById(R.id.usrblood);
        reward = (ImageView)findViewById(R.id.usrreward);
        profile = (ImageView)findViewById(R.id.usrprofile);
        developer = (ImageView)findViewById(R.id.developer);
        bloodbank = (ImageView)findViewById(R.id.usrbloodbank);
        hospital = (ImageView)findViewById(R.id.usrhospital);
        lifeline = (ImageView)findViewById(R.id.usrrelative);
        userimage = (ImageView)findViewById(R.id.usrimg);


        String uId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        userimage.setOnClickListener(view -> {
//            Intent intent = new Intent(dashboard.this, usr_image.class);
//            startActivity(intent);
        });

        certi.setOnClickListener(view -> {
            Intent intent = new Intent(dashboard.this, DriverMapsActivity.class);
            startActivity(intent);
        });

        nearby.setOnClickListener(view -> {
            Intent intent = new Intent(dashboard.this, StudentMapsActivity.class);
            startActivity(intent);
        });

        reward.setOnClickListener(view -> {
            Intent intent = new Intent(dashboard.this, reward.class);
            startActivity(intent);
        });

        hospital.setOnClickListener(view -> {
            Toast.makeText(dashboard.this, "Under Development Phase", Toast.LENGTH_LONG).show();
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        String uId1 = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference.child("reward").child(uId1).child("hospital1").child("name").setValue("Opal Hospital");
        reference.child("reward").child(uId1).child("hospital1").child("mobile").setValue("01");

        reference.child("reward").child(uId1).child("hospital2").child("name").setValue("BHU");
        reference.child("reward").child(uId1).child("hospital2").child("mobile").setValue("02");

        bloodbank.setOnClickListener(view -> {
            Toast.makeText(dashboard.this, "Under Development Phase", Toast.LENGTH_LONG).show();
        });

        lifeline.setOnClickListener(view -> {
            Toast.makeText(dashboard.this, "Under Development Phase", Toast.LENGTH_LONG).show();
        });


        profile.setOnClickListener(view -> {

            Toast.makeText(dashboard.this, uId, Toast.LENGTH_SHORT).show();
            FirebaseDatabase.getInstance().getReference().child("Users").child("Donor").child(uId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChildren())
                    {
                        for(DataSnapshot cSnapshot:dataSnapshot.getChildren())
                        {
                             String name3 = dataSnapshot.getValue().toString();

                             String[] token = name3.split(",");
//                             String gh;
//                             String abcd = name3.substring(8,18);
//                             String efgh = name3.substring(25);
//                             if(efgh.contains(",")){
//                              gh = efgh.substring(0,efgh.indexOf(","));}
//                             else
//                                  gh = efgh;
                             Toast.makeText(dashboard.this, token[0].substring(1)+"\n"+token[1], Toast.LENGTH_SHORT).show();
//                            User user2 = cSnapshot.getValue(User.class);
//                            Toast.makeText(dashboard.this,user2.getName(),Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        });

        developer.setOnClickListener(view -> {
            Intent intent = new Intent(dashboard.this, developer.class);
            startActivity(intent);
        });


    }
}
