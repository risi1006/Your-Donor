//63 113 134 139

package com.donation.risi.your_donor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class chat extends AppCompatActivity {
   EditText receiver,usermsg;
   String receive;
   int cnt = 0;
   int flag=0;
   public String ritesh="";
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


//**********************************Displaying users message*****************************************************************************

        mydatabase = FirebaseDatabase.getInstance().getReference().child("chat").child(uId1);
        myText = findViewById(R.id.msg);
        mydatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    String[] Messages = dataSnapshot.getValue().toString().split(",");
                    myText.setText("");
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

//**********************************End of disp usrs message*****************************************************************************









        receiver = (EditText)findViewById(R.id.rec);

        join = (Button)findViewById(R.id.join);
        send = (ImageButton)findViewById(R.id.send);
        clear = (Button)findViewById(R.id.clear);


//******************************************************appending verification****************************************************************

        DatabaseReference blood = FirebaseDatabase.getInstance().getReference().child("Users").child("Donor").child(uId1);
        blood.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("bb").exists()){
                    String bob = (String) dataSnapshot.child("bb").getValue();
                    if(bob.equals("true")){
                        flag=1;
                    }}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(chat.this, "Not a blood bank", Toast.LENGTH_LONG).show();
            }
        });

//********************************************************************************************************************************************


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
//                            Toast.makeText(chat.this,receive, Toast.LENGTH_SHORT).show();
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
//                        Toast.makeText(chat.this,String.valueOf(cnt), Toast.LENGTH_SHORT).show();
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
                try {
                    usermsg = (EditText)findViewById(R.id.usermsg);
                    if(receive.equals(uId1)){
                        if(flag==1){
                        mydatabase.child(String.valueOf(cnt)).setValue("                        "+usermsg.getText().toString()+" (Bloodbank)");
    //                    Toast.makeText(chat.this,"same", Toast.LENGTH_SHORT).show();


                    }
                    else
                        {
                            mydatabase.child(String.valueOf(cnt)).setValue("                        "+usermsg.getText().toString()+"( Not Bloodbank)");
                        }
                    }
                    else {
                        if(flag==1){
    //                    Toast.makeText(chat.this,"different", Toast.LENGTH_SHORT).show();
                    mydatabase.child(String.valueOf(cnt)).setValue(usermsg.getText().toString()+" (Bloodbank)");
                        }
                    else
                        {
                            mydatabase.child(String.valueOf(cnt)).setValue(usermsg.getText().toString()+" (Not Bloodbank)");
                        }

                    }
                    usermsg.setText("");
                } catch (Exception e) {
                    Toast.makeText(chat.this,"No user ID found", Toast.LENGTH_SHORT).show();
                }

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
