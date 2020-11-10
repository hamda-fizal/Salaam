package com.example.salaam.Fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.salaam.Adapter.UserAdapter;
import com.example.salaam.R;
import com.example.salaam.model.ChatList;
import com.example.salaam.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> users;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    private List<ChatList> userIds;


    public ChatFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_chat, container, false);


       recyclerView=view.findViewById(R.id.recyclerView);
       recyclerView.setHasFixedSize(true);
       recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

       firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

       userIds=new ArrayList<>();

        reference=FirebaseDatabase.getInstance().getReference("ChatList").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                userIds.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    ChatList chatList=dataSnapshot.getValue(ChatList.class);
                    userIds.add(chatList);
                }
                readChats();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    /*    reference=FirebaseDatabase.getInstance().getReference("ChatList").child("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    ChatList chatList=dataSnapshot.

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        })


    */

        return view;
    }
    private void readChats(){
        users=new ArrayList<>();
        reference=FirebaseDatabase.getInstance().getReference("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    for (ChatList chatList : userIds) {
                        assert user != null;
                        if (user.getUid().equals(chatList.getId()))
                            users.add(user);
                    }

                    userAdapter = new UserAdapter(getContext(), users);
                    recyclerView.setAdapter(userAdapter);
                    userAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
