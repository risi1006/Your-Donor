package com.donation.risi.your_donor;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import android.graphics.drawable.AnimationDrawable;

import com.firebase.client.Firebase;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    private EditText r1, r2;
    private Spinner spinner,spinner1;
    String item,item1;
    String value1,value2;
    Button forget;
    String latti,longgi;
    int total;
    User user1 = new User();
    private CoordinatorLayout coordinatorLayout;
    private AnimationDrawable animationDrawable;
    DatabaseReference user_no;
    int no_user=0;
    String totall="null";
    @BindView(R.id.text_input_layout_email)
    TextInputLayout emailWrapper;
    @BindView(R.id.text_input_layout_password)
    TextInputLayout passwordWrapper;
    @BindView(R.id.btnLogin)
    Button login;
    @BindView(R.id.Loginn)
    Button loginn;
    @BindView(R.id.btnregister)
    Button register;
    @BindView(R.id.btnregisterr)
    Button registerr;
//    @BindView(R.id.chooser_spinner)
//    Spinner userType;

    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;
    public static final String LOG_TAG = LoginActivity.class.getSimpleName();
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private ProgressDialog mProgress;
    DatabaseReference reference;
    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intentt = getIntent();
        latti = intentt.getStringExtra("latti");
        longgi = intentt.getStringExtra("longgi");
        super.onCreate(savedInstanceState);
        try {
            loadLocale();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        forget = (Button)findViewById(R.id.forget);
        spinner = (Spinner)findViewById(R.id.spinner);
        spinner1 = (Spinner)findViewById(R.id.spinner1);
        r1 = (EditText) findViewById(R.id.name);
        r2 = (EditText) findViewById(R.id.contact);
        Firebase.setAndroidContext(getApplicationContext());

        List<String> categories = new ArrayList<>();
        List<String> categories1 = new ArrayList<>();
        categories.add(0,"Select your blood group");
        categories.add("A+");
        categories.add("A-");
        categories.add("B+");
        categories.add("B-");
        categories.add("AB+");
        categories.add("AB-");
        categories.add("O+");
        categories.add("O-");
        categories.add("Blood Bank");

        categories1.add(0,"Contact Timing");
        categories1.add("Any Time");
        categories1.add("Only in day time");

        ArrayAdapter<String> dataAdapter;
        ArrayAdapter<String> dataAdapter1;
        dataAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,categories);
        dataAdapter1 = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,categories1);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).equals("Select your blood group"))
                {

                }
                else
                {
                    item = parent.getItemAtPosition(position).toString();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner1.setAdapter(dataAdapter1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).equals("Contact timing"))
                {

                }
                else
                {
                    item1 = parent.getItemAtPosition(position).toString();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });








//*****************************Finding  no of users******************************************************************************

        user_no = FirebaseDatabase.getInstance().getReference();
//*****************************End of find **************************************************************************************

        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo nInfo = cm.getActiveNetworkInfo();
        if(nInfo != null && nInfo.isConnected()){
        }
        else {
            AlertDialog.Builder a_builder = new AlertDialog.Builder(LoginActivity.this) ;
            a_builder.setMessage("Please enable internet connection !!!")
                    .setCancelable(false)
                    .setPositiveButton("Settings", (dialogInterface, i) -> {


                        Intent in = new Intent(android.provider.Settings.ACTION_DATA_ROAMING_SETTINGS  );
                        startActivity(in);

                    })
                    .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel());
            AlertDialog alert = a_builder.create();
            alert.setTitle("No Internet Connection");
            alert.show();
        }

        mProgress = new ProgressDialog(LoginActivity.this, R.style.AppTheme_Dark_Dialog);
                mProgress.setMessage("Authenticating...");
                mProgress.setCancelable(false);
               mProgress.setIndeterminate(true);

        // overridePendingTransition(R.anim.slide_in_right, R.anim.stay_in_place);

        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();




            if (user != null) {
                    Intent intent = new Intent(LoginActivity.this, dashboard.class);
                    startActivity(intent);
                    finish();
            }
        };

        login.setOnClickListener(view -> {
            try {
                mProgress.show();
                hideKeyBoard();
                emailInput = (TextInputEditText) emailWrapper.getEditText();
                passwordInput = (TextInputEditText) passwordWrapper.getEditText();
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();

                SharedPreferences.Editor editor = getPreferences(Context.MODE_PRIVATE).edit();
                editor.putBoolean(getString(R.string.isDriver), true);
                editor.apply();
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()){
                           Toast.makeText(LoginActivity.this ,"No Account found with these credentials." ,Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                            Bundle extras = new Bundle();
                            extras.putString("latti", latti);
                            extras.putString("longgi", longgi);
                            intent.putExtras(extras);
                            startActivity(intent);
                            finish();
                        }



                    }
                });
            } catch (Exception e) {
                Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                Bundle extras = new Bundle();
                extras.putString("latti", latti);
                extras.putString("longgi", longgi);
                intent.putExtras(extras);
                startActivity(intent);
                finish();
            }

        });


        registerr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r1.setVisibility(View.VISIBLE);
                r2.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.VISIBLE);
                spinner1.setVisibility(View.VISIBLE);
                login.setVisibility(View.GONE);
                forget.setVisibility(View.GONE);
                registerr.setVisibility(View.GONE);
                register.setVisibility(View.VISIBLE);
                loginn.setVisibility(View.VISIBLE);

            }
        });

        loginn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                r1.setVisibility(ViewPagerAdapter.INVISIBLE);
//                r2.setVisibility(ViewPagerAdapter.INVISIBLE);
                r1.setVisibility(View.GONE);
                r2.setVisibility(View.GONE);
                spinner.setVisibility(View.GONE);
                spinner1.setVisibility(View.GONE);
                forget.setVisibility(View.VISIBLE);
                login.setVisibility(View.VISIBLE);
                registerr.setVisibility(View.VISIBLE);
                register.setVisibility(View.GONE);
                loginn.setVisibility(View.GONE);
            }
        });

        register.setOnClickListener(view -> {

                DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("Users").child("Donor");
                reff.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                            total = (int) dataSnapshot.getChildrenCount();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            try {
                if(item.equals("A+")) value1 = "1";
                else if(item.equals("A-")) value1 = "2";
                else if(item.equals("B+")) value1 = "3";
                else if(item.equals("B-")) value1 = "4";
                else if(item.equals("AB+")) value1 = "5";
                else if(item.equals("AB-")) value1 = "6";
                else if(item.equals("O+")) value1 = "7";
                else if(item.equals("O-")) value1 = "8";
                else if(item.equals("Blood Bank")) value1 = "9";


                value2 = "day";
                if(item1.equals("Any Time")) value2 = "all";
                else if(item1.equals("Only in day time")) value2 = "day";
                User user1 = new User();
                mProgress.show();
                hideKeyBoard();
                emailInput = (TextInputEditText) emailWrapper.getEditText();
                passwordInput = (TextInputEditText) passwordWrapper.getEditText();
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();

                String name1 = r1.getText().toString();
                String contact1 = r2.getText().toString();

                if(name1==""|| contact1==""){
                    Toast.makeText(this,"Enter all the fields", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    user1.setName(name1);
                    user1.setMobile(contact1);
//                    user1.setGroup(String.valueOf(value1));
                }
                SharedPreferences.Editor editor = getPreferences(Context.MODE_PRIVATE).edit();
                editor.putBoolean(getString(R.string.isDriver), true);
                editor.apply();


                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, task -> {
                    if (!task.isSuccessful()) {

                        Toast.makeText(LoginActivity.this, "Sign up error!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                        Bundle extras = new Bundle();
                        extras.putString("latti", latti);
                        extras.putString("longgi", longgi);
                        intent.putExtras(extras);
                        startActivity(intent);
                        finish();
                    } else {
                        String user_id = mAuth.getCurrentUser().getUid();

                            DatabaseReference user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Donor").child(user_id);
                                      user_db.setValue(true);

    //****************************************user no***********************************************************************************
                        int curr = total+1;
                        FirebaseDatabase.getInstance().getReference().child("1total").setValue(String.valueOf(curr));
                        user1.setUser_n(String.valueOf(curr));
                        FirebaseDatabase.getInstance().getReference().child("Users").child("Donor").child(user_id).setValue(user1);
                        FirebaseDatabase.getInstance().getReference().child("Users").child("Donor").child(user_id).child("group").setValue(value1);
                        FirebaseDatabase.getInstance().getReference().child("Users").child("Donor").child(user_id).child("timing").setValue(value2);
                        user_no.child("user_no").child(String.valueOf(curr)).setValue(user_id);
//                        user_no.child("user_no").child(user_id).setValue("NA");
                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("donor_available").child(userId).child("l");
                            ref.child("0").setValue(latti);
                            ref.child("1").setValue(longgi);

    //****************************************user no********************************************************************************

                        String uIdd = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("Save the Life").child(value1).child(uIdd);
                        ref1.setValue("Donor");


                        Intent intent1 = new Intent(this,dashboard.class);
                        startActivity(intent1);
                    }
                });
            } catch (Exception e) {
                Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                Bundle extras = new Bundle();
                extras.putString("latti", latti);
                extras.putString("longgi", longgi);
                intent.putExtras(extras);
                startActivity(intent);
                finish();
            }
        });



    }

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

    @Override
    public void finish() {
        super.finish();
        //LoginActivity.this.overridePendingTransition(0, R.anim.slide_out_right);
    }

    private void hideKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            view.clearFocus();
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (animationDrawable != null && !animationDrawable.isRunning()) {
            // start the animation
            animationDrawable.start();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (animationDrawable != null && animationDrawable.isRunning()) {
            // stop the animation
            animationDrawable.stop();
        }
    }

    public void userE(View view) {
        Intent intentt1 = new Intent(this,HUseE.class);
        startActivity(intentt1);
    }

    public void userH(View view) {
        Toast.makeText(this, latti+" \n"+longgi, Toast.LENGTH_SHORT).show();
        final String[] listItems = {"English","Hindi","Punjabi","Tamil"};
        AlertDialog.Builder mbuilder = new AlertDialog.Builder(LoginActivity.this);
        mbuilder.setTitle("Choose Language...");
        mbuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if(i==0)
                {
                    setLocale("en");
                    recreate();
                }
                else if(i==1)
                {
                    setLocale("hi");
                    recreate();
                }
                else if(i==2)
                {
                    setLocale("pa");
                    recreate();
                }
                else if(i==3)
                {
                    setLocale("ta");
                    recreate();
                }

                dialog.dismiss();

            }
        });
        AlertDialog mdialog = mbuilder.create();
        mdialog.show();


    }
    public void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration,getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor= getSharedPreferences("Settings",MODE_PRIVATE).edit();
        editor.putString("My_Lang",lang);
        editor.apply();
    }

    public void loadLocale(){
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang","");
        setLocale(language);
    }

    public void forget(View view) {
            Intent intent = new Intent(this,profile.class);
            startActivity(intent);
    }
}

