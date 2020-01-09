package com.donation.risi.your_donor;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
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
    public static final int RequestPermissionCode = 1;
    protected GoogleApiClient googleApiClient;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private ImageView userimage;
    private SharedPreferences prefs;
    public String name;
    String mob,userno;
    private TextView txt;
    String[] token;
    int no_user=0;
    int i=1;
    String lat = null,lon = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startService(new Intent(this, location_bck.class));
        super.onCreate(savedInstanceState);
        try {
            LoginActivity lg = new LoginActivity();
            lg.loadLocale();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_dashboard);
        certi = (ImageView) findViewById(R.id.usrcerti);
        nearby = (ImageView) findViewById(R.id.usrblood);
//      reward = (ImageView) findViewById(R.id.usrreward);
        profile = (ImageView) findViewById(R.id.usrprofile);
        developer = (ImageView) findViewById(R.id.developer);
        bloodbank = (ImageView) findViewById(R.id.usrbloodbank);
        hospital = (ImageView) findViewById(R.id.usrhospital);
        lifeline = (ImageView) findViewById(R.id.usrrelative);

        txt = (TextView) findViewById(R.id.txtdashboard);

//        turnGPSOn();

//******************************************Counting no of users*********************************************************************
//        DatabaseReference user_no = FirebaseDatabase.getInstance().getReference().child("Users").child("Donor");
//        user_no.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                if (dataSnapshot.exists()) {
//                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                        no_user++;
//                    }
//                }
////                Toast.makeText(dashboard.this ,String.valueOf(no_user) ,Toast.LENGTH_SHORT).show();
//                FirebaseDatabase.getInstance().getReference().child("1total").setValue(String.valueOf(no_user));
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });




//*******************************Calling service activity*******************************************************************************
        Intent intent1 = new Intent(dashboard.this, MyService.class);
        startService(intent1);


//*****************************************************This is end details of the card**********************************************************************************

        //To get the ID of the current user
        String uId = FirebaseAuth.getInstance().getCurrentUser().getUid();

//***************************************Setting Everyone As a donor By default**************************************************************
        DatabaseReference sett;
        for (int i = 1; i <= 10; i++) {
            sett = FirebaseDatabase.getInstance().getReference().child("Save the Life").child(String.valueOf(i)).child(uId);
            DatabaseReference finalSett = sett;
            sett.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        finalSett.setValue("Donor");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

//        Toast.makeText(getApplicationContext(), "HI", Toast.LENGTH_SHORT).show();
//***************************************By default setup ended here*************************************************************************
//        try {
//            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//
//            GpsTracker gt = new GpsTracker(getApplicationContext());
////        Toast.makeText(getApplicationContext(), String.valueOf(gt) + lon, Toast.LENGTH_LONG).show();
//            Location l = gt.getLocation();
////        if (l == null) {
////
//////        Toast.makeText(getApplicationContext(), "GPS unable to get Value", Toast.LENGTH_SHORT).show();
////        } else {
//////
//            GpsTracker gps = new GpsTracker(this);
//            double latitude = gps.getLatitude();
//            double longitude = gps.getLongitude();
//
//            lat = l.getLatitude();
//            lon = l.getLongitude();
////            Toast.makeText(getApplicationContext(), "GPS Lat = " + latitude + "\n lon = " + longitude, Toast.LENGTH_LONG).show();
//
//            if(String.valueOf(lon)!=null && String.valueOf(lon)!=null){
//            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("donor_available").child(userId).child("l");
////            Toast.makeText(getApplicationContext(), String.valueOf(lat)+" "+String.valueOf(lon), Toast.LENGTH_SHORT).show();
//            ref.child("0").setValue(lat);
//            ref.child("1").setValue(lon);}
//        } catch (Exception e) {
////            Toast.makeText(getApplicationContext(), "GPS failed...", Toast.LENGTH_LONG).show();
//        }
////    }
////        Toast.makeText(getApplicationContext(), "BYE", Toast.LENGTH_SHORT).show();


//*******************************************************For Name in Dashboard *********************************************************************************************************************************
        FirebaseDatabase.getInstance().getReference().child("Users").child("Donor").child(uId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot cSnapshot : dataSnapshot.getChildren()) {
                        name = dataSnapshot.child("name").getValue().toString();
                        mob = dataSnapshot.child("mobile").getValue().toString();
                        userno = dataSnapshot.child("user_n").getValue().toString();
//                        String name3 = dataSnapshot.getValue().toString();
//
//                        token = name3.split(",");
                        txt.setText(name);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//******************************************************Ending of the name*******************************************************************************************




//******************************************************Pressing the cards*******************************************************************************************

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        String uId1 = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference.child("chat").child(uId1).child("chatroom").setValue("");
        reference.child("chat").child(uId1).child("chatroom").setValue("");
    }

    public void logof() {




        try {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this)
                    .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this)
                    .addApi(LocationServices.API)
                    .build();

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                lat = String.valueOf(location.getLatitude());
                                lon = String.valueOf(location.getLongitude());
//                                    Toast.makeText(getApplicationContext(), "GPS Lat = " + lati + "\n lon = " + longi, Toast.LENGTH_SHORT).show()

                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Intent intent1 = new Intent(dashboard.this, MyService.class);
            stopService(intent1);
            FirebaseAuth.getInstance().signOut();
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove(getString(R.string.isDriver));
            editor.remove(getString(R.string.acceptor_maps_first_time_launch));
            editor.remove(getString(R.string.bus_no));
            editor.commit();
            Intent intent = new Intent(dashboard.this, LoginActivity.class);
            Bundle extra = new Bundle();
            extra.putString("latti", String.valueOf(lat));
            extra.putString("longgi", String.valueOf(lon));
            intent.putExtras(extra);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            Intent intent = new Intent(dashboard.this, LoginActivity.class);
            Bundle extra = new Bundle();
            extra.putString("latti", String.valueOf(lat));
            extra.putString("longgi", String.valueOf(lon));
            intent.putExtras(extra);
            startActivity(intent);
            finish();
        }
    }


    public void BB(View view) {
        //blood bank
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        String uId1 = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference.child("chat").child(uId1).child("chatroom").setValue("");
        DatabaseReference blood = FirebaseDatabase.getInstance().getReference().child("Users").child("Donor").child(uId1);
        blood.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("bb").exists()) {
                    String bob = (String) dataSnapshot.child("bb").getValue();
                    if (bob.equals("true")) {
                        Toast.makeText(dashboard.this, "Verified Blood Bank", Toast.LENGTH_LONG).show();
                        FirebaseDatabase.getInstance().getReference().child("Save the Life").child("10").child(uId1).setValue("Donor");
                        Intent intent = new Intent(dashboard.this, BloodBank.class);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(dashboard.this, "This account id not connected\nwith any blood bank", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(dashboard.this, "Not a blood bank", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void CBA(View view) {
        //check blood availability
        Intent intent = new Intent(dashboard.this, BloodAvailability.class);
        startActivity(intent);
    }

    public void BN(View view) {
        //blood nearby
        Intent intent = new Intent(dashboard.this, AcceptorMapsActivity.class);
        startActivity(intent);
    }

    public void OC(View view) {
        //organize camp
        Toast.makeText(dashboard.this, "Under Development Phase", Toast.LENGTH_LONG).show();
    }

    public void YP(View view) {
        //your profile
        Intent intent = new Intent(dashboard.this, ProfileInfo.class);
        startActivity(intent);
    }

    public void CL(View view) {
        //contact lifeline
        Toast.makeText(dashboard.this, "Currently Chat is available", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(dashboard.this, chat.class);
        startActivity(intent);
    }

    public void Dev(View view) {
        //Developer
        Intent intent = new Intent(dashboard.this, developer.class);
        startActivity(intent);
    }

    public void logoff(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logging out...")
                .setMessage("Are you sure you want to logout??")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logof();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
