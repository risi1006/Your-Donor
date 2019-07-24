package com.example.risi.your_donor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileInfo extends AppCompatActivity {

    String name,mob,userno;
    private TextView a,b,c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_info);
        a=(TextView)findViewById(R.id.txtname1);
        b=(TextView)findViewById(R.id.txtmob1);
        c=(TextView)findViewById(R.id.txtid1);
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
                }
                a.setText(name.toString());
                b.setText(mob.toString());
                c.setText(userno.toString());
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });


    }
}
