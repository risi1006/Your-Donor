package com.donation.risi.your_donor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class editprofile extends AppCompatActivity {

    private EditText e1,e2;
    private Button B1,B2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
        getui();
    }

    private void getui() {
        e1 = (EditText)findViewById(R.id.updtN);
        e2 = (EditText)findViewById(R.id.updtM);
    }

    public void upName(View view) {
        try {
            if(e1.getText().toString()!=""){
            String uId1 = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference rew1 = FirebaseDatabase.getInstance().getReference().child("Users").child("Donor").child(uId1);
            rew1.child("name").setValue(e1.getText().toString());
            Toast.makeText(this,"Name updated successfully...",Toast.LENGTH_SHORT).show();}
            else
            {
                Toast.makeText(this,"Unable to update Name",Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this,"Unable to update Name",Toast.LENGTH_SHORT).show();
        }
    }

    public void upMob(View view) {
        try {
            if(e2.getText().toString()!=" "){
            String uId1 = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference rew1 = FirebaseDatabase.getInstance().getReference().child("Users").child("Donor").child(uId1);
            rew1.child("mobile").setValue(e2.getText().toString());
            Toast.makeText(this,"Moblile no updated successfully...",Toast.LENGTH_SHORT).show();}
            else
            {
                Toast.makeText(this,"Unable to update Mobile",Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this,"Unable to update Mobile",Toast.LENGTH_SHORT).show();
        }
    }
}
