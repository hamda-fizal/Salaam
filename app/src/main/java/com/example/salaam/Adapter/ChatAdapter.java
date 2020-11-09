package com.example.salaam.Adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.salaam.R;
import com.example.salaam.model.Chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    public static final int msgSendType = 1;
    public static final int msgReceiveType = 0;

    private Context mContext;
    private List<Chat> chats;
    private String imageUrl;


    FirebaseUser firebaseUser;

    public ChatAdapter(Context mContext, List<Chat> chats, String imageUrl) {
        this.mContext = mContext;
        this.chats = chats;
        this.imageUrl = imageUrl;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == msgSendType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.sender_chat_item, parent, false);
            return new ChatAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.receiver_chat_item, parent, false);
            return new ChatAdapter.ViewHolder(view);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Chat chat = chats.get(position);
        //String message = chats.get(position);
        holder.messageTextView.setText(chat.getMessage());
        if (!imageUrl.equals("default")) {
            Glide.with(mContext).load(imageUrl).into(holder.displayPicture);
        }

    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView messageTextView;
        public ImageView displayPicture;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            messageTextView = itemView.findViewById(R.id.messageTextView);
            displayPicture = itemView.findViewById(R.id.displayPicture);
        }
    }

    @Override
    public int getItemViewType(int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (chats.get(position).getSender().equals(firebaseUser.getUid()))
            return msgSendType;
        else
            return msgReceiveType;

    }
}


