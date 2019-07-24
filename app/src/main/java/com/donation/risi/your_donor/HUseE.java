package com.example.risi.your_donor;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

public class HUseE extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huse_e);
    }

    public void userrE(View view) {
        //for user
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=AsAh9e3GbZM&t=1s"));
        startActivity(intent);

    }

    public void userB(View view) {
        //for blood bank
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=hicyjCLPYYk"));
        startActivity(intent);

    }
}
