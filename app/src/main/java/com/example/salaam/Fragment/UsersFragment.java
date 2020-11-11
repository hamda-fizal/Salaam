package com.example.salaam.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.salaam.Adapter.UserAdapter;
import com.example.salaam.R;
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

public class UsersFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> mUsers;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_users, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager((getContext())));

        mUsers = new ArrayList<>();
        readUsers(new FirebaseCallback() {
            @Override
            public void onCallback(List<User> users) {
                Log.i("Callback",users.toString());
            }
        });

        return view;
    }

    private void readUsers(final FirebaseCallback firebaseCallback) {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    User user = snapshot1.getValue(User.class);
                    try{
                        assert user!=null;
                        assert firebaseUser != null;
                        if (!user.getUid().equals((firebaseUser.getUid()))) {
                            mUsers.add(user);
                        }
                    }catch (Exception e){
                        Toast.makeText(getActivity(),"Network Timeout",Toast.LENGTH_SHORT).show();

                    }
                }
                firebaseCallback.onCallback(mUsers);
                userAdapter = new UserAdapter(getContext(), mUsers,false);
                recyclerView.setAdapter(userAdapter);
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
           //     Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_SHORT).show();

            }
        });
    }


    public interface FirebaseCallback {

          void onCallback(List<User> users);
    }

}
