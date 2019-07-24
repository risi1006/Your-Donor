package com.example.risi.your_donor;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.os.Handler;
import android.provider.Settings;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Map;


import butterknife.BindView;
import butterknife.ButterKnife;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;

import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {
    private static final int RC_PER = 2;
    private Location mLastKnownLocation;
    private NotificationManager notifManager;
    Location location;
    int cnt =0,cnt1=0;
    private final String CHANNEL_ID = "personal notification";
    private final int NOTIFICATION_ID  = 001;
    private GoogleMap mMap;
    public static final int notify = 600000;  //interval between two services(Here Service run every 10 Minute)
    public static final int notify_msg = 1000;
    private Handler mHandler = new Handler();   //run on another Thread to avoid crash
    private Handler mHandler_msg = new Handler();
    private Timer mTimer = null;
    private Timer mTimer_msg = null;

    @Override
    public void onCreate() {
        if (mTimer != null) // Cancel if already existed
            mTimer.cancel();
        else
        {mTimer = new Timer();   //recreate new
            mTimer_msg = new Timer();}
        mTimer.scheduleAtFixedRate(new TimeDisplay(), 0, notify);   //Schedule task
        mTimer.scheduleAtFixedRate(new TimeDisplaymsg(), 0, notify);


    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        throw new UnsupportedOperationException("Not yet implemented");
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimer.cancel();    //For Cancel Timer
        Log.d("service is ","Destroyed");
    }

    //class TimeDisplay for handling task
    private class TimeDisplay extends TimerTask {


            @Override
            public void run() {
                // run on another thread
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("service is ","running");






                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    double lat = 0, lon = 0;
    GpsTracker gt = new GpsTracker(getApplicationContext());
    Location l = gt.getLocation();
    if (l == null) {

//        Toast.makeText(getApplicationContext(), "GPS unable to get Value", Toast.LENGTH_SHORT).show();
    } else {


            lat = l.getLatitude();
            lon = l.getLongitude();
//            Toast.makeText(getApplicationContext(), "GPS Lat = " + lat + "\n lon = " + lon, Toast.LENGTH_SHORT).show();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("donor_available").child(userId).child("l");
            ref.child("0").setValue(String.valueOf(lat));
            ref.child("1").setValue(String.valueOf(lon));




    }
                    }
                });
            }
        }

    private class TimeDisplaymsg extends TimerTask {
        @Override
        public void run() {
            mHandler_msg.post(new Runnable() {
                @Override
                public void run() {

                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference mydatabase = FirebaseDatabase.getInstance().getReference().child("chat").child(userId);
                    mydatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()){
                                String[] Messages = dataSnapshot.getValue().toString().split(",");
                                cnt=0;
                                for (int i=0; i<Messages.length;i++){
                                    cnt++;
                                }

                            }
                            if(cnt!=cnt1)
                            {
                                cnt1= cnt;

                                final int NOTIFY_ID = 0; // ID of notification
                                String id = "001"; // default_channel_id
                                String title = "Your Donor"; // Default Channel
                                Intent intent;
                                PendingIntent pendingIntent;
                                NotificationCompat.Builder builder;
                                if (notifManager == null) {
                                    notifManager = (NotificationManager)MyService.this.getSystemService(Context.NOTIFICATION_SERVICE);
                                }
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    int importance = NotificationManager.IMPORTANCE_HIGH;
                                    NotificationChannel mChannel = notifManager.getNotificationChannel(id);
                                    if (mChannel == null) {
                                        mChannel = new NotificationChannel(id, title, importance);
                                        mChannel.enableVibration(true);
                                        mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                                        notifManager.createNotificationChannel(mChannel);
                                    }
                                    builder = new NotificationCompat.Builder(MyService.this, id);
                                    intent = new Intent(MyService.this, MyService.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    pendingIntent = PendingIntent.getActivity(MyService.this, 0, intent, 0);
                                    builder.setContentTitle("Your Donor")                            // required
                                            .setSmallIcon(R.drawable.ic_hotel_notification)   // required
                                            .setContentText("New Message Arrived") // required
                                            .setDefaults(Notification.DEFAULT_ALL)
                                            .setAutoCancel(true)
                                            .setContentIntent(pendingIntent)
                                            .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                                }
                                else {
                                    builder = new NotificationCompat.Builder(MyService.this, id);
                                    intent = new Intent(MyService.this, MyService.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    pendingIntent = PendingIntent.getActivity(MyService.this, 0, intent, 0);
                                    builder.setContentTitle("Your Donor")                            // required
                                            .setSmallIcon(R.drawable.ic_hotel_notification)   // required
                                            .setContentText("New Message Arrived") // required
                                            .setDefaults(Notification.DEFAULT_ALL)
                                            .setAutoCancel(true)
                                            .setContentIntent(pendingIntent)
                                            .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                                            .setPriority(Notification.PRIORITY_HIGH)
                                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                                }
                                Notification notification = builder.build();
                                notifManager.notify(NOTIFY_ID, notification);
//                                turnGPSOn();


//                                NotificationCompat.Builder builder = new NotificationCompat.Builder(MyService.this,CHANNEL_ID);
//                                builder.setSmallIcon(R.drawable.ic_hotel_notification);
//                                builder.setContentTitle("Your Donor");
//                                builder.setLights(Color.BLUE, 500, 500);
//                                long[] pattern = {500,500,500,500,500,500,500,500,500};
//                                builder.setVibrate(pattern);
//                                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//                                builder.setSound(alarmSound);
//                                builder.setContentText("New Message Arrived ");
//                                builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
//                                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(MyService.this);
//                                notificationManagerCompat.notify(NOTIFICATION_ID,builder.build());

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            });

        }
    }

//    public void turnGPSOn() {
//        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
//        boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        if (!enabled) {
//            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//            alertDialogBuilder.setMessage("GPS is disabled in your device. Enable it?")
//                    .setPositiveButton(R.string.enable_gps, (dialog, id) -> {
//                        /** Here it's leading to GPS setting options*/
//                        Intent callGPSSettingIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                        startActivity(callGPSSettingIntent);
//                    }).setNegativeButton(R.string.cancel, (dialog, id) -> dialog.cancel());
//            AlertDialog alert = alertDialogBuilder.create();
//            alert.show();
//        }
//    }
//
////    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
////        requestPermission();
////        return;
////    }
////
//
////    public void requestPermission() {
//        ActivityCompat.requestPermissions(MyService.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, RC_PER);
////    }

//********************************Turning GPS on*******************************************************


}


