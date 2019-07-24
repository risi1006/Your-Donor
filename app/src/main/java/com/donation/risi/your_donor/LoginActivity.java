package com.example.risi.your_donor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    private EditText r1, r2;
    private Spinner spinner;
    String item;
    String value1;
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
    @BindView(R.id.btnregister)
    Button register;
//    @BindView(R.id.chooser_spinner)
//    Spinner userType;

    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;
    public static final String LOG_TAG = LoginActivity.class.getSimpleName();
    private int bus_num = 0;
    //Firebase utils
    Integer result=0;
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        spinner = (Spinner)findViewById(R.id.spinner);
        r1 = (EditText) findViewById(R.id.name);
        r2 = (EditText) findViewById(R.id.contact);
        Firebase.setAndroidContext(getApplicationContext());

        List<String> categories = new ArrayList<>();
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

        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,categories);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

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





//*****************************Finding  no of users******************************************************************************

        user_no = FirebaseDatabase.getInstance().getReference();
//        user_no.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if(dataSnapshot.exists())
//                {
//                    totall = dataSnapshot.child("total").getValue().toString();
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

//        DatabaseReference totalll = FirebaseDatabase.getInstance().getReference().child("1total");
//        totalll.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//             totall = dataSnapshot.getValue().toString();
//                Toast.makeText(LoginActivity.this ,"1 "+dataSnapshot.toString(),Toast.LENGTH_SHORT).show();
//                Toast.makeText(LoginActivity.this ,"2 "+dataSnapshot.getValue().toString(),Toast.LENGTH_SHORT).show();
//                Toast.makeText(LoginActivity.this,"3 "+totall,Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//        Toast.makeText(LoginActivity.this ,totall,Toast.LENGTH_SHORT).show();

//        user_no = FirebaseDatabase.getInstance().getReference().child("1total");
//        user_no.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                if(dataSnapshot.exists())
//                {
//                    total = dataSnapshot.getValue().toString();
//                }
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
////        });
//        while (totall.equals("null")){
//            Toast.makeText(LoginActivity.this,"waiting... ",Toast.LENGTH_SHORT).show();
//        }
////        Toast.makeText(LoginActivity.this ,totall,Toast.LENGTH_SHORT).show();
////        result = Integer.parseInt(total);
//          result = Integer.valueOf(totall);
//        Toast.makeText(LoginActivity.this,String.valueOf(result),Toast.LENGTH_SHORT).show();
//        user_no = FirebaseDatabase.getInstance().getReference();
//*****************************End of find **************************************************************************************


//        ConnectivityManager cm = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo nInfo = cm.getActiveNetworkInfo();
        if(nInfo != null && nInfo.isConnected()){
            Toast.makeText(LoginActivity.this ,"just say...All is well!" ,Toast.LENGTH_SHORT).show();
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

/*
        // init coordinatorLayout
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.activity_login);
        // initializing animation drawable by getting background from constraint layout
        animationDrawable = (AnimationDrawable) coordinatorLayout.getBackground();

        // setting enter fade animation duration to 5 seconds
        animationDrawable.setEnterFadeDuration(5000);

        // setting exit fade animation duration to 2 seconds
        animationDrawable.setExitFadeDuration(2000);
*/
        mProgress = new ProgressDialog(LoginActivity.this, R.style.AppTheme_Dark_Dialog);
                mProgress.setMessage("Authenticating...");
                mProgress.setCancelable(false);
               mProgress.setIndeterminate(true);

        // overridePendingTransition(R.anim.slide_in_right, R.anim.stay_in_place);

        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();



            //changed was done
            if (user != null) {
//                Boolean ut = getPreferences(Context.MODE_PRIVATE).getBoolean(getString(R.string.isDriver), false);
//                if (ut) {
                    Intent intent = new Intent(LoginActivity.this, DonorMapsActivity.class);
//                Intent intent = new Intent(LoginActivity.this, DonorMapsActivity.class);
                    startActivity(intent);
                    finish();
//                } else {
//                    Intent intent = new Intent(LoginActivity.this, AcceptorMapsActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
                //up to here
            }
        };

//        switchToSignUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(LoginActivity.this, SignUPActivity.class);
//                startActivity(intent);
//            }
//        });

        login.setOnClickListener(view -> {
            mProgress.show();
            hideKeyBoard();
            emailInput = (TextInputEditText) emailWrapper.getEditText();
            passwordInput = (TextInputEditText) passwordWrapper.getEditText();
            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();



            /**
             * Saving detail of the user in a shared preference file i.e he is driver or not
             * and will check from that shared pref while logging in!
             */
            //change was done from here to here
//            if (userType.getSelectedItem().toString().equals("Driver")) {
                SharedPreferences.Editor editor = getPreferences(Context.MODE_PRIVATE).edit();
                editor.putBoolean(getString(R.string.isDriver), true);
                editor.apply();
//                editor.commit();
//            }else{
//                SharedPreferences.Editor editor = getPreferences(Context.MODE_PRIVATE).edit();
//                editor.remove(getString(R.string.isDriver));
////                editor.commit();
//                editor.apply();
//            }




            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()){
                       Toast.makeText(LoginActivity.this ,"Sign in error!" ,Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }



                }
            });
        });


        register.setOnClickListener(view -> {
            if(item.equals("A+")) value1 = "1";
            else if(item.equals("A-")) value1 = "2";
            else if(item.equals("B+")) value1 = "3";
            else if(item.equals("B-")) value1 = "4";
            else if(item.equals("AB+")) value1 = "5";
            else if(item.equals("AB-")) value1 = "6";
            else if(item.equals("O+")) value1 = "7";
            else if(item.equals("O-")) value1 = "8";
            else if(item.equals("Blood Bank")) value1 = "9";
            User user1 = new User();
            mProgress.show();
            hideKeyBoard();
            emailInput = (TextInputEditText) emailWrapper.getEditText();
            passwordInput = (TextInputEditText) passwordWrapper.getEditText();
            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();
//            String user = userType.getSelectedItem().toString();

            String name1 = r1.getText().toString();
            String contact1 = r2.getText().toString();

            if(name1==""|| contact1==""){
                Toast.makeText(this,"Enter all the fields", Toast.LENGTH_SHORT).show();
            }
            else
            {

                user1.setName(name1);
                user1.setMobile(contact1);

//                Firebase firebase = new Firebase(Config.url);
//                firebase.child("person"+i).setValue(user);
//                i++;
                Toast.makeText(this,"Data saved successfully",Toast.LENGTH_SHORT).show();
            }



            //change was done from here to here
//            if (user.equals("Driver")) {
                SharedPreferences.Editor editor = getPreferences(Context.MODE_PRIVATE).edit();
                editor.putBoolean(getString(R.string.isDriver), true);
//                editor.commit();
                editor.apply();
//            } else {
//                SharedPreferences.Editor editor = getPreferences(Context.MODE_PRIVATE).edit();
//                editor.remove(getString(R.string.isDriver));
////                editor.commit();
//                editor.apply();
//            }

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, task -> {
                if (!task.isSuccessful()) {

                    Toast.makeText(LoginActivity.this, "Sign up error!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    String user_id = mAuth.getCurrentUser().getUid();

                    Log.i(LOG_TAG, "User is : " + "Donor");


//                    Log.i(LOG_TAG, "User is : " + user);
//                    User user1 = new User();

                    //change was done from here to here
//                    Boolean isDriver = getPreferences(Context.MODE_PRIVATE).getBoolean(getString(R.string.isDriver), false);
//                    if (isDriver) {
                        DatabaseReference user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Donor").child(user_id);
//                                user_db.setValue(user1.toString());
                                  user_db.setValue(true);

//****************************************user no***********************************************************************************
                    user1.setUser_n("NA");
                    FirebaseDatabase.getInstance().getReference().child("Users").child("Donor").child(user_id).setValue(user1);
                    user_no.child("user_no").child(user_id).setValue("NA");
//****************************************user no********************************************************************************

                    String uIdd = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Save the Life").child(value1).child(uIdd);
                    ref.setValue("Donor");


                    Intent intentt = new Intent(this,DonorMapsActivity.class);
                    startActivity(intentt);





//                    } else {
//                        DatabaseReference user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Acceptor").child(user_id);
//                        user_db.setValue(true);
//                    }
                    //up to here
                }
            });
        });



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
        Intent intentt1 = new Intent(this,HUseH.class);
        startActivity(intentt1);

    }
}

