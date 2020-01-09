package com.donation.risi.your_donor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BloodAvailability extends AppCompatActivity {
    private EditText text;
    private TextView a111,a121,b111,b121,ab111,ab121,o111,o121;
    private TextView a211,a221,b211,b221,ab211,ab221,o211,o221;
    private TextView a311,a321,b311,b321,ab311,ab321,o311,o321;
    private TextView a411,a421,b411,b421,ab411,ab421,o411,o421;
    String keys;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_availability);
        text = (EditText)findViewById(R.id.risitext);
        a111 = (TextView)findViewById(R.id.ax11);
        a211 = (TextView)findViewById(R.id.ax21);
        a311 = (TextView)findViewById(R.id.ax31);
        a411 = (TextView)findViewById(R.id.ax41);
        a121 = (TextView)findViewById(R.id.ax12);
        a221 = (TextView)findViewById(R.id.ax22);
        a321 = (TextView)findViewById(R.id.ax32);
        a421 = (TextView)findViewById(R.id.ax42);

        b111 = (TextView)findViewById(R.id.bx11);
        b211 = (TextView)findViewById(R.id.bx21);
        b311 = (TextView)findViewById(R.id.bx31);
        b411 = (TextView)findViewById(R.id.bx41);
        b121 = (TextView)findViewById(R.id.bx12);
        b221 = (TextView)findViewById(R.id.bx22);
        b321 = (TextView)findViewById(R.id.bx32);
        b421 = (TextView)findViewById(R.id.bx42);

        ab111 = (TextView)findViewById(R.id.abx11);
        ab211 = (TextView)findViewById(R.id.abx21);
        ab311 = (TextView)findViewById(R.id.abx31);
        ab411 = (TextView)findViewById(R.id.abx41);
        ab121 = (TextView)findViewById(R.id.abx12);
        ab221 = (TextView)findViewById(R.id.abx22);
        ab321 = (TextView)findViewById(R.id.abx32);
        ab421 = (TextView)findViewById(R.id.abx42);

        o111 = (TextView)findViewById(R.id.ox11);
        o211 = (TextView)findViewById(R.id.ox21);
        o311 = (TextView)findViewById(R.id.ox31);
        o411 = (TextView)findViewById(R.id.ox41);
        o121 = (TextView)findViewById(R.id.ox12);
        o221 = (TextView)findViewById(R.id.ox22);
        o321 = (TextView)findViewById(R.id.ox32);
        o421 = (TextView)findViewById(R.id.ox42);



    }

    public void search(View view) {
        String xx = text.getText().toString();

        DatabaseReference xy = FirebaseDatabase.getInstance().getReference().child("user_no").child(xx);
        xy.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    keys = dataSnapshot.getValue().toString();
//                    DatabaseReference kk = FirebaseDatabase.getInstance("https://save-life-97f46-7093f.firebaseio.com/").getReference().child(keys).child("Blood").child("WHB");
                    DatabaseReference kk = FirebaseDatabase.getInstance().getReference().child(keys).child("Blood").child("WHB");
                    kk.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot1) {
                            if(dataSnapshot1.exists())
                            {
                                a111.setText(dataSnapshot1.child("A+").getValue().toString());
                                a121.setText(dataSnapshot1.child("A-").getValue().toString());
                                b111.setText(dataSnapshot1.child("B+").getValue().toString());
                                b121.setText(dataSnapshot1.child("B-").getValue().toString());
                                ab111.setText(dataSnapshot1.child("AB+").getValue().toString());
                                ab121.setText(dataSnapshot1.child("AB-").getValue().toString());
                                o111.setText(dataSnapshot1.child("O+").getValue().toString());
                                o121.setText(dataSnapshot1.child("O-").getValue().toString());
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
//                    DatabaseReference kk1 = FirebaseDatabase.getInstance("https://save-life-97f46-7093f.firebaseio.com/").getReference().child(keys).child("Blood").child("RBC");
                    DatabaseReference kk1 = FirebaseDatabase.getInstance().getReference().child(keys).child("Blood").child("RBC");

                    kk1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot1) {
                            if(dataSnapshot1.exists())
                            {
                                a211.setText(dataSnapshot1.child("A+").getValue().toString());
                                a221.setText(dataSnapshot1.child("A-").getValue().toString());
                                b211.setText(dataSnapshot1.child("B+").getValue().toString());
                                b221.setText(dataSnapshot1.child("B-").getValue().toString());
                                ab211.setText(dataSnapshot1.child("AB+").getValue().toString());
                                ab221.setText(dataSnapshot1.child("AB-").getValue().toString());
                                o211.setText(dataSnapshot1.child("O+").getValue().toString());
                                o221.setText(dataSnapshot1.child("O-").getValue().toString());
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
//                    DatabaseReference kk2 = FirebaseDatabase.getInstance("https://save-life-97f46-7093f.firebaseio.com/").getReference().child(keys).child("Blood").child("FFP");
                    DatabaseReference kk2 = FirebaseDatabase.getInstance().getReference().child(keys).child("Blood").child("FFP");

                    kk2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot1) {
                            if(dataSnapshot1.exists())
                            {
                                a311.setText(dataSnapshot1.child("A+").getValue().toString());
                                a321.setText(dataSnapshot1.child("A-").getValue().toString());
                                b311.setText(dataSnapshot1.child("B+").getValue().toString());
                                b321.setText(dataSnapshot1.child("B-").getValue().toString());
                                ab311.setText(dataSnapshot1.child("AB+").getValue().toString());
                                ab321.setText(dataSnapshot1.child("AB-").getValue().toString());
                                o311.setText(dataSnapshot1.child("O+").getValue().toString());
                                o321.setText(dataSnapshot1.child("O-").getValue().toString());
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
//                    DatabaseReference kk3 = FirebaseDatabase.getInstance("https://save-life-97f46-7093f.firebaseio.com/").getReference().child(keys).child("Blood").child("PC");
                    DatabaseReference kk3 = FirebaseDatabase.getInstance().getReference().child(keys).child("Blood").child("PC");

                    kk3.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot1) {
                            if(dataSnapshot1.exists())
                            {
                                a411.setText(dataSnapshot1.child("A+").getValue().toString());
                                a421.setText(dataSnapshot1.child("A-").getValue().toString());
                                b411.setText(dataSnapshot1.child("B+").getValue().toString());
                                b421.setText(dataSnapshot1.child("B-").getValue().toString());
                                ab411.setText(dataSnapshot1.child("AB+").getValue().toString());
                                ab421.setText(dataSnapshot1.child("AB-").getValue().toString());
                                o411.setText(dataSnapshot1.child("O+").getValue().toString());
                                o421.setText(dataSnapshot1.child("O-").getValue().toString());
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
