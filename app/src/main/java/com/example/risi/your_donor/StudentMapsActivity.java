package com.example.risi.your_donor;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Handler;
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

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StudentMapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar mToolbar;
    @BindView(R.id.student_drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_view_student)
    NavigationView mNavigationView;
//    @BindView(R.id.locate_bus_fab)
//    FloatingActionButton locateBus;

    public static final String LOG_TAG = StudentMapsActivity.class.getSimpleName();
    private static final int RC_PER = 2;

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastKnownLocation;
    private LatLng etaLocation;
    String myParentNode;
    private LatLng studentLocation;
    private LocationRequest mLocationRequest;
    private int radiusLocateBusRequest = 1;
    private boolean busFound = false;
    private String busDriverKey = "";
    private String donordet = "";
    private ActionBarDrawerToggle mDrawerToggle;
    private View mMapView;
    private int bus_num;
    private boolean driverFound = false;
    private SharedPreferences prefs;
    private Marker mBusMarker;
    private Marker mBusMarker1;
    private ProgressBar spinner;
    String name3="";
    int hospitall = 0;
    int busNo;
    int bloodbankk = 0;
    String don,no,fina="";
    DatabaseReference doornail;
    DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_maps);
        ButterKnife.bind(this);

//*****************************************************Adding timer for Donor activity***************************************************************
        Handler mHandler = new Handler();
        mHandler.postDelayed(() -> {
            Intent intent = new Intent(StudentMapsActivity.this, DriverMapsActivity.class);
            startActivity(intent);
            finish();

        }, 120000L);



//*****************************************************This is the details of the card**********************************************************************************




//

//        Bundle bundle = getIntent().getExtras();
//         hospitall = bundle.getInt("hospital");
////        if(hospitall==1)
//        {
//            bus_num=111;
//
//        }
        // Toolbar :: Transparent
        mToolbar.setBackgroundColor(Color.TRANSPARENT);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Locate your Donor");

        Window window = this.getWindow();
        // Status bar :: Transparent
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(Color.TRANSPARENT);

        setupDrawerContent(mNavigationView);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mMapView = mapFragment.getView();

        // When the studentMapsActivity launches first time, we ask for bus number and then it
        // would be configurable in the settings activity.
        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean firstTime = prefs.getBoolean(getString(R.string.student_maps_first_time_launch), true);
        if (firstTime) {

            AlertDialog.Builder metadialogBuilder = new AlertDialog.Builder(StudentMapsActivity.this);
            metadialogBuilder.setTitle(getString(R.string.selectBusTitle))
                    .setItems(R.array.bus_numbers, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
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
                            ref.setValue("Acceptor");
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putBoolean(getString(R.string.student_maps_first_time_launch), false);
                            editor.putInt(getString(R.string.bus_no), bus_num);
                            editor.apply();
                        }
                    });
            AlertDialog dialog = metadialogBuilder.create();
            dialog.show();
            Log.e(LOG_TAG, "Blood Group selected by user is : " + bus_num);


            //
        }
//********************************************************************************
//        locateBus = (FloatingActionButton) findViewById(R.id.locate_bus_fab);
        //********************************************************************
//        locateBus.setBackgroundColor(getResources().getColor(R.color.white));
//        locateBus.setImageResource(R.drawable.activity);

        spinner=findViewById(R.id.progressBar1);
                spinner.setVisibility(View.GONE);
                spinner.getLayoutParams().height = 30;

//
//        mLocateNearestBus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("studentRequestNearestBus");
//                GeoFire geoFire = new GeoFire(reference);
//                geoFire.setLocation(uid, new GeoLocation(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()));
//                studentLocation = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
//
//                getNearestBus();
//            }
//        });

//********************************************************************************************************************************************************
//        locateBus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(StudentMapsActivity.this, "Wait Please...", Toast.LENGTH_SHORT).show();
//                if (busDriverKey.isEmpty()){
//                    Toast.makeText(StudentMapsActivity.this, "Sorry, No Donor is not online!", Toast.LENGTH_LONG).show();
//                    return;
//                }
//
//
//
////224,244,250
////                Toast.makeText(StudentMapsActivity.this, busDriverKey, Toast.LENGTH_LONG).show();
//                DatabaseReference busLocation = FirebaseDatabase.getInstance().getReference().child("donor_available").child(busDriverKey).child("l");
//                busLocation.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        if (dataSnapshot.exists()) {
//                            List<Object> map = (List<Object>) dataSnapshot.getValue();
//                            double lat = 0;
//                            double lon = 0;
//                            if (map.get(0) != null) {
//                                lat = Double.parseDouble(map.get(0).toString());
//                            }
//
//                            if (map.get(1) != null) {
//                                lon = Double.parseDouble(map.get(1).toString());
//                            }
//
//                            LatLng busLocation = new LatLng(lat, lon);
//                            DatabaseReference def= dataSnapshot.getRef();
//                            myParentNode = def.getParent().getKey();
////                            Toast.makeText(StudentMapsActivity.this, myParentNode, Toast.LENGTH_SHORT).show();
//                            doornail = FirebaseDatabase.getInstance().getReference().child("Users").child("Donor").child(myParentNode );
//                            doornail.addValueEventListener(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(DataSnapshot dataSnapshot) {
//                                    name3 = dataSnapshot.getValue().toString();
////                                    Toast.makeText(StudentMapsActivity.this, name3, Toast.LENGTH_SHORT).show();
//                                }
//
//                                @Override
//                                public void onCancelled(DatabaseError databaseError) {
//
//                                }
//                            });
//                            if (mBusMarker != null) mBusMarker.remove();
//                            mBusMarker = mMap.addMarker(new MarkerOptions().position(busLocation).title(name3));
//
//
//
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
////
////                doornail = FirebaseDatabase.getInstance().getReference().child("Users").child("Donor").child(donordet);
////                doornail.addValueEventListener(new ValueEventListener() {
////                    @Override
////                    public void onDataChange(DataSnapshot dataSnapshot1) {
////                        String name3 = dataSnapshot1.child("name").getValue().toString();
//////                        String mobile3 = dataSnapshot.child("mobile").getValue().toString();
////                        Toast.makeText(StudentMapsActivity.this,name3,Toast.LENGTH_SHORT).show();
////                    }
////
////                    @Override
////                    public void onCancelled(DatabaseError databaseError) {
////                        Toast.makeText(StudentMapsActivity.this,"Error occured",Toast.LENGTH_SHORT).show();
////                    }
////                });
//
//            }
//        });
//*************************************************************************************************************************************************************
    }
//************************Hospital************************************

    //********************Hospital************************************


    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
//                    case R.id.eta:
//                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("request_eta");
//                        GeoFire geoFire = new GeoFire(reference);
//                        geoFire.setLocation(uid, new GeoLocation(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()));
//                        // getDriverLocation();
//
//                        break;



                        //here for bloodbank and hospital

                    case R.id.request_wait:
                        if (prefs.getInt(getString(R.string.bus_no), 0)==0){
                            Toast.makeText(StudentMapsActivity.this, "Please link your Blood Group first!", Toast.LENGTH_LONG).show();
                            break;
                        }
                        spinner.setVisibility(View.VISIBLE);
                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("request_wait");
                        GeoFire geofire = new GeoFire(ref);
                        geofire.setLocation(userId, new GeoLocation(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()));

                        etaLocation = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(etaLocation));
                        Toast.makeText(StudentMapsActivity.this, "Requesting...", Toast.LENGTH_SHORT).show();

                        busNo = prefs.getInt(getString(R.string.bus_no), 0);



                        if (busNo==0){
                            Toast.makeText(StudentMapsActivity.this, "Please add your Blood Group first in settings!", Toast.LENGTH_LONG).show();
                            break;
                        }
                        DatabaseReference busRef = FirebaseDatabase.getInstance().getReference().child("Save the Life").child(String.valueOf(busNo));
                        busRef.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                busDriverKey = dataSnapshot.getKey();
                                String isDriver = dataSnapshot.getValue(String.class);
                                if (isDriver.equals("Donor")){
                                    busDriverKey = dataSnapshot.getKey();
                                    driverFound = true;
                                    HashMap map = new HashMap();
                                    map.put("busDriverID", busDriverKey);
                                    ref.child(userId).updateChildren(map);
                                    Log.e(LOG_TAG, "keyis : " + busDriverKey);

                                    String studentId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    DatabaseReference driverRef = FirebaseDatabase.getInstance().getReference().child("Users")
                                            .child("Donor")
                                            .child(busDriverKey)
                                            .child("acceptorRequest");
                                    driverRef.setValue(studentId);




                                    //This is something to add map of all the user
                                    DatabaseReference busLocation1 = FirebaseDatabase.getInstance().getReference().child("donor_available").child(busDriverKey).child("l");
                                    busLocation1.addValueEventListener(new ValueEventListener() {
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

                                                LatLng busLocation1 = new LatLng(lat1, lon1);
                                                DatabaseReference def1= dataSnapshott.getRef();
                                                myParentNode = def1.getParent().getKey();
//                            Toast.makeText(StudentMapsActivity.this, myParentNode, Toast.LENGTH_SHORT).show();
                                                doornail = FirebaseDatabase.getInstance().getReference().child("Users").child("Donor").child(myParentNode );
                                                doornail.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        String name4 = dataSnapshot.getValue().toString();
                                                        String[] risi= name4.split(",");
                                                        don = risi[0].substring(1);
                                                        no = risi[1];

                                    Toast.makeText(StudentMapsActivity.this, don+" || "+no, Toast.LENGTH_SHORT).show();
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
//                                                if (mBusMarker1 != null) mBusMarker1.remove();
                                                if(!fina.equals(don+" "+no)){

                                                mBusMarker1 = mMap.addMarker(new MarkerOptions().position(busLocation1).title(don+" "+no));
                                                fina = don+" "+no;}


                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });



                                    Toast.makeText(StudentMapsActivity.this, busDriverKey, Toast.LENGTH_LONG).show();


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

                    case R.id.link_bus:

                        AlertDialog.Builder metadialogBuilder = new AlertDialog.Builder(StudentMapsActivity.this);
                        metadialogBuilder.setTitle(getString(R.string.selectBusTitle))
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
                                    DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("Save the Life").child(String.valueOf(bus_num)).child(uId);
                                    ref1.setValue("Acceptor");
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putInt(getString(R.string.bus_no), bus_num);
                                    editor.commit();
                                });
                        metadialogBuilder.show();
                        break;

                    case R.id.logout:
                        FirebaseAuth.getInstance().signOut();
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.remove(getString(R.string.isDriver));
                        editor.remove(getString(R.string.student_maps_first_time_launch));
                        editor.remove(getString(R.string.bus_no));
                        editor.commit();

                        Intent intent = new Intent(StudentMapsActivity.this, LoginActivity.class);
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

    private void getNearestBus() {
        DatabaseReference nearestBus = FirebaseDatabase.getInstance().getReference().child("donor_available");
        GeoFire geoFire = new GeoFire(nearestBus);
        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(studentLocation.latitude, studentLocation.longitude), radiusLocateBusRequest);
        geoQuery.removeAllListeners();
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if (!busFound) {
                    busFound = true;
                    busDriverKey = key;
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
                if (!busFound) {
                    radiusLocateBusRequest++;
                    getNearestBus();
                }

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
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
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17));

    }

    public void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, RC_PER);
    }
}
