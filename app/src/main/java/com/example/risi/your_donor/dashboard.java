package com.example.risi.your_donor;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
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
//    private ImageView ritu;
    private TextView txt;
    String[] token;

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

        txt = (TextView)findViewById(R.id.txtdashboard);
//        ritu = (ImageView)findViewById(R.id.ritu);
//*****************************************************This is end details of the card**********************************************************************************

        //To get the ID of the current user
        String uId = FirebaseAuth.getInstance().getCurrentUser().getUid();


//*******************************************************For Name in Dashboard *********************************************************************************************************************************
        FirebaseDatabase.getInstance().getReference().child("Users").child("Donor").child(uId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren())
                {
                    for(DataSnapshot cSnapshot:dataSnapshot.getChildren())
                    {
                        String name3 = dataSnapshot.getValue().toString();

                        token = name3.split(",");
                        txt.setText("Welcome "+token[1].substring(6));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//******************************************************Ending of the name*******************************************************************************************

//        startService(new Intent(this,MyService.class));


//******************************************************Pressing the cards*******************************************************************************************
//        userimage.setOnClickListener(view -> {
////            Intent intent = new Intent(dashboard.this, usr_image.class);
////            startActivity(intent);
//        });

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
            Intent intent = new Intent(getApplicationContext(), StudentMapsActivity.class);
            Toast.makeText(dashboard.this, "Link yourself as Hospital \n Click Request Blood", Toast.LENGTH_LONG).show();
            intent.putExtra("hospital",1);
            startActivity(intent);
        });

        //Extra test code which would be for hospital use
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        String uId1 = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference.child("reward").child(uId1).child("hospital1").child("name").setValue("Opal Hospital");
        reference.child("reward").child(uId1).child("hospital1").child("mobile").setValue("01");

        reference.child("reward").child(uId1).child("hospital2").child("name").setValue("BHU");
        reference.child("reward").child(uId1).child("hospital2").child("mobile").setValue("02");

        reference.child("chat").child(uId1).child("chatroom").setValue(" ");

        bloodbank.setOnClickListener(view -> {
            Intent intent = new Intent(dashboard.this, StudentMapsActivity.class);
            Toast.makeText(dashboard.this, "Link yourself as BloodBank \n Click Requst Blood", Toast.LENGTH_LONG).show();
            startActivity(intent);
        });

        lifeline.setOnClickListener(view -> {
            Toast.makeText(dashboard.this, "Under development phase", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(dashboard.this, chat.class);
//            startActivity(intent);
        });


        profile.setOnClickListener(view -> {
            Toast.makeText(dashboard.this, token[0].substring(1)+"\n"+token[1], Toast.LENGTH_SHORT).show();
//             ritu.setVisibility(View.VISIBLE);

        });

        developer.setOnClickListener(view -> {
            Intent intent = new Intent(dashboard.this, developer.class);
            startActivity(intent);
        });
//******************************************************Ending for the details of the card********************************************************************************

//*****************************************************Adding timer for Donor activity***************************************************************
        Handler mHandler = new Handler();
        mHandler.postDelayed(() -> {
            Intent intent = new Intent(dashboard.this, DriverMapsActivity.class);
            startActivity(intent);
        }, 60000L);



//*****************************************************This is the details of the card**********************************************************************************




    }
}
