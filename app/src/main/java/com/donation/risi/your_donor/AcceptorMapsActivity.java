package com.donation.risi.your_donor;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.preference.PreferenceManager;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AcceptorMapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    //For Tools in acceptor activity
    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar mToolbar;
    //For drawer in acceptor activity
    @BindView(R.id.acceptor_drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_view_student)
    NavigationView mNavigationView;

    public static final String LOG_TAG = AcceptorMapsActivity.class.getSimpleName();
    private static final int RC_PER = 2;

    private GoogleMap mMap;
    private double langgi,altti;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastKnownLocation;
    private LatLng etaLocation;
    String SS="";
    int key_no=0;

    //name,mobile,user,timing
    String detail1 = "";
    String detail2 = "";
    String detail3 = "";
    String detail4 = "";

    //for retrieval the parent node of the node
    String myParentNode;

    //for storing the location of acceptor
    private LatLng acceptorLocation;
    private LocationRequest mLocationRequest;
    private int radiusLocateBusRequest = 1;
    private boolean DonorFound = false;
    private String DonorKey = "";
    private String donordet = "";
    private ActionBarDrawerToggle mDrawerToggle;
    private View mMapView;
    private int group;
    private boolean donorFound = false;
    private SharedPreferences prefs;
    private Marker mBusMarker;
    private Marker mBusMarker1;
    private ProgressBar spinner;
    String name3="";
    int hospitall = 0;
    int GNo;
    int bloodbankk = 0;

    //For updating date and time...currently under progress
    String Stime="0",Sdate="0";
    int Itime=-1,Idate;
    String don,no,fina="",id;

    //for firebase Database
    DatabaseReference doornail;
    DatabaseReference ref;

    //To start map activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_maps);
        ButterKnife.bind(this);


//*****************************************************Adding timer for Donor activity***************************************************************
//        Handler mHandler = new Handler();
//        mHandler.postDelayed(() -> {
//            Intent intent = new Intent(AcceptorMapsActivity.this, DonorMapsActivity.class);
//            startActivity(intent);
//            finish();
//
//        }, 120000L);
//*****************************************************This is the details of the card**********************************************************************************

        //Activity costomization
//

        mToolbar.setBackgroundColor(Color.TRANSPARENT);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Locate your Donor");

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(Color.TRANSPARENT);

        setupDrawerContent(mNavigationView);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Interacting with google map
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mMapView = mapFragment.getView();
//        etaLocation = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(etaLocation));


        // When the DonorMapsActivity launches first time, we ask for group number and then it
        // would be configurable in the settings activity.
        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
//        boolean firstTime = prefs.getBoolean(getString(R.string.acceptor_maps_first_time_launch), true);
//        if (firstTime) {
//
//            AlertDialog.Builder metadialogBuilder = new AlertDialog.Builder(AcceptorMapsActivity.this);
//            metadialogBuilder.setTitle(getString(R.string.selectBusTitle))
//                    .setItems(R.array.Bgroup, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            switch (i) {
//                                case 0:
//                                    group = 1;
//                                    break;
//                                case 1:
//                                    group = 2;
//                                    break;
//                                case 2:
//                                    group = 3;
//                                    break;
//                                case 3:
//                                    group = 4;
//                                    break;
//                                case 4:
//                                    group = 5;
//                                    break;
//                                case 5:
//                                    group = 6;
//                                    break;
//                                case 6:
//                                    group = 7;
//                                    break;
//                                case 7:
//                                    group = 8;
//                                    break;
//                                case 8:
//                                    group = 9;
//                                    break;
//                                case 9:
//                                    group = 10;
//                                    break;
//
//                            }
//
//                            //Interaction with google firebase
//                            String uId = FirebaseAuth.getInstance().getCurrentUser().getUid(); //gives the unique id of current user
//                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Save the Life").child(String.valueOf(group)).child(uId);
////                            ref.setValue("Acceptor");
//                            SharedPreferences.Editor editor = prefs.edit();
//                            editor.putBoolean(getString(R.string.acceptor_maps_first_time_launch), false);
//                            editor.putInt(getString(R.string.bus_no), group);
//                            editor.apply();
//                        }
//                    });
//            AlertDialog dialog = metadialogBuilder.create();
//            dialog.show();
//
//            //To show in log
//            Log.e(LOG_TAG, "Blood Group selected by user is : " + group);
//

            //
//        }

//        DatabaseReference timing = FirebaseDatabase.getInstance().getReference().child("Time");
//        timing.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//            if(dataSnapshot!=null){
//                Stime = dataSnapshot.child("time").getValue().toString();
//                int kkk= 0;
//                while(kkk<=5) {
////                    Toast.makeText(AcceptorMapsActivity.this, "", Toast.LENGTH_SHORT).show();
//                    kkk++;
//                }
////
//            }
////
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
        DatabaseReference dating = FirebaseDatabase.getInstance().getReference().child("Time");
        dating.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot cSnapshot : dataSnapshot.getChildren()) {
                        Stime = dataSnapshot.child("time").getValue().toString();
                        Sdate = dataSnapshot.child("date").getValue().toString();
                    }}

//                Toast.makeText(AcceptorMapsActivity.this, Sdate+"\n "+Stime, Toast.LENGTH_SHORT).show();
//                Itime = Integer.getInteger(Stime);
//                if(dataSnapshot!=null){
//                Sdate = dataSnapshot.child("date").getValue().toString();
//                    int kkk= 0;
//                    while(kkk<=5){
////                        Toast.makeText(AcceptorMapsActivity.this, "", Toast.LENGTH_SHORT).show();
//                        kkk++;
//                    }

//                Toast.makeText(AcceptorMapsActivity.this, Stime, Toast.LENGTH_SHORT).show();
}
//            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



//
//        FirebaseDatabase.getInstance().getReference().child("Time").child("time");
//        FirebaseDatabase.getInstance().getReference().child("Date").child("date").setValue(date);

        spinner=findViewById(R.id.progressBar1);
                spinner.setVisibility(View.GONE);
                spinner.getLayoutParams().height = 30;

  }



    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                        //here for bloodbank and hospital

                        //if request blood key is pressed
                        case R.id.request_wait:

                            try {
                            ArrayList<String> keys = new ArrayList<String>();
                            ArrayList<String> detaill = new ArrayList<String>();
                            if (prefs.getInt(getString(R.string.bus_no), 0)==0){
                                Toast.makeText(AcceptorMapsActivity.this, "Please link your Blood Group first!", Toast.LENGTH_LONG).show();
                                break;
                            }
                            spinner.setVisibility(View.VISIBLE);
                            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("request_wait");
                            GeoFire geofire = new GeoFire(ref);
                            geofire.setLocation(userId, new GeoLocation(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()));

                            etaLocation = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                            langgi = mLastKnownLocation.getLongitude();
                            altti = mLastKnownLocation.getLatitude();

                            //Fod adding marker in google map
                            mMap.addMarker(new MarkerOptions().position(etaLocation));
                            Toast.makeText(AcceptorMapsActivity.this, "Requesting...", Toast.LENGTH_SHORT).show();

                                GNo = prefs.getInt(getString(R.string.bus_no), 0);


                            if (GNo==0){
                                Toast.makeText(AcceptorMapsActivity.this, "Please add your Blood Group first in settings!", Toast.LENGTH_LONG).show();
                                break;
                            }

                            //Database retrieval
                            DatabaseReference busRef = FirebaseDatabase.getInstance().getReference().child("Save the Life").child(String.valueOf(GNo));
                            busRef.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    DonorKey = dataSnapshot.getKey();
                                    String isDriver = dataSnapshot.getValue(String.class);
                                    if (isDriver.equals("Donor")){
                                        DonorKey = dataSnapshot.getKey();
                                        donorFound = true;
                                        HashMap map = new HashMap();
                                        map.put("busDriverID", DonorKey);
                                        ref.child(userId).updateChildren(map);
                                        Log.e(LOG_TAG, "keyis : " + DonorKey);

                                        String studentId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                        keys.add(DonorKey);
                                        key_no++;
                                        DatabaseReference driverRef = FirebaseDatabase.getInstance().getReference().child("Users")
                                                .child("Donor")
                                                .child(DonorKey)
                                                .child("acceptorRequest");
                                        driverRef.setValue(studentId);

                                        try {
                                            DatabaseReference detail = FirebaseDatabase.getInstance().getReference().child("Users").child("Donor").child(DonorKey);
                                            detail.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if(dataSnapshot!=null){
                                                    detail1 = dataSnapshot.child("name").getValue().toString();
                                                    detail2 = dataSnapshot.child("mobile").getValue().toString();
                                                    detail3 = dataSnapshot.child("user_n").getValue().toString();
                                                    detail4 = dataSnapshot.child("timing").getValue().toString();
//                                                    if(Stime!=null){
//                                                    Itime = Integer.getInteger(Stime);}
        //                                                Toast.makeText(AcceptorMapsActivity.this, detail1+" || "+detail2+" || "+detail3, Toast.LENGTH_SHORT).show();
                                                        while(detail2==null){ Toast.makeText(AcceptorMapsActivity.this, "", Toast.LENGTH_SHORT).show();}

                                                        //FOr day and night donor availiblity
                                                        Itime = Integer.parseInt(Stime);
                                                        if(detail4.equals("day")){
                                                            if((Itime>=20 || Itime<=8))
                                                                detaill.add("Currently Unavailable");
                                                            else
                                                                detaill.add(detail3 + " || " + detail1 + " || " + detail2);
                                                        }
                                                        else if(detail4.equals("all")){
                                                            detaill.add(detail3 + " || " + detail1 + " || " + detail2);
        //                                                Toast.makeText(AcceptorMapsActivity.this, "started", Toast.LENGTH_SHORT).show();
                                                        }



                                                }}

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });


                                            //finding the available donor and storing their information
                                            final int[] count = {0};
                                            for(String sss:keys){
//                                                Toast.makeText(AcceptorMapsActivity.this, sss, Toast.LENGTH_SHORT).show();

                                                DatabaseReference marker = FirebaseDatabase.getInstance().getReference().child("donor_available").child(sss).child("l");
        //                                                while(marker.toString()==null){ Toast.makeText(AcceptorMapsActivity.this, "", Toast.LENGTH_SHORT).show();}
                                                marker.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshott) {

                                                            if (dataSnapshott != null) {
//                                                                Toast.makeText(AcceptorMapsActivity.this, "ended", Toast.LENGTH_SHORT).show();

                                                                double lat_value = Double.parseDouble(dataSnapshott.child("0").getValue().toString());
                                                                double long_value = Double.parseDouble(dataSnapshott.child("1").getValue().toString());
            //
                                                                if((Math.abs(langgi-long_value)<=0.02) && (Math.abs(altti-lat_value)<=0.02)){
                                                                LatLng DLocation1 = new LatLng(lat_value, long_value);
            //                                                    mBusMarker1.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_icon));
                                                                mBusMarker1 = mMap.addMarker(new MarkerOptions().position(DLocation1).title(detaill.get(count[0])));

            //                                                    mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_icon)));
            //                                                    mBusMarker1.setIcon(R.drawable.ic_icon);
                                                                count[0]++;
            //                                                        Toast.makeText(AcceptorMapsActivity.this, lat1+" || "+lon1, Toast.LENGTH_SHORT).show();
                                                            }}


                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });

                                            }
                                        } catch (NumberFormatException e) {
                                            Toast.makeText(AcceptorMapsActivity.this, "Donor Not Found...", Toast.LENGTH_SHORT).show();
                                        }


                                        //This is something to add map of all the user
                                        DatabaseReference DLocation1 = FirebaseDatabase.getInstance().getReference().child("donor_available").child(DonorKey).child("l");
                                        DLocation1.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshott) {
                                                if (dataSnapshott.exists()) {
                                                    List<Object> map = (List<Object>) dataSnapshott.getValue();
                                                    double lat1 = 0;
                                                    double lon1 = 0;
                                                    if (map.get(0) != null) {
                                                        lat1 = Double.parseDouble(map.get(0).toString());
                                                    }

                                                    if (map.get(1) != null) {
                                                        lon1 = Double.parseDouble(map.get(1).toString());
                                                    }

                                                    LatLng DLocation1 = new LatLng(lat1, lon1);
                                                    DatabaseReference def1= dataSnapshott.getRef();
                                                    myParentNode = def1.getParent().getKey();
    //                            Toast.makeText(AcceptorMapsActivity.this, myParentNode, Toast.LENGTH_SHORT).show();
                                                    doornail = FirebaseDatabase.getInstance().getReference().child("Users").child("Donor").child(myParentNode );
                                                    doornail.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            String name4 = dataSnapshot.getValue().toString();
                                                            String[] risi= name4.split(",");
                                                            don = risi[0].substring(1);
                                                            no = risi[1];
                                                            id = risi[2];

    //                                    Toast.makeText(AcceptorMapsActivity.this, don+" || "+no+" || "+id, Toast.LENGTH_SHORT).show();
                                         SS =  don+" || "+no+" || "+id;
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });
    //                                                if (mBusMarker1 != null) mBusMarker1.remove();
                                                    if(!fina.equals(SS)){
    //                                                mBusMarker1 = mMap.addMarker(new MarkerOptions().position(DLocation1).title(don+" "+no+" "+id));
                                                    fina = SS;}


                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });



    //                                    Toast.makeText(AcceptorMapsActivity.this, DonorKey, Toast.LENGTH_LONG).show();


                                        DatabaseReference locationAdd = FirebaseDatabase.getInstance().getReference().child("Users").child("Acceptor").child(studentId);
                                        GeoFire geofire = new GeoFire(locationAdd);
                                        geofire.setLocation(studentId, new GeoLocation(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()));
                                        return;
                                    }
                                    spinner.setVisibility(View.GONE);
                                }

                                @Override
                                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                }

                                @Override
                                public void onChildRemoved(DataSnapshot dataSnapshot) {

                                }

                                @Override
                                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                            break;
                    } catch (NumberFormatException e) {
                        Toast.makeText(AcceptorMapsActivity.this, "Network Error...", Toast.LENGTH_LONG).show();
                    }

                    //for linking blood group
                    case R.id.link_bus:

                        AlertDialog.Builder metadialogBuilder = new AlertDialog.Builder(AcceptorMapsActivity.this);
                        metadialogBuilder.setTitle(getString(R.string.selectBusTitle))
                                .setItems(R.array.Bgroup, (dialogInterface, i) -> {
                                    switch (i) {
                                        case 0:
                                            group = 1;
                                            break;
                                        case 1:
                                            group = 2;
                                            break;
                                        case 2:
                                            group = 3;
                                            break;
                                        case 3:
                                            group = 4;
                                            break;
                                        case 4:
                                            group = 5;
                                            break;
                                        case 5:
                                            group = 6;
                                            break;
                                        case 6:
                                            group = 7;
                                            break;
                                        case 7:
                                            group = 8;
                                            break;
                                        case 8:
                                            group = 9;
                                            break;
                                        case 9:
                                            group = 10;
                                            break;

                                    }

                                    String uId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                                    DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("Save the Life").child(String.valueOf(bus_num)).child(uId);
//                                    ref1.setValue("Acceptor");
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putInt(getString(R.string.bus_no), group);
                                    editor.commit();
                                });
                        metadialogBuilder.show();
                        break;

                    case R.id.logout:
                        FirebaseAuth.getInstance().signOut();
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.remove(getString(R.string.isDriver));
                        editor.remove(getString(R.string.acceptor_maps_first_time_launch));
                        editor.remove(getString(R.string.bus_no));
                        editor.commit();

                        Intent intent = new Intent(AcceptorMapsActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

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

    private void getNearestDonor() {
        DatabaseReference nearesDonor = FirebaseDatabase.getInstance().getReference().child("donor_available");
        GeoFire geoFire = new GeoFire(nearesDonor);
        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(acceptorLocation.latitude, acceptorLocation.longitude), radiusLocateBusRequest);
        geoQuery.removeAllListeners();
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if (!DonorFound) {
                    DonorFound = true;
                    DonorKey = key;
                }
            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {
                if (!DonorFound) {
                    radiusLocateBusRequest++;
                    getNearestDonor();
                }

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
    public void onConnected(@Nullable Bundle bundle) {

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(1000)
                .setFastestInterval(1000)
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

    @Override
    public void onLocationChanged(Location location) {

        mLastKnownLocation = location;
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        Log.e(LOG_TAG, "Latitude and longitude are : " + latLng);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo((float) 14));

    }

    public void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, RC_PER);
    }

    public void selectgrp(View view) {
        AlertDialog.Builder metadialogBuilder = new AlertDialog.Builder(AcceptorMapsActivity.this);
        metadialogBuilder.setTitle(getString(R.string.selectBusTitle))
                .setItems(R.array.Bgroup, (dialogInterface, i) -> {
                    switch (i) {
                        case 0:
                            group = 1;
                            locate();
                            break;
                        case 1:
                            group = 2;
                            locate();
                            break;
                        case 2:
                            group = 3;
                            locate();
                            break;
                        case 3:
                            group = 4;
                            locate();
                            break;
                        case 4:
                            group = 5;
                            locate();
                            break;
                        case 5:
                            group = 6;
                            locate();
                            break;
                        case 6:
                            group = 7;
                            locate();
                            break;
                        case 7:
                            group = 8;
                            locate();
                            break;
                        case 8:
                            group = 9;
                            locate();
                            break;
                        case 9:
                            group = 10;
                            locate();
                            break;

                    }


                });
        metadialogBuilder.show();


    }

    private void locate() {
        String uId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                                    DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("Save the Life").child(String.valueOf(bus_num)).child(uId);
//                                    ref1.setValue("Acceptor");
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(getString(R.string.bus_no), group);
        editor.commit();
        final int[] counter = {0};
        try {
            ArrayList<String> keys = new ArrayList<String>();
            ArrayList<String> detaill = new ArrayList<String>();
            if (prefs.getInt(getString(R.string.bus_no), 0)==0){
                Toast.makeText(AcceptorMapsActivity.this, "Please link your Blood Group first!", Toast.LENGTH_LONG).show();

            }
            spinner.setVisibility(View.VISIBLE);
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("request_wait");
            GeoFire geofire = new GeoFire(ref);
            geofire.setLocation(userId, new GeoLocation(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()));

            etaLocation = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
            langgi = mLastKnownLocation.getLongitude();
            altti = mLastKnownLocation.getLatitude();

            //Fod adding marker in google map
            mMap.addMarker(new MarkerOptions().position(etaLocation));
            Toast.makeText(AcceptorMapsActivity.this, "Requesting...", Toast.LENGTH_SHORT).show();

            GNo = prefs.getInt(getString(R.string.bus_no), 0);


            if (GNo==0){
                Toast.makeText(AcceptorMapsActivity.this, "Please add your Blood Group first in settings!", Toast.LENGTH_LONG).show();

            }

            //Database retrieval
            DatabaseReference busRef = FirebaseDatabase.getInstance().getReference().child("Save the Life").child(String.valueOf(GNo));
            busRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    DonorKey = dataSnapshot.getKey();
                    String isDriver = dataSnapshot.getValue(String.class);
                    if (isDriver.equals("Donor")){
                        DonorKey = dataSnapshot.getKey();
                        donorFound = true;
                        HashMap map = new HashMap();
                        map.put("busDriverID", DonorKey);
                        ref.child(userId).updateChildren(map);
                        Log.e(LOG_TAG, "keyis : " + DonorKey);

                        String studentId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        keys.add(DonorKey);
                        counter[0]++;
                        key_no++;
                        DatabaseReference driverRef = FirebaseDatabase.getInstance().getReference().child("Users")
                                .child("Donor")
                                .child(DonorKey)
                                .child("acceptorRequest");
                        driverRef.setValue(studentId);

                        try {
                            DatabaseReference detail = FirebaseDatabase.getInstance().getReference().child("Users").child("Donor").child(DonorKey);
                            detail.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot!=null){
                                        detail1 = dataSnapshot.child("name").getValue().toString();
                                        detail2 = dataSnapshot.child("mobile").getValue().toString();
                                        detail3 = dataSnapshot.child("user_n").getValue().toString();
                                        detail4 = dataSnapshot.child("timing").getValue().toString();
//                                                    if(Stime!=null){
//                                                    Itime = Integer.getInteger(Stime);}
                                        //                                                Toast.makeText(AcceptorMapsActivity.this, detail1+" || "+detail2+" || "+detail3, Toast.LENGTH_SHORT).show();
                                        while(detail2==null){ Toast.makeText(AcceptorMapsActivity.this, "", Toast.LENGTH_SHORT).show();}

                                        //FOr day and night donor availiblity
                                        Itime = Integer.parseInt(Stime);
                                        if(detail4.equals("day")){
                                            if((Itime>=20 || Itime<=8))
                                                detaill.add("Currently Unavailable");
                                            else
                                                detaill.add(detail3 + " || " + detail1 + " || " + detail2);
                                        }
                                        else if(detail4.equals("all")){
                                            detaill.add(detail3 + " || " + detail1 + " || " + detail2);
                                            //                                                Toast.makeText(AcceptorMapsActivity.this, "started", Toast.LENGTH_SHORT).show();
                                        }



                                    }}

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                            //finding the available donor and storing their information
                            final int[] count = {0};
                            for(String sss:keys){
//                                                Toast.makeText(AcceptorMapsActivity.this, sss, Toast.LENGTH_SHORT).show();

                                DatabaseReference marker = FirebaseDatabase.getInstance().getReference().child("donor_available").child(sss).child("l");
                                //                                                while(marker.toString()==null){ Toast.makeText(AcceptorMapsActivity.this, "", Toast.LENGTH_SHORT).show();}
                                marker.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshott) {

                                        if (dataSnapshott != null) {
//                                                                Toast.makeText(AcceptorMapsActivity.this, "ended", Toast.LENGTH_SHORT).show();

                                            double lat_value = Double.parseDouble(dataSnapshott.child("0").getValue().toString());
                                            double long_value = Double.parseDouble(dataSnapshott.child("1").getValue().toString());
                                            //
                                            if((Math.abs(langgi-long_value)<=0.02) && (Math.abs(altti-lat_value)<=0.02)){
                                                LatLng DLocation1 = new LatLng(lat_value, long_value);
                                                //                                                    mBusMarker1.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_icon));
                                                mBusMarker1 = mMap.addMarker(new MarkerOptions().position(DLocation1).title(detaill.get(count[0])));

                                                //                                                    mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_icon)));
                                                //                                                    mBusMarker1.setIcon(R.drawable.ic_icon);
                                                count[0]++;
                                                //                                                        Toast.makeText(AcceptorMapsActivity.this, lat1+" || "+lon1, Toast.LENGTH_SHORT).show();
                                            }else
                                                count[0]++;
                                        }


                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }
                        } catch (NumberFormatException e) {
                            Toast.makeText(AcceptorMapsActivity.this, "Donor Not Found...", Toast.LENGTH_SHORT).show();
                        }


                        //This is something to add map of all the user
                        DatabaseReference DLocation1 = FirebaseDatabase.getInstance().getReference().child("donor_available").child(DonorKey).child("l");
                        DLocation1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshott) {
                                if (dataSnapshott.exists()) {
                                    List<Object> map = (List<Object>) dataSnapshott.getValue();
                                    double lat1 = 0;
                                    double lon1 = 0;
                                    if (map.get(0) != null) {
                                        lat1 = Double.parseDouble(map.get(0).toString());
                                    }

                                    if (map.get(1) != null) {
                                        lon1 = Double.parseDouble(map.get(1).toString());
                                    }

                                    LatLng DLocation1 = new LatLng(lat1, lon1);
                                    DatabaseReference def1= dataSnapshott.getRef();
                                    myParentNode = def1.getParent().getKey();
                                    //                            Toast.makeText(AcceptorMapsActivity.this, myParentNode, Toast.LENGTH_SHORT).show();
                                    doornail = FirebaseDatabase.getInstance().getReference().child("Users").child("Donor").child(myParentNode );
                                    doornail.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            String name4 = dataSnapshot.getValue().toString();
                                            String[] risi= name4.split(",");
                                            don = risi[0].substring(1);
                                            no = risi[1];
                                            id = risi[2];

                                            //                                    Toast.makeText(AcceptorMapsActivity.this, don+" || "+no+" || "+id, Toast.LENGTH_SHORT).show();
                                            SS =  don+" || "+no+" || "+id;
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                    //                                                if (mBusMarker1 != null) mBusMarker1.remove();
                                    if(!fina.equals(SS)){
                                        //                                                mBusMarker1 = mMap.addMarker(new MarkerOptions().position(DLocation1).title(don+" "+no+" "+id));
                                        fina = SS;}


                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });



                        //                                    Toast.makeText(AcceptorMapsActivity.this, DonorKey, Toast.LENGTH_LONG).show();


                        DatabaseReference locationAdd = FirebaseDatabase.getInstance().getReference().child("Users").child("Acceptor").child(studentId);
                        GeoFire geofire = new GeoFire(locationAdd);
                        geofire.setLocation(studentId, new GeoLocation(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()));
                        return;
                    }
                    spinner.setVisibility(View.GONE);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (NumberFormatException e) {
            Toast.makeText(AcceptorMapsActivity.this, "Network Error...", Toast.LENGTH_LONG).show();
        }
    }
}

