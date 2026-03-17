package com.example.betweenus.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.betweenus.R;
import com.example.betweenus.model.Message;
import com.example.betweenus.model.User;
import com.google.android.material.imageview.ShapeableImageView;
import java.util.List;
import java.util.Map;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Message> messageList;
    private Map<Integer, User> userMap;

    public MessageAdapter(List<Message> messageList, Map<Integer, User> userMap) {
        this.messageList = messageList;
        this.userMap = userMap;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messageList.get(position);
        holder.messageText.setText(message.getMessageText());
        holder.messageTimestamp.setText(message.getTimestamp());

        User sender = userMap.get(message.getSenderID());
        if (sender != null) {
            holder.messageUsername.setText(sender.getName());
            if (sender.getAvatar() != 0) {
                holder.messageProfileImage.setImageResource(sender.getAvatar());
            } else {
                holder.messageProfileImage.setImageResource(R.drawable.profilebtn_filled);
            }
        } else {
            holder.messageUsername.setText("Unknown User");
            holder.messageProfileImage.setImageResource(R.drawable.profilebtn_filled);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public void updateMessages(List<Message> newMessages, Map<Integer, User> newUserMap) {
        this.messageList = newMessages;
        this.userMap = newUserMap;
        notifyDataSetChanged();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView messageProfileImage;
        TextView messageUsername;
        TextView messageText;
        TextView messageTimestamp;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageProfileImage = itemView.findViewById(R.id.messageProfileImage);
            messageUsername = itemView.findViewById(R.id.messageUsername);
            messageText = itemView.findViewById(R.id.messageText);
            messageTimestamp = itemView.findViewById(R.id.messageTimestamp);
        }
    }
}
