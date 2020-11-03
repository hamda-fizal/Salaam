package com.example.salaam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

     ListView messagesListView;
    ArrayList<String> messages=new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    EditText chatBoxEditText;
    ImageButton sendButton;
    DatabaseReference ref;
    FirebaseAuth mAuth;
    String messageSenderID,messageReceiverID;
    public void sendMessage(View view){
        if(!chatBoxEditText.getText().toString().trim().equals("")){
            messages.add(chatBoxEditText.getText().toString().trim());
            arrayAdapter.notifyDataSetChanged();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent=getIntent();
        String activeUser=intent.getStringExtra("ActiveUser");
        setTitle(activeUser.substring(0,activeUser.indexOf('@')));
      chatBoxEditText=(EditText)findViewById(R.id.chatboxEditText);
        sendButton=(ImageButton)findViewById(R.id.sendButton);
        messagesListView=(ListView)findViewById(R.id.messagesListView);
        arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,messages);
        messagesListView.setAdapter(arrayAdapter);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        messageSenderID=user.getUid();
        Toast.makeText(ChatActivity.this,"uid is "+messageSenderID,Toast.LENGTH_LONG).show();
        ref= FirebaseDatabase.getInstance().getReference();
//        ref.child("Messages").child(messageSenderID).child(messageReceiverID)
  //              .addChildEventListener(new ChildEventListener()
       /*ref.child("users").child(mAuth.getCurrentUser().getUid()).child("message").setValue(.getText().toString());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
    }
}
