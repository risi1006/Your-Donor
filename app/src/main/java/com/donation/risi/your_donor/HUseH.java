package com.donation.risi.your_donor;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
public class HUseH extends AppCompatActivity implements ConnectionCallbacks, OnConnectionFailedListener {

    public static final int RequestPermissionCode = 1;
    protected GoogleApiClient googleApiClient;
    protected TextView longitudeText;
    protected TextView latitudeText;
    protected Location lastLocation;
    private JobScheduler jobScheduler;
    private ComponentName componentName;
    private JobInfo jobInfo;
    public String longi=null;
    public String lati=null;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            LoginActivity lg = new LoginActivity();
            lg.loadLocale();
        } catch (Exception e) {
            e.printStackTrace();
        }
//            StartBackgroundTask();
        setContentView(R.layout.activity_huse_h);
//            setContentView(R.layout.activity_login);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void StartBackgroundTask() {
        jobScheduler = (JobScheduler) getApplicationContext().getSystemService(JOB_SCHEDULER_SERVICE);
        componentName = new ComponentName(getApplicationContext(), location_bck.class);
        jobInfo = new JobInfo.Builder(1, componentName)
                .setMinimumLatency(10000) //10 sec interval
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY).setRequiresCharging(false).build();
        jobScheduler.schedule(jobInfo);
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

        if (ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission1();
        }
            else {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                lati = String.valueOf(location.getLatitude());
                                longi = String.valueOf(location.getLongitude());
//                                    Toast.makeText(getApplicationContext(), "GPS Lat = " + lati + "\n lon = " + longi, Toast.LENGTH_SHORT).show();
                                if (lati != null) {
                                    Intent intent = new Intent(HUseH.this, LoginActivity.class);
                                    Bundle extra = new Bundle();
                                    extra.putString("latti", lati);
                                    extra.putString("longgi", longi);
                                    intent.putExtras(extra);
                                    startActivity(intent);
                                    finish();
                                }
//                                    Toast.makeText(getApplicationContext(), "GPS Lat = " + lati + "\n lon = " + longi, Toast.LENGTH_SHORT).show();
                                //                                    latitudeText.setText(String.valueOf(location.getLatitude()));
                                //                                    longitudeText.setText(String.valueOf(location.getLongitude()));
                            }
                        }
                    });
        }
    }

    private void requestPermission1() {
        ActivityCompat.requestPermissions(HUseH.this, new
                String[]{ACCESS_COARSE_LOCATION}, RequestPermissionCode);

        Handler mHandler = new Handler();
        mHandler.postDelayed(() -> {
            Intent intent = new Intent(HUseH.this, HUseH.class);
            startActivity(intent);
            finish();

        }, 2000L);
    }

    private void requestPermission2() {
        ActivityCompat.requestPermissions(HUseH.this, new
                String[]{ACCESS_FINE_LOCATION}, RequestPermissionCode);

        Handler mHandler = new Handler();
        mHandler.postDelayed(() -> {
            Intent intent = new Intent(HUseH.this, HUseH.class);
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



