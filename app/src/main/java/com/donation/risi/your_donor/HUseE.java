package com.donation.risi.your_donor;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class HUseE extends AppCompatActivity {

    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huse_e);
    viewPager = (ViewPager)findViewById(R.id.myViewPager);

    ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
    viewPager.setAdapter(viewPagerAdapter);

    }

}