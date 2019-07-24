package com.example.risi.your_donor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class intermediate1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intermediate1);
    }

    public void Enter(View view) {
        Intent intent = new Intent(intermediate1.this, BloodAvailability.class);
        startActivity(intent);
    }
}
