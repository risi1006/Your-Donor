package com.donation.risi.your_donor;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Handler;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.Service;
import android.os.Build;
import android.os.IBinder;

import java.util.Timer;
import java.util.TimerTask;

import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.Executor;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.widget.Toast.LENGTH_LONG;


public class MyService extends Service {
    Calendar calendar;
    SimpleDateFormat simpledateformat;
    protected GoogleApiClient googleApiClient;
    String Date;
    String lati,longi;
    private FusedLocationProviderClient fusedLocationProviderClient;
    public static final int RequestPermissionCode = 1;
    private static final int RC_PER = 2;
    private NotificationManager notifManager;
    int cnt =0,cnt1=-1;
    private final String CHANNEL_ID = "personal notification";
    private final int NOTIFICATION_ID  = 001;
    private GoogleMap mMap;
    String lat=null,lon=null;
    public static final int notify = 1000;  //interval between two services(Here Service run every 10 Minute)
    public static final int notify_msg = 1800000;
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
        mTimer_msg.scheduleAtFixedRate(new TimeDisplaymsg(), 0, notify_msg);
//        googleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks((ConnectionCallbacks) MyService.this)
//                .addOnConnectionFailedListener((OnConnectionFailedListener) this)
//                .addApi(LocationServices.API)
//                .build();
//
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        throw new UnsupportedOperationException("Not yet implemented");
    }




    @Override
    public void onDestroy() {
//
        super.onDestroy();
//        Intent intent1 = new Intent(MyService.this, MyService.class);
//        startService(intent1);
//        mTimer.cancel();    //For Cancel Timer
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
//                        try {
//                            GpsTracker gt = new GpsTracker(getApplicationContext());
//                            Location l = gt.getLocation();
//                            GpsTracker gps = new GpsTracker(MyService.this);
//                            double latitude = gps.getLatitude();
//                            double longitude = gps.getLongitude();
//
//                            double latt = l.getLatitude();
//                            double lonn = l.getLongitude();
//                            lat = String.valueOf(latt);
//                            lon = String.valueOf(lonn);
//                            if(lat!=null && lon != null)
//                            {
//                                String uId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("donor_available").child(uId).child("l");
//                                ref.child("0").setValue(lat);
//                                ref.child("1").setValue(lon);
//                                Toast.makeText(MyService.this,lat+" "+lon, Toast.LENGTH_LONG).show();
//
//                            }
//                             } catch (Exception e) {
//                            e.printStackTrace();
//                        }
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

                   try {
                            GpsTracker gt = new GpsTracker(getApplicationContext());
                            Location l = gt.getLocation();
                            GpsTracker gps = new GpsTracker(MyService.this);
                            double latitude = gps.getLatitude();
                            double longitude = gps.getLongitude();

                            double latt = l.getLatitude();
                            double lonn = l.getLongitude();
                            lat = String.valueOf(latt);
                            lon = String.valueOf(lonn);
                            if(lat!=null && lon != null)
                            {
                                String uId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("donor_available").child(uId).child("l");
                                ref.child("0").setValue(lat);
                                ref.child("1").setValue(lon);
//                                Toast.makeText(MyService.this,lat+" "+lon, Toast.LENGTH_LONG).show();

                            }
                             } catch (Exception e) {
                            e.printStackTrace();
                        }
                    String userId = null;
                    try {
                        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    } catch (Exception e) {
                        userId=null;
                    }
                    if(userId!=null){
                    calendar = Calendar.getInstance();
                    simpledateformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    Date = simpledateformat.format(calendar.getTime());
                    String time = Date.substring(11,13);
//                    String time = Date.substring(11,19);
                    String date = Date.substring(0,10);
                    FirebaseDatabase.getInstance().getReference().child("Time").child("time").setValue(time);
                    FirebaseDatabase.getInstance().getReference().child("Time").child("date").setValue(date);
//                    Toast.makeText(MyService.this ,time,Toast.LENGTH_SHORT).show();

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
                            if(cnt1==-1)
                            {
                                cnt1= cnt;
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

                }}
            });


        }
    }

    private void updatelocation() {

        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            requestPermission();
            Toast.makeText(this,"Kindly enalbe your location", LENGTH_LONG).show();
        } else {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener((Executor) this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                lati = String.valueOf(location.getLatitude());
                                longi = String.valueOf(location.getLongitude());
                                    Toast.makeText(getApplicationContext(), "GPS Lat = " + lati + "\n lon = " + longi, Toast.LENGTH_SHORT).show();
//
                            }
                        }
                    });
        }

    }

//    private void requestPermission() {
//        ActivityCompat.requestPermissions(MyService.this, new
//                String[]{ACCESS_FINE_LOCATION}, RequestPermissionCode);
//    }


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
//    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//        requestPermission();
//        return;
//    }
////
//
//    public void requestPermission() {
//        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, RC_PER);
//    }

//********************************Turning GPS on*******************************************************


}

class location1 extends AppCompatActivity implements ConnectionCallbacks, OnConnectionFailedListener{

    public static final int RequestPermissionCode = 1;
    protected GoogleApiClient googleApiClient;
    protected TextView longitudeText;
    protected TextView latitudeText;
    protected Location lastLocation;
    public String longi=null;
    public String lati=null;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_huse_h);
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        } else {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                lati = String.valueOf(location.getLatitude());
                                longi = String.valueOf(location.getLongitude());
                                String a1 = lati;
                                String b1 = longi;

                                    Toast.makeText(getApplicationContext(), "GPS Lat = " + lati + "\n lon = " + longi, Toast.LENGTH_SHORT).show();
                                //                                    latitudeText.setText(String.valueOf(location.getLatitude()));
                                //                                    longitudeText.setText(String.valueOf(location.getLongitude()));
                            }
                        }
                    });
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new
                String[]{ACCESS_COARSE_LOCATION}, RequestPermissionCode);

        Handler mHandler = new Handler();
        mHandler.postDelayed(() -> {
            Intent intent = new Intent(this, HUseH.class);
            startActivity(intent);
            finish();

        }, 2000L);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("MainActivity", "Connection failed: " + connectionResult.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("MainActivity", "Connection suspendedd");
    }
}

