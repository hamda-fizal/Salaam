package com.example.salaam.Fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
        final String currentUid=firebaseUser.getUid();
      /*  reference=FirebaseDatabase.getInstance().getReference("ChatList").child(currentUid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                userIds.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    ChatList chatList=dataSnapshot.getValue(ChatList.class);
                    userIds.add(chatList);
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        readChats();
*/
        final List<String> stringUids = new ArrayList<>();
        for(int i=0;i<userIds.size();i++){
            stringUids.add(userIds.get(i).getId());
        }
        reference=FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userIds.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){

                    String receiverId=dataSnapshot.child("receiver").getValue(String.class);
                    String senderId=dataSnapshot.child("sender").getValue(String.class);

                    if(receiverId.equals(currentUid)){

                    assert receiverId != null;
                    ChatList chatList=new ChatList(senderId);
                    if(!stringUids.contains(senderId))
                        userIds.add(chatList);
                    }
                    else if(senderId.equals(currentUid)){
                        assert senderId != null;
                        ChatList chatList=new ChatList(receiverId);
                        if(!stringUids.contains(receiverId))
                            userIds.add(chatList);

                    }
                }
                readChats();

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




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

                    userAdapter = new UserAdapter(getContext(), users,true);
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
