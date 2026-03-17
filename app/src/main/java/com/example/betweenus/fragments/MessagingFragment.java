package com.example.betweenus.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.betweenus.DatabaseHelper;
import com.example.betweenus.R;
import com.example.betweenus.adapter.MessageAdapter;
import com.example.betweenus.model.Message;
import com.example.betweenus.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessagingFragment extends Fragment {

    private RecyclerView messagesRecyclerView;
    private MessageAdapter messageAdapter;
    private EditText messageInput;
    private ImageButton sendMessageButton;
    private DatabaseHelper dbHelper;
    
    // For demo purposes, we'll use a fixed current user ID and receiver ID
    // In a real app, these would be passed in or retrieved from a session
    private int currentUserId = 1; 
    private int receiverId = 2; 

    public MessagingFragment() {
        // Required empty public constructor
    }

    public static MessagingFragment newInstance() {
        return new MessagingFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DatabaseHelper(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_messaging, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        messagesRecyclerView = view.findViewById(R.id.messagesRecyclerView);
        messageInput = view.findViewById(R.id.messageInput);
        sendMessageButton = view.findViewById(R.id.sendMessageButton);

        setupRecyclerView();
        loadMessages();

        sendMessageButton.setOnClickListener(v -> {
            String text = messageInput.getText().toString().trim();
            if (!text.isEmpty()) {
                sendMessage(text);
            }
        });
    }

    private void setupRecyclerView() {
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // Initialize with empty data; will be populated in loadMessages
        messageAdapter = new MessageAdapter(new java.util.ArrayList<>(), new HashMap<>());
        messagesRecyclerView.setAdapter(messageAdapter);
    }

    private void loadMessages() {
        List<Message> messages = dbHelper.getMessages(currentUserId);
        List<User> allUsers = dbHelper.getUsersList();
        
        Map<Integer, User> userMap = new HashMap<>();
        for (User user : allUsers) {
            userMap.put(user.getUserId(), user);
        }

        messageAdapter.updateMessages(messages, userMap);
        if (messageAdapter.getItemCount() > 0) {
            messagesRecyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);
        }
    }

    private void sendMessage(String text) {
        long result = dbHelper.sendMessage(currentUserId, receiverId, text);
        if (result != -1) {
            messageInput.setText("");
            loadMessages(); // Refresh list
        } else {
            Toast.makeText(getContext(), "Failed to send message", Toast.LENGTH_SHORT).show();
        }
    }
}
