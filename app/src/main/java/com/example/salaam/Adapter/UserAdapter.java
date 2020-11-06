package com.example.salaam.Adapter;


import android.content.Context;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.salaam.ChatActivity;
import com.example.salaam.R;
import com.example.salaam.model.User;


import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context mContext;
    private List<User> mUsers;

    public UserAdapter(Context mcontext,List<User> mUsers){
        this.mContext=mcontext;
        this.mUsers=mUsers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(mContext).inflate(R.layout.user_item,parent,false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final User user=mUsers.get(position);
        holder.usernameTextView.setText(user.getUsername());
        if(user.getImageURL().equals("default")) {
            holder.displayPicture.setImageResource(R.drawable.displaypicture);
        }else{
            Glide.with(mContext).load(user.getImageURL()).into(holder.displayPicture);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, ChatActivity.class);
                intent.putExtra("uid",user.getUid());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView usernameTextView;
        public ImageView displayPicture;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            usernameTextView= itemView.findViewById(R.id.usernameTextView);
            displayPicture=itemView.findViewById(R.id.displayPicture);
        }
    }


}
