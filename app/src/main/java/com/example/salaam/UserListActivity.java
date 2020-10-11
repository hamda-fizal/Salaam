package com.example.salaam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;

public class UserListActivity extends AppCompatActivity {


    ArrayAdapter arrayAdapter;
    ArrayList<String> users = new ArrayList<>();
    DatabaseReference userRef;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        ListView userListView=(ListView)findViewById(R.id.userListView);
        arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,users);
        userListView.setAdapter(arrayAdapter);
        setTitle("User List");
        users.clear();
     //   Intent intent =getIntent();
     //   String username= intent.getStringExtra("username");
      //  Log.i("KittiyeUsername",username);
        //mAuth=FirebaseAuth.getInstance();
        //  currentUser =mAuth.getCurrentUser();
        //    assert currentUser != null;
        //   final String uid=currentUser.getUid();
        userRef= FirebaseDatabase.getInstance().getReference().child("users");
        userRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //     if(!uid.equals(snapshot.getValue(String.class))) {
                email = snapshot.child("email id").getValue(String.class);
                if (!email.equals("null"))
                    users.add(email);
                arrayAdapter.notifyDataSetChanged();
                Log.i("email", "blaaaaaaaaa");

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}
