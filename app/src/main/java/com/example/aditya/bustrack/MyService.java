package com.example.aditya.bustrack;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import android.support.annotation.Nullable;

public class MyService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    //    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//
//        Intent intent1 = new Intent(MyService.this, DriverMapsActivity.class);
//        startActivity(intent1);
//        return START_STICKY;
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
