package com.donation.risi.your_donor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class reward extends AppCompatActivity {
    DatabaseReference reference;
    ListView l1;
    TextView txtProgress1;
    TextView txtProgress2;
    ProgressBar progressBar1;
    ProgressBar progressBar2;
    User user;
    int i = 0;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);

//        progressBar1 = (ProgressBar) findViewById(R.id.progressBar);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        txtProgress2 = (TextView)findViewById(R.id.txtProgress2);
//*****************************************************Adding timer for Donor activity***************************************************************
//        Handler mHandler = new Handler();
//        mHandler.postDelayed(() -> {
//            Intent intent = new Intent(reward.this, DonorMapsActivity.class);
//            startActivity(intent);
//            finish();
//        }, 60000L);



//*****************************************************This is the details of the card**********************************************************************************



        l1 = (ListView)findViewById(R.id.listview);
        user = new User();
        String uId = FirebaseAuth.getInstance().getCurrentUser().getUid();




        String ref = "Registration No"+"                         "+"Hospital";
        list = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);
        l1.setAdapter(adapter);
        list.add(ref);


        DatabaseReference rew = FirebaseDatabase.getInstance().getReference().child("reward").child(uId);
        rew.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot ds: dataSnapshot.getChildren())
                {   i++;
                    user = ds.getValue(User.class);
                    String repeated = new String(new char[20]).replace("\0", " ");
                    list.add(user.getMobile().toString()+repeated+user.getName().toString());
                }
                l1.setAdapter(adapter);


                progressBar2.setProgress(i*10);
                txtProgress2.setText(i*10 + "Pts");
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
