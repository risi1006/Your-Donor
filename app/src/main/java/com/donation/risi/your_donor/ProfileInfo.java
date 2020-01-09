package com.donation.risi.your_donor;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static java.sql.Types.INTEGER;

public class ProfileInfo extends AppCompatActivity {

    String name,mob,userno,group;
    String lat=null,lon=null;
    private TextView a,b,c;
    private TextView verify;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_info);
        a=(TextView)findViewById(R.id.txtname1);
        b=(TextView)findViewById(R.id.txtmob1);
        c=(TextView)findViewById(R.id.txtid1);
        verify = (TextView)findViewById(R.id.varification);

        String uId = FirebaseAuth.getInstance().getCurrentUser().getUid();
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
                    group = dataSnapshot.child("group").getValue().toString();
                }
                a.setText(name.toString());
                b.setText(mob.toString());
                c.setText(userno.toString());
                String check = "NA";
                if(userno.equalsIgnoreCase(check))
                {
                    verify.setText("Verification Pending ):");
//                    verify.setTextColor(0xEE0B0B);
                }
                else
                {

                    verify.setText("Verified Account :)");
                    verify.setTextColor(0xFF4EB10C);
//                    verify.setText("Verification Pending ):");
//                    verify.setTextColor(0xEE0B0B);
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });


    }

    public void edit(View view) {
        //for editing access given to the user
        Intent intent = new Intent(getApplicationContext(),editprofile.class);
        startActivity(intent);
    }

    public void deregister(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete the Account")
               .setMessage("All your data would be deleted from the Server...Do you want to continue??")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete_fianl();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void delete_fianl() {
        String uId1 = FirebaseAuth.getInstance().getCurrentUser().getUid();
        try {
            DatabaseReference rew = FirebaseDatabase.getInstance().getReference().child("Save the Life").child(group).child(uId1);
            rew.removeValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            DatabaseReference rew1 = FirebaseDatabase.getInstance().getReference().child("Users").child("Donor").child(uId1);
            rew1.removeValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            DatabaseReference rew2 = FirebaseDatabase.getInstance().getReference().child("chat").child(uId1);
            rew2.removeValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            DatabaseReference rew3 = FirebaseDatabase.getInstance().getReference().child("donor_available").child(uId1);
            rew3.removeValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            DatabaseReference rew4 = FirebaseDatabase.getInstance().getReference().child("user_no").child(uId1);
            rew4.removeValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            DatabaseReference rew5 = FirebaseDatabase.getInstance().getReference().child("user_no").child(userno);
            rew5.removeValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        deleteAccount();
    }


    private void deleteAccount() {

        String lat = null,lon = null;

        try {
            GpsTracker gt = new GpsTracker(getApplicationContext());
            Location l = gt.getLocation();
            GpsTracker gps = new GpsTracker(ProfileInfo.this);
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            double latt = l.getLatitude();
            double lonn = l.getLongitude();
            lat = String.valueOf(latt);
            lon = String.valueOf(lonn);
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            final FirebaseUser currentUser = firebaseAuth.getCurrentUser();

            String finalLat = lat;
            String finalLon = lon;
            currentUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(ProfileInfo.this, LoginActivity.class);
                        Bundle extra = new Bundle();
                        extra.putString("latti", String.valueOf(finalLat));
                        extra.putString("longgi", finalLon);
                        intent.putExtras(extra);
                        startActivity(intent);
                        finish();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        } catch (Exception e) {
            String finalLat = lat;
            String finalLon = lon;
            Intent intent = new Intent(ProfileInfo.this, LoginActivity.class);
            Bundle extra = new Bundle();
            extra.putString("latti", String.valueOf(finalLat));
            extra.putString("longgi", finalLon);
            intent.putExtras(extra);
            startActivity(intent);
            finish();
        }
    }
}
