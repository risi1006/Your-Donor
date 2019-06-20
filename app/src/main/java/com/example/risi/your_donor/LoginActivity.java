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
import android.widget.Button;
import android.widget.EditText;
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


import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    private EditText r1, r2;
    User user1 = new User();
    private CoordinatorLayout coordinatorLayout;
    private AnimationDrawable animationDrawable;
    DatabaseReference user_no;
    int no_user=0;
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
        r1 = (EditText) findViewById(R.id.name);
        r2 = (EditText) findViewById(R.id.contact);
        Firebase.setAndroidContext(getApplicationContext());
        user_no  = FirebaseDatabase.getInstance().getReference();



//*****************************Finding  no of users******************************************************************************

        user_no = FirebaseDatabase.getInstance().getReference().child("Users").child("Donor");
        user_no.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    for(DataSnapshot ds: dataSnapshot.getChildren())
                    {   no_user++;
                    }
                }
                Toast.makeText(LoginActivity.this ,String.valueOf(no_user) ,Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        user_no = FirebaseDatabase.getInstance().getReference();
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
                    Intent intent = new Intent(LoginActivity.this, dashboard.class);
//                Intent intent = new Intent(LoginActivity.this, DriverMapsActivity.class);
                    startActivity(intent);
                    finish();
//                } else {
//                    Intent intent = new Intent(LoginActivity.this, StudentMapsActivity.class);
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
                    }

                }
            });
        });


        register.setOnClickListener(view -> {
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
                    user1.setUser_n(String.valueOf(no_user+1));
                    FirebaseDatabase.getInstance().getReference().child("Users").child("Donor").child(user_id).setValue(user1);
                    user_no.child("user_no").child(String.valueOf(no_user+1)).setValue(user_id);

                    no_user=0;





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
}

