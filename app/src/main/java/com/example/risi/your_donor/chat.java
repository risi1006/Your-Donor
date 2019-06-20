package com.example.risi.your_donor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class chat extends AppCompatActivity {


    DatabaseReference reference;
    EditText e1;
    ListView l1;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;
    private  Button click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        click = (Button)findViewById(R.id.button2);
        e1=(EditText)findViewById(R.id.editText);
        l1 = (ListView)findViewById(R.id.listview);
        list = new ArrayList<String>();
//
//////        adapter = new ArrayAdapter<String>(this,R.layout.activity_chat,list);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);
        String uId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//
        reference = FirebaseDatabase.getInstance().getReference().child("chat").child(uId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Set<String> myset = new HashSet<String>();
                Iterator i = dataSnapshot.getChildren().iterator();

                for (DataSnapshot datas : dataSnapshot.getChildren()){

                    myset.add(datas.getValue(String.class));

                }


//                while (i.hasNext())
//                {
//                    myset.add(dataSnapshot.getChildren().toString());
//                }
//                list.clear();
                list.addAll(myset);
                l1.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Sorry...Network Issue",Toast.LENGTH_LONG).show();
            }
        });

click.setOnClickListener(v -> {
    String uId1 = FirebaseAuth.getInstance().getCurrentUser().getUid();
    DatabaseReference ref;
    ref = FirebaseDatabase.getInstance().getReference().child("chat").child(uId1);
    HashMap<String,Object> hashMap = new HashMap<>();
    hashMap.put(e1.getText().toString()," ");
    ref.updateChildren(hashMap);

    list.add(e1.getText().toString());
//    adapter.notifyDataSetChanged();
});


    }

}
