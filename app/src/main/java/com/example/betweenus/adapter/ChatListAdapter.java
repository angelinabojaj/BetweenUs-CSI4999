package com.example.betweenus.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.betweenus.R;
import com.example.betweenus.model.User;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatViewHolder> {

    private List<User> users;
    private OnChatClickListener listener;

    public interface OnChatClickListener {
        void onChatClick(User user);
    }

    public ChatListAdapter(List<User> users, OnChatClickListener listener) {
        this.users = users;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_list, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        User user = users.get(position);
        holder.usernameText.setText(user.getName());
        // holder.profileImage.setImageResource(user.getAvatar()); // Use actual avatar if available
        holder.itemView.setOnClickListener(v -> listener.onChatClick(user));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView profileImage;
        TextView usernameText, lastMessageText;

        public ChatViewHolder(@NonNull View view) {
            super(view);
            profileImage = view.findViewById(R.id.profileImage);
            usernameText = view.findViewById(R.id.usernameText);
            lastMessageText = view.findViewById(R.id.lastMessageText);
        }
    }
}