package com.example.salaam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.salaam.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    CircleImageView displayPicture;
    TextView usernameTextView;

    FirebaseUser firebaseUser;
    DatabaseReference ref;
    Intent intent;



//    ArrayList<String> messages=new ArrayList<>();
  //  ArrayAdapter<String> arrayAdapter;
    EditText chatBoxEditText;
    ImageButton sendButton;

    public void sendMessage(View view){
        //if(!chatBoxEditText.getText().toString().trim().equals("")){
        //    messages.add(chatBoxEditText.getText().toString().trim());
      //      arrayAdapter.notifyDataSetChanged();
     //   }
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

        displayPicture=findViewById(R.id.displayPicture);
        usernameTextView=findViewById(R.id.username);
        chatBoxEditText=(EditText)findViewById(R.id.chatboxEditText);
        sendButton=(ImageButton)findViewById(R.id.sendButton);



        intent=getIntent();
        String uid=intent.getStringExtra("uid");


        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        ref= FirebaseDatabase.getInstance().getReference("users").child(uid);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user=snapshot.getValue(User.class);
                if(user.getUsername()!=null) {
                    usernameTextView.setText(user.getUsername());
                    if (user.getImageURL().equals("default")) {
                        displayPicture.setImageResource(R.drawable.displaypicture);
                    } else {
                        Glide.with(ChatActivity.this).load(user.getImageURL()).into(displayPicture);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
