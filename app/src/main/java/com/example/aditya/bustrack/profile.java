package com.example.aditya.bustrack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;

public class profile extends AppCompatActivity {

    private EditText t1, t2, t3;
    private Button submit;
    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        t1 = (EditText) findViewById(R.id.usrname);
        t2 = (EditText) findViewById(R.id.usrno);
        t3 = (EditText) findViewById(R.id.usrgroup);
        submit = (Button) findViewById(R.id.submit);
        Firebase.setAndroidContext(getApplicationContext());


        submit.setOnClickListener(v -> {

            String name = t1.getText().toString();
            String contact = t2.getText().toString();
            String blood = t3.getText().toString();

            if(name==""|| contact==""){
                Toast.makeText(this,"Enter all the fields", Toast.LENGTH_SHORT).show();
            }
            else
            {
                User user1 = new User();
                user1.setName(name);
                user1.setMobile(contact);
                user1.setGroup(blood);
//                Firebase firebase = new Firebase(Config.url);
//                firebase.child("person"+i).setValue(user);
//                i++;
                Toast.makeText(this,"Data saved successfully",Toast.LENGTH_SHORT).show();
            }

            Intent intent = new Intent(profile.this, dashboard.class);
            startActivity(intent);
        });
    }
}