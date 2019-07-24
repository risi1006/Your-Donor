package com.example.risi.your_donor;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HUseH extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huse_h);
    }

    public void abcd(View view) {
        //blood bank
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=hicyjCLPYYk"));
        startActivity(intent);
    }

    public void efgh(View view) {
        //user
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=AsAh9e3GbZM&t=1s"));
        startActivity(intent);
    }
}
