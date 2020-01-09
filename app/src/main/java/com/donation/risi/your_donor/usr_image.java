//package com.example.aditya.bustrack;
//
//import android.content.Intent;
//import android.net.Uri;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.view.ViewPagerAdapter;
//import android.widget.ImageView;
//
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//
//public class usr_image extends AppCompatActivity {
//    private StorageReference mStorageRef;
//    private DatabaseReference mDatabaseRef;
//    private ImageView imageView;
//    private Uri imguri;
//
//    public static final String FB_STORAGE_PATH = "image/";
//    public static final String FB_DATABASE_PATH = "image";
//    public static final int REQUEST_CODE = 1234;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_usr_image);
//        mStorageRef = FirebaseStorage.getInstance().getReference();
//        mDatabaseRef = FirebaseDatabase.getInstance().getReference(FB_DATABASE_PATH);
//
//        imageView = (ImageView)findViewById(R.id.usrimg);
//    }
//
//    public void btnBrowse(ViewPagerAdapter view) {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent,"Select image"),REQUEST_CODE);
//    }
//}
