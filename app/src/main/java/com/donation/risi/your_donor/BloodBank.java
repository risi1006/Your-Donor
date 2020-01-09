package com.donation.risi.your_donor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BloodBank extends AppCompatActivity {

    private String a11=null,a12=null,b11=null,b12=null,ab11=null,ab12=null,o11=null,o12=null;
    private String a21=null,a22=null,b21=null,b22=null,ab21=null,ab22=null,o21=null,o22=null;
    private String a31=null,a32=null,b31=null,b32=null,ab31=null,ab32=null,o31=null,o32=null;
    private String a41=null,a42=null,b41=null,b42=null,ab41=null,ab42=null,o41=null,o42=null;


    private EditText a111;
    private EditText a311;
    private EditText a221;
    private EditText a421;
    private EditText a121;
    private EditText b111;
    private EditText b121;
    private EditText ab111;
    private EditText ab121;
    private EditText o111;
    private EditText o121;
    private EditText a211;
    private EditText b211;
    private EditText b221;
    private EditText ab211;
    private EditText ab221;
    private EditText o211;
    private EditText o221;
    private EditText a321;
    private EditText b311;
    private EditText b321;
    private EditText ab311;
    private EditText ab321;
    private EditText o311;
    private EditText o321;
    private EditText a411;
    private EditText b411;
    private EditText b421;
    private EditText ab411;
    private EditText ab421;
    private EditText o411;
    private EditText o421;

    DatabaseReference user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_bank);
        a111 = (EditText) findViewById(R.id.a11);
        a211 = (EditText)findViewById(R.id.a21);
        a311 = (EditText) findViewById(R.id.a31);
        a411 = (EditText)findViewById(R.id.a41);
        a121 = (EditText)findViewById(R.id.a12);
        a221 = (EditText) findViewById(R.id.a22);
        a321 = (EditText)findViewById(R.id.a32);
        a421 = (EditText) findViewById(R.id.a42);

        b111 = (EditText)findViewById(R.id.b11);
        b211 = (EditText)findViewById(R.id.b21);
        b311 = (EditText)findViewById(R.id.b31);
        b411 = (EditText)findViewById(R.id.b41);
        b121 = (EditText)findViewById(R.id.b12);
        b221 = (EditText)findViewById(R.id.b22);
        b321 = (EditText)findViewById(R.id.b32);
        b421 = (EditText)findViewById(R.id.b42);

        ab111 = (EditText)findViewById(R.id.ab11);
        ab211 = (EditText)findViewById(R.id.ab21);
        ab311 = (EditText)findViewById(R.id.ab31);
        ab411 = (EditText)findViewById(R.id.ab41);
        ab121 = (EditText)findViewById(R.id.ab12);
        ab221 = (EditText)findViewById(R.id.ab22);
        ab321 = (EditText)findViewById(R.id.ab32);
        ab421 = (EditText)findViewById(R.id.ab42);

        o111 = (EditText)findViewById(R.id.o11);
        o211 = (EditText)findViewById(R.id.o21);
        o311 = (EditText)findViewById(R.id.o31);
        o411 = (EditText)findViewById(R.id.o41);
        o121 = (EditText)findViewById(R.id.o12);
        o221 = (EditText)findViewById(R.id.o22);
        o321 = (EditText)findViewById(R.id.o32);
        o421 = (EditText)findViewById(R.id.o42);

        String uId1 = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        DatabaseReference kk = FirebaseDatabase.getInstance("https://save-life-97f46-7093f.firebaseio.com/").getReference().child(uId1).child("Blood").child("WHB");
        DatabaseReference kk = FirebaseDatabase.getInstance().getReference().child(uId1).child("Blood").child("WHB");

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
                    while (dataSnapshot1.child("O-").getValue().toString()==null)
                    {
                        Toast.makeText(BloodBank.this, "", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        DatabaseReference kk1 = FirebaseDatabase.getInstance("https://save-life-97f46-7093f.firebaseio.com/").getReference().child(uId1).child("Blood").child("RBC");
        DatabaseReference kk1 = FirebaseDatabase.getInstance().getReference().child(uId1).child("Blood").child("RBC");

        kk1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot2) {
                if(dataSnapshot2.exists())
                {
                    a211.setText(dataSnapshot2.child("A+").getValue().toString());
                    a221.setText(dataSnapshot2.child("A-").getValue().toString());
                    b211.setText(dataSnapshot2.child("B+").getValue().toString());
                    b221.setText(dataSnapshot2.child("B-").getValue().toString());
                    ab211.setText(dataSnapshot2.child("AB+").getValue().toString());
                    ab221.setText(dataSnapshot2.child("AB-").getValue().toString());
                    o211.setText(dataSnapshot2.child("O+").getValue().toString());
                    o221.setText(dataSnapshot2.child("O-").getValue().toString());
                    while (dataSnapshot2.child("O-").getValue().toString()==null)
                    {
                        Toast.makeText(BloodBank.this, "", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//        DatabaseReference kk2 = FirebaseDatabase.getInstance("https://save-life-97f46-7093f.firebaseio.com/").getReference().child(uId1).child("Blood").child("FFP");
        DatabaseReference kk2 = FirebaseDatabase.getInstance().getReference().child(uId1).child("Blood").child("FFP");

        kk2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot3) {
                if(dataSnapshot3.exists())
                {
                    a311.setText(dataSnapshot3.child("A+").getValue().toString());
                    a321.setText(dataSnapshot3.child("A-").getValue().toString());
                    b311.setText(dataSnapshot3.child("B+").getValue().toString());
                    b321.setText(dataSnapshot3.child("B-").getValue().toString());
                    ab311.setText(dataSnapshot3.child("AB+").getValue().toString());
                    ab321.setText(dataSnapshot3.child("AB-").getValue().toString());
                    o311.setText(dataSnapshot3.child("O+").getValue().toString());
                    o321.setText(dataSnapshot3.child("O-").getValue().toString());
                    while (dataSnapshot3.child("O-").getValue().toString()==null)
                    {
                        Toast.makeText(BloodBank.this, "", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//        DatabaseReference kk3 = FirebaseDatabase.getInstance("https://save-life-97f46-7093f.firebaseio.com/").getReference().child(uId1).child("Blood").child("PC");
        DatabaseReference kk3 = FirebaseDatabase.getInstance().getReference().child(uId1).child("Blood").child("PC");

        kk3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot4) {
                if(dataSnapshot4.exists())
                {
                    a411.setText(dataSnapshot4.child("A+").getValue().toString());
                    a421.setText(dataSnapshot4.child("A-").getValue().toString());
                    b411.setText(dataSnapshot4.child("B+").getValue().toString());
                    b421.setText(dataSnapshot4.child("B-").getValue().toString());
                    ab411.setText(dataSnapshot4.child("AB+").getValue().toString());
                    ab421.setText(dataSnapshot4.child("AB-").getValue().toString());
                    o411.setText(dataSnapshot4.child("O+").getValue().toString());
                    o421.setText(dataSnapshot4.child("O-").getValue().toString());
                    while (dataSnapshot4.child("O-").getValue().toString()==null)
                    {
                        Toast.makeText(BloodBank.this, "", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });











    }

    public void Save(View view) {

        a11 = a111.getText().toString();
        if(a11==null)
            a11=" ";
        a21 = a211.getText().toString();
        if(a21==null)
            a21=" ";
        a31 = a311.getText().toString();
        if(a31==null)
            a31=" ";
        a41 = a411.getText().toString();
        if(a41==null)
            a41=" ";
        a12 = a121.getText().toString();
        if(a12==null)
            a12=" ";
        a22 = a221.getText().toString();
        if(a22==null)
            a22=" ";
        a32 = a321.getText().toString();
        if(a32==null)
            a32=" ";
        a42 = a421.getText().toString();
        if(a42==null)
            a42=" ";

        b11 = b111.getText().toString();
        if(b11==null)
            b11=" ";
        b21 = b211.getText().toString();
        if(b21==null)
            b21=" ";
        b31 = b311.getText().toString();
        if(b31==null)
            b31=" ";
        b41 = b411.getText().toString();
        if(b41==null)
            b41=" ";
        b12 = b121.getText().toString();
        if(b12==null)
            b12=" ";
        b22 = b221.getText().toString();
        if(b22==null)
            b22=" ";
        b32 = b321.getText().toString();
        if(b32==null)
            b32=" ";
        b42 = b421.getText().toString();
        if(b42==null)
            b42=" ";

        ab11 = ab111.getText().toString();
        if(ab11==null)
            ab11=" ";
        ab21 = ab211.getText().toString();
        if(ab21==null)
            ab21=" ";
        ab31 = ab311.getText().toString();
        if(ab31==null)
            ab31=" ";
        ab41 = ab411.getText().toString();
        if(ab41==null)
            ab41=" ";
        ab12 = ab121.getText().toString();
        if(ab12==null)
            ab12=" ";
        ab22 = ab221.getText().toString();
        if(ab22==null)
            ab22=" ";
        ab32 = ab321.getText().toString();
        if(ab32==null)
            ab32=" ";
        ab42 = ab421.getText().toString();
        if(ab42==null)
            ab42=" ";

        o11 = o111.getText().toString();
        if(o11==null)
            o11=" ";
        o21 = o211.getText().toString();
        if(o21==null)
            o21=" ";
        o31 = o311.getText().toString();
        if(o31==null)
            o31=" ";
        o41 = o411.getText().toString();
        if(o41==null)
            o41=" ";
        o12 = o121.getText().toString();
        if(o12==null)
            o12=" ";
        o22 = o221.getText().toString();
        if(o22==null)
            o22=" ";
        o32 = o321.getText().toString();
        if(o32==null)
            o32=" ";
        o42 = o421.getText().toString();
        if(o42==null)
            o42=" ";

        try {
            String uId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        Toast.makeText(BloodBank.this, uId, Toast.LENGTH_LONG).show();
//        DatabaseReference bbstore = FirebaseDatabase.getInstance("https://save-life-97f46-7093f.firebaseio.com/").getReference().child(uId).child("Blood");
            DatabaseReference bbstore = FirebaseDatabase.getInstance().getReference().child(uId).child("Blood");


            bbstore.child("WHB").child("A+").setValue(a11);
            bbstore.child("WHB").child("A-").setValue(a12);
            bbstore.child("WHB").child("B+").setValue(b11);
            bbstore.child("WHB").child("B-").setValue(b12);
            bbstore.child("WHB").child("AB+").setValue(ab11);
            bbstore.child("WHB").child("AB-").setValue(ab12);
            bbstore.child("WHB").child("O+").setValue(o11);
            bbstore.child("WHB").child("O-").setValue(o12);
            int kkr =0;
            while (kkr>=5){
                Toast.makeText(this,"",Toast.LENGTH_SHORT).show();
                kkr++;
            }
            kkr=0;

            bbstore.child("RBC").child("A+").setValue(a21);
            bbstore.child("RBC").child("A-").setValue(a22);
            bbstore.child("RBC").child("B+").setValue(b21);
            bbstore.child("RBC").child("B-").setValue(b22);
            bbstore.child("RBC").child("AB+").setValue(ab21);
            bbstore.child("RBC").child("AB-").setValue(ab22);
            bbstore.child("RBC").child("O+").setValue(o21);
            bbstore.child("RBC").child("O-").setValue(o22);

            while (kkr>=5){
                Toast.makeText(this,"",Toast.LENGTH_SHORT).show();
                kkr++;
            }
            kkr=0;

            bbstore.child("FFP").child("A+").setValue(a31);
            bbstore.child("FFP").child("A-").setValue(a32);
            bbstore.child("FFP").child("B+").setValue(b31);
            bbstore.child("FFP").child("B-").setValue(b32);
            bbstore.child("FFP").child("AB+").setValue(ab31);
            bbstore.child("FFP").child("AB-").setValue(ab32);
            bbstore.child("FFP").child("O+").setValue(o31);
            bbstore.child("FFP").child("O-").setValue(o32);
            while (kkr>=5){
                Toast.makeText(this,"",Toast.LENGTH_SHORT).show();
                kkr++;
            }
            kkr=0;


            bbstore.child("PC").child("A+").setValue(a41);
            bbstore.child("PC").child("A-").setValue(a42);
            bbstore.child("PC").child("B+").setValue(b41);
            bbstore.child("PC").child("B-").setValue(b42);
            bbstore.child("PC").child("AB+").setValue(ab41);
            bbstore.child("PC").child("AB-").setValue(ab42);
            bbstore.child("PC").child("O+").setValue(o41);
            bbstore.child("PC").child("O-").setValue(o42);
            while (kkr>=5){
                Toast.makeText(this,"",Toast.LENGTH_SHORT).show();
                kkr++;
            }
            kkr=0;
        }
        catch (Exception e) {
//            int kkr=0;
//            while (kkr<=5){
//                Toast.makeText(this,"",Toast.LENGTH_SHORT).show();
//                kkr++;
//            }
            Intent intent = new Intent(BloodBank.this, BloodBank.class);
            startActivity(intent);
        }

//        String uId = FirebaseAuth.getInstance().getCurrentUser().getUid();
////        Toast.makeText(BloodBank.this, uId, Toast.LENGTH_LONG).show();
////        DatabaseReference bbstore = FirebaseDatabase.getInstance("https://save-life-97f46-7093f.firebaseio.com/").getReference().child(uId).child("Blood");
//        DatabaseReference bbstore = FirebaseDatabase.getInstance().getReference().child(uId).child("Blood");
//
//
//        bbstore.child("WHB").child("A+").setValue(a11);
//        bbstore.child("WHB").child("A-").setValue(a12);
//        bbstore.child("WHB").child("B+").setValue(b11);
//        bbstore.child("WHB").child("B-").setValue(b12);
//        bbstore.child("WHB").child("AB+").setValue(ab11);
//        bbstore.child("WHB").child("AB-").setValue(ab12);
//        bbstore.child("WHB").child("O+").setValue(o11);
//        bbstore.child("WHB").child("O-").setValue(o12);
//        int kkr =0;
//        while (kkr>=5){
//            Toast.makeText(this,"",Toast.LENGTH_SHORT).show();
//            kkr++;
//        }
//        kkr=0;
//
//        bbstore.child("RBC").child("A+").setValue(a21);
//        bbstore.child("RBC").child("A-").setValue(a22);
//        bbstore.child("RBC").child("B+").setValue(b21);
//        bbstore.child("RBC").child("B-").setValue(b22);
//        bbstore.child("RBC").child("AB+").setValue(ab21);
//        bbstore.child("RBC").child("AB-").setValue(ab22);
//        bbstore.child("RBC").child("O+").setValue(o21);
//        bbstore.child("RBC").child("O-").setValue(o22);
//
//        while (kkr>=5){
//            Toast.makeText(this,"",Toast.LENGTH_SHORT).show();
//            kkr++;
//        }
//        kkr=0;
//
//        bbstore.child("FFP").child("A+").setValue(a31);
//        bbstore.child("FFP").child("A-").setValue(a32);
//        bbstore.child("FFP").child("B+").setValue(b31);
//        bbstore.child("FFP").child("B-").setValue(b32);
//        bbstore.child("FFP").child("AB+").setValue(ab31);
//        bbstore.child("FFP").child("AB-").setValue(ab32);
//        bbstore.child("FFP").child("O+").setValue(o31);
//        bbstore.child("FFP").child("O-").setValue(o32);
//        while (kkr>=5){
//            Toast.makeText(this,"",Toast.LENGTH_SHORT).show();
//            kkr++;
//        }
//        kkr=0;
//
//
//        bbstore.child("PC").child("A+").setValue(a41);
//        bbstore.child("PC").child("A-").setValue(a42);
//        bbstore.child("PC").child("B+").setValue(b41);
//        bbstore.child("PC").child("B-").setValue(b42);
//        bbstore.child("PC").child("AB+").setValue(ab41);
//        bbstore.child("PC").child("AB-").setValue(ab42);
//        bbstore.child("PC").child("O+").setValue(o41);
//        bbstore.child("PC").child("O-").setValue(o42);
//        while (kkr>=5){
//            Toast.makeText(this,"",Toast.LENGTH_SHORT).show();
//            kkr++;
//        }
//        kkr=0;


    }
}
