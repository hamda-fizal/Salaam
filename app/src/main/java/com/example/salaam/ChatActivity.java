package com.example.salaam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.salaam.Adapter.ChatAdapter;
import com.example.salaam.model.Chat;
import com.example.salaam.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    CircleImageView displayPicture;
    TextView usernameTextView;
    EditText chatBoxEditText;
    ImageButton sendButton;

    ChatAdapter chatAdapter;
    List<Chat> chats;


    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    FirebaseUser firebaseUser;
    DatabaseReference ref;
    DatabaseReference chatref;
    Intent intent;

    private String uid;


    public void sendMessage(View view){

            final String message=chatBoxEditText.getText().toString();

        InputMethodManager imm = (InputMethodManager) ChatActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);




        if(!message.trim().isEmpty()){
            String sender=firebaseUser.getUid();
            final String receiver=uid;
            DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
            HashMap<String,Object> hashMap=new HashMap<>();
            hashMap.put("sender",sender);
            hashMap.put("receiver",receiver);
            hashMap.put("message",message);
            reference.child("Chats").push().setValue(hashMap);

            chatref=FirebaseDatabase.getInstance().getReference("ChatList").child(sender).child(receiver);
            chatref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(!snapshot.exists())
                        chatref.child("id").setValue(receiver);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        chatBoxEditText.setText("");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView=findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        chats=new ArrayList<>();

        displayPicture=findViewById(R.id.displayPicture);
        usernameTextView=findViewById(R.id.username);
        chatBoxEditText= findViewById(R.id.chatboxEditText);
        sendButton= findViewById(R.id.sendButton);



        intent=getIntent();
        uid=intent.getStringExtra("uid");


        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        ref= FirebaseDatabase.getInstance().getReference("users").child(uid);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user=snapshot.getValue(User.class);
                try {
                    assert user != null;
                    usernameTextView.setText(user.getUsername());
                    if (!user.getImageURL().equals("default")) {
                        Glide.with(ChatActivity.this).load(user.getImageURL()).into(displayPicture);
                    }

                    readMessages(firebaseUser.getUid(),uid,user.getImageURL());

                }catch (DatabaseException e){
                    Toast.makeText(ChatActivity.this,"Error while updating messages",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void readMessages(final String sender, final String receiver, final String imageURL){

        ref=FirebaseDatabase.getInstance().getReference("Chats");
      ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                chats.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                    Chat chat=dataSnapshot.getValue(Chat.class);

                        assert chat != null;
                        if ((chat.getSender().equals(sender) && chat.getReceiver().equals(receiver))
                                || (chat.getSender().equals(receiver) && chat.getReceiver().equals(sender)))
                            chats.add(chat);

                    chatAdapter = new ChatAdapter(ChatActivity.this, chats, imageURL);
                    recyclerView.setAdapter(chatAdapter);
                    chatAdapter.notifyDataSetChanged();

                }

                    }catch (Exception e){
                    Toast.makeText(ChatActivity.this,"Error while retrieving messages",Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(ChatActivity.this,error.toString(),Toast.LENGTH_SHORT).show();

            }
      });


    }
    private void status(String status){
        ref=FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("status",status);

        ref.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }
}
