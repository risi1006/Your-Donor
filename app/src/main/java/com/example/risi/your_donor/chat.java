package com.example.risi.your_donor;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ThrowOnExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class chat extends AppCompatActivity {
   EditText receiver,usermsg;
   String receive;
   int cnt = 0;
   Button join,clear;
   ImageButton send;
    String uId1;
    TextView myText;
   private DatabaseReference mydatabase,rew,id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
         uId1 = FirebaseAuth.getInstance().getCurrentUser().getUid();

        receiver = (EditText)findViewById(R.id.rec);

        join = (Button)findViewById(R.id.join);
        send = (ImageButton)findViewById(R.id.send);
        clear = (Button)findViewById(R.id.clear);


        receiver.setText(uId1);
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receive = receiver.getText().toString();
                id = FirebaseDatabase.getInstance().getReference().child("user_no").child(receive);
                id.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot!=null){
                            receive = dataSnapshot.getValue().toString();
                            Toast.makeText(chat.this,receive, Toast.LENGTH_SHORT).show();
                            mydatabase = FirebaseDatabase.getInstance().getReference().child("chat").child(receive);
                            myText = findViewById(R.id.msg);
//                final TextView myText = findViewById(R.id.msg);
                            mydatabase.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.exists()){
                                        String[] Messages = dataSnapshot.getValue().toString().split(",");
                                        myText.setText("");
                                        cnt=0;
                                        for (int i=0; i<Messages.length;i++){
                                            cnt++;
//                            String[] finalmsg = Messages[i].split("=");
//                            myText.append(finalmsg[1]+ "\n");
                                        }
                                        for(DataSnapshot ds: dataSnapshot.getChildren())
                                        {
                                            String s = ds.getValue().toString();
                                            myText.append(s + "\n");
                                        }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


 //****************************************Counting the no of messages*********************************************************
//                        DatabaseReference rew = FirebaseDatabase.getInstance().getReference().child("chat").child(receive);
//                        rew.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                                for(DataSnapshot ds: dataSnapshot.getChildren())
//                                {   cnt++;
//                                }
//                            }
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//
//                            }
//                        });
                        Toast.makeText(chat.this,String.valueOf(cnt), Toast.LENGTH_SHORT).show();
//****************************************End of count of messages************************************************************

                    }}

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                        myText.setText("Cancelled");
                    }
                });

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usermsg = (EditText)findViewById(R.id.usermsg);
                if(receive.equals(uId1)){
                    mydatabase.child(String.valueOf(cnt)).setValue("                        "+usermsg.getText().toString());
                    Toast.makeText(chat.this,"same", Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(chat.this,"different", Toast.LENGTH_SHORT).show();
                mydatabase.child(String.valueOf(cnt)).setValue(usermsg.getText().toString());}
                usermsg.setText("");
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rew = FirebaseDatabase.getInstance().getReference().child("chat").child(uId1);
                rew.removeValue();
                myText.setText("");
                cnt = 0;
                mydatabase.child(String.valueOf(cnt)).setValue(" ");
                rew = FirebaseDatabase.getInstance().getReference();
            }
        });


    }

}