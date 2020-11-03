package com.example.salaam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    String currentUserId,uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        ListView userListView=(ListView)findViewById(R.id.userListView);

        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(),ChatActivity.class);
                intent.putExtra("ActiveUser",users.get(position));

                startActivity(intent);
            }
        });

        arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,users);
        userListView.setAdapter(arrayAdapter);
        setTitle("User List");
        users.clear();
        Bundle extras = getIntent().getExtras();
        if (extras != null)
            uid = extras.getString("uid");
        Toast.makeText(this,uid,Toast.LENGTH_SHORT).show();
        userRef= FirebaseDatabase.getInstance().getReference().child("users");
        userRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                currentUserId = snapshot.child("uid").getValue(String.class);
                 if (!currentUserId.equals("null")||!currentUserId.equals(uid)) {
                    users.add(currentUserId);
                    arrayAdapter.notifyDataSetChanged();

                }
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
