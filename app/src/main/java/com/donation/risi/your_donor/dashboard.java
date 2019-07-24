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
    public String name;
    String mob,userno;
//    private ImageView ritu;
    private TextView txt;
    String[] token;
    int no_user=0;
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


        DatabaseReference user_no = FirebaseDatabase.getInstance().getReference().child("Users").child("Donor");
        user_no.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    for(DataSnapshot ds: dataSnapshot.getChildren())
                    {   no_user++;
                    }
                }
//                Toast.makeText(dashboard.this ,String.valueOf(no_user) ,Toast.LENGTH_SHORT).show();
                FirebaseDatabase.getInstance().getReference().child("1total").setValue(String.valueOf(no_user));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


//*******************************Calling service activity*******************************************************************************
        Intent intent1 = new Intent(dashboard.this, MyService.class);
        startService(intent1);


//*****************************************************This is end details of the card**********************************************************************************

        //To get the ID of the current user
        String uId = FirebaseAuth.getInstance().getCurrentUser().getUid();

//***************************************Setting Everyone As a donor By default**************************************************************
        DatabaseReference sett;
        for(int i=1;i<=10;i++)
        {
            sett = FirebaseDatabase.getInstance().getReference().child("Save the Life").child(String.valueOf(i)).child(uId);
            DatabaseReference finalSett = sett;
            sett.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        finalSett.setValue("Donor");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }



//***************************************By default setup ended here*************************************************************************



//*******************************************************For Name in Dashboard *********************************************************************************************************************************
        FirebaseDatabase.getInstance().getReference().child("Users").child("Donor").child(uId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren())
                {
                    for(DataSnapshot cSnapshot:dataSnapshot.getChildren())
                    {
                        name = dataSnapshot.child("name").getValue().toString();
                        mob = dataSnapshot.child("mobile").getValue().toString();
                        userno = dataSnapshot.child("user_n").getValue().toString();
//                        String name3 = dataSnapshot.getValue().toString();
//
//                        token = name3.split(",");
                        txt.setText("Welcome "+name);
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
            Intent intent = new Intent(dashboard.this, intermediate1.class);
            startActivity(intent);

        });

        nearby.setOnClickListener(view -> {
            Intent intent = new Intent(dashboard.this, AcceptorMapsActivity.class);
            startActivity(intent);
        });

        reward.setOnClickListener(view -> {
            Toast.makeText(dashboard.this, "Under development phase", Toast.LENGTH_LONG).show();
//            Intent intent = new Intent(dashboard.this, reward.class);
//            startActivity(intent);
        });

        hospital.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), AcceptorMapsActivity.class);
            Toast.makeText(dashboard.this, "Link yourself as Hospital \n Click Request Blood", Toast.LENGTH_LONG).show();
            intent.putExtra("hospital",1);
            startActivity(intent);
        });

        //Extra test code which would be for hospital use
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        String uId1 = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        reference.child("reward").child(uId1).child("hospital1").child("name").setValue("Opal Hospital");
//        reference.child("reward").child(uId1).child("hospital1").child("mobile").setValue("01");
//
//        reference.child("reward").child(uId1).child("hospital2").child("name").setValue("BHU");
//        reference.child("reward").child(uId1).child("hospital2").child("mobile").setValue("02");

        reference.child("chat").child(uId1).child("chatroom").setValue("");

        bloodbank.setOnClickListener(view -> {

            DatabaseReference blood = FirebaseDatabase.getInstance().getReference().child("Users").child("Donor").child(uId1);
            blood.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child("bb").exists()){
                    String bob = (String) dataSnapshot.child("bb").getValue();
                    if(bob.equals("true")){
                        Toast.makeText(dashboard.this, "Verified Blood Bank", Toast.LENGTH_LONG).show();
                        FirebaseDatabase.getInstance().getReference().child("Save the Life").child("10").child(uId1).setValue("Donor");
                        Intent intent = new Intent(dashboard.this, BloodBank.class);
                        startActivity(intent);
                    }}
                    else
                    {
                        Toast.makeText(dashboard.this, "This account id not connected\nwith any blood bank", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(dashboard.this, "Not a blood bank", Toast.LENGTH_LONG).show();
                }
            });


        });

        lifeline.setOnClickListener(view -> {
            Toast.makeText(dashboard.this, "Currently Chat is available", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(dashboard.this, chat.class);
            startActivity(intent);
        });


        profile.setOnClickListener(view -> {

            Intent intent = new Intent(dashboard.this, ProfileInfo.class);
            startActivity(intent);
        });

        developer.setOnClickListener(view -> {
            Intent intent = new Intent(dashboard.this, developer.class);
            startActivity(intent);
        });
//******************************************************Ending for the details of the card********************************************************************************

//*****************************************************Adding timer for Donor activity***************************************************************
//        Handler mHandler = new Handler();
//        mHandler.postDelayed(() -> {
//            Intent intent = new Intent(dashboard.this, DonorMapsActivity.class);
//            startActivity(intent);
//        }, 60000L);



//*****************************************************This is the details of the card**********************************************************************************




    }
}
