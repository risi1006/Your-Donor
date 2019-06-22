package com.example.risi.your_donor;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import android.support.annotation.Nullable;
import android.widget.Toast;

public class MyService extends Service {


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        for(int i=0;i<100;i++)
//        {
//            Toast.makeText(MyService.this, String.valueOf(i), Toast.LENGTH_SHORT).show();
//        }
//        Intent intent1 = new Intent(this,DonorMapsActivity.class);
//        startActivity(intent1);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public void onDestroy() {
//        Intent intent = new Intent(this,MyService.class);
//        startActivity(intent);
    }
}
