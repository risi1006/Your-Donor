package com.example.risi.your_donor;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
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

public class DriverMapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

//*********************************************Definging the buttons of the drawer*********************************************************************
    @BindView(R.id.logout_btn_driver)
    Button mLogout;
    @BindView(R.id.link_bus)
    Button mLinkBus;
    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar mToolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_view)
    NavigationView mNavigationView;

//*********************************************Defining the variables******************************************************************************
    public static final String LOG_TAG = DriverMapsActivity.class.getSimpleName();
    private static final int RC_PER = 2;

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastKnownLocation;
    private LocationRequest mLocationRequest;
    private int bus_num;
    private ActionBarDrawerToggle mDrawerToggle;
    private View mMapView;
    private String channelId = "my_channel";
    private String studentId = "";
    double studentLocationLat = 0;
    double studentLocationLon = 0;
    private Marker mStudentMarker;

    @Override
    protected void onStop() {
        if (mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        DriverMapsActivity.setVisibility(View.INVISIBLE);
//*************************************First check if GPS is enabled************************************************************************************
        turnGPSOn();


        setContentView(R.layout.activity_driver_maps);

        ButterKnife.bind(this);
//************************************* Toolbar :: Transparent******************************************************************************************
        mToolbar.setBackgroundColor(Color.TRANSPARENT);
        setSupportActionBar(mToolbar);

        Window window = this.getWindow();
//************************************* Status bar :: Transparent**************************************************************************************
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(Color.TRANSPARENT);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        setupDrawerContent(mNavigationView);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);

//************************************** Obtain the SupportMapFragment and get notified when the map is ready to be used.*******************************
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

//        mMapView = mapFragment.getView();   it was here

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mMapView = mapFragment.getView();

        Boolean startFromNotification = getIntent().getBooleanExtra(getString(R.string.launched_via_notification), false);
        if (startFromNotification) {

            Log.e(LOG_TAG, "Launched from notification");
            //TODO: Application launched from notification and need to be handle effectively.
            String uId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child("Donor").child(uId);

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                        studentId = map.get("acceptorRequest").toString();


                        Log.e(LOG_TAG, "Acceptor id is " + studentId);

                        DatabaseReference studentLocation = FirebaseDatabase.getInstance().getReference().child("Users").child("Acceptor").child(studentId).child(studentId).child("l");
                        studentLocation.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                List<Object> map = (List<Object>) dataSnapshot.getValue();
                                studentLocationLat = Double.parseDouble(map.get(0).toString());
                                studentLocationLon = Double.parseDouble(map.get(1).toString());
                                LatLng studentLatLng = new LatLng(studentLocationLat, studentLocationLon);
                                if (mStudentMarker!=null) mStudentMarker.remove();
                                mStudentMarker = mMap.addMarker(new MarkerOptions().position(studentLatLng).title("Here!"));
                                Log.e(LOG_TAG, "Location of the Acceptor is " + map.get(0));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


//*********************************************Garbage***********************************************************************************************

//        DatabaseReference requestRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Donor").child(uid);
//        requestRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Toast.makeText(DriverMapsActivity.this, "Request Available!", Toast.LENGTH_LONG).show();
//                CharSequence name = getString(R.string.channel_name);
//                String description = getString(R.string.channel_description);
//                @SuppressLint("InlinedApi") int importance = NotificationManager.IMPORTANCE_DEFAULT;
////                int importance = NotificationManager.IMPORTANCE_DEFAULT;
//
//                NotificationCompat.Builder builder = new NotificationCompat.Builder(DriverMapsActivity.this, channelId);
//                Intent notificationIntent = new Intent(DriverMapsActivity.this, DriverMapsActivity.class);
//                notificationIntent.putExtra(getString(R.string.launched_via_notification), true);
//                TaskStackBuilder stackBuilder = TaskStackBuilder.create(DriverMapsActivity.this);
//                stackBuilder.addParentStack(DriverMapsActivity.this);
//                stackBuilder.addNextIntent(notificationIntent);
//                PendingIntent notificationPendingIntent = stackBuilder.getPendingIntent(1,
//                        PendingIntent.FLAG_UPDATE_CURRENT);
//
//
//                builder.setSmallIcon(R.drawable.ic_launcher_foreground)
//                        .setLargeIcon(BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.ic_launcher_foreground))
//                        .setContentTitle(getString(R.string.request_wait_notification))
//                        .setContentText("Tap me to view him/her on the map")
//                        .setContentIntent(notificationPendingIntent)
//                        .setAutoCancel(true)
//                        .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
//                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
//
//                mNotificationManager.notify(1, builder.build());
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

//*****************************************************************************************************************************************************

    }
//*************************************Function to turn GPS on******************************************************************************************
    public void turnGPSOn() {
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!enabled) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("GPS is disabled in your device. Enable it?")
                    .setPositiveButton(R.string.enable_gps, (dialog, id) -> {
                        /** Here it's leading to GPS setting options*/
                        Intent callGPSSettingIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(callGPSSettingIntent);
                    }).setNegativeButton(R.string.cancel, (dialog, id) -> dialog.cancel());
                            AlertDialog alert = alertDialogBuilder.create();
                alert.show();
        }
    }


//***************************************set up drawer*************************************************************************************************
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(item -> {
            selectDrawrItem(item);
            return true;
        });

    }


//***************************************Select drawer item********************************************************************************************
  private void selectDrawrItem(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.link_bus:
                AlertDialog.Builder metaDialog = new AlertDialog.Builder(DriverMapsActivity.this);
                metaDialog.setTitle(getString(R.string.selectBusTitle))
                        .setItems(R.array.bus_numbers, (dialogInterface, i) -> {
                            switch (i) {
                                case 0:
                                    bus_num = 2;
                                    break;
                                case 1:
                                    bus_num = 8;
                                    break;
                                case 2:
                                    bus_num = 11;
                                    break;
                                case 3:
                                    bus_num = 23;
                                    break;
                                case 4:
                                    bus_num = 21;
                                    break;
                                case 5:
                                    bus_num = 81;
                                    break;
                                case 6:
                                    bus_num = 111;
                                    break;
                                case 7:
                                    bus_num = 231;
                                    break;
                                case 8:
                                    bus_num = 111;
                                    break;
                                case 9:
                                    bus_num = 231;
                                    break;


                            }
                            String uId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Save the Life").child(String.valueOf(bus_num)).child(uId);
                            ref.setValue("Donor");
                        });
                metaDialog.show();
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                SharedPreferences.Editor editor = getPreferences(Context.MODE_PRIVATE).edit();
                editor.remove(getString(R.string.isDriver));
                editor.commit();

                Intent intent = new Intent(DriverMapsActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;

        }
        item.setChecked(true);
        mDrawerLayout.closeDrawers();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
            return;
        }
        mMap.setMyLocationEnabled(true);
        View locationButton = ((View) mMapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
// position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.setMargins(0, 0, 30, 60);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("donor_available");
        GeoFire geoFire = new GeoFire(reference);
        geoFire.removeLocation(uid);
    }

    @Override
    public void onLocationChanged(Location location) {

        mLastKnownLocation = location;
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        Log.e(LOG_TAG, "Latitude and longitude are : " + latLng);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17));

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("donor_available");
        GeoFire geoFire = new GeoFire(reference);
        geoFire.setLocation(uid, new GeoLocation(location.getLatitude(), location.getLongitude()));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(1000)
                .setFastestInterval(1000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, RC_PER);
    }
}