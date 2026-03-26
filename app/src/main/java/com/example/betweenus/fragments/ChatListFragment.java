package com.example.betweenus.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.betweenus.DatabaseHelper;
import com.example.betweenus.R;
import com.example.betweenus.adapter.ChatListAdapter;
import com.example.betweenus.model.User;

import java.util.List;

public class ChatListFragment extends Fragment {

    private RecyclerView chatListRecyclerView;
    private ChatListAdapter chatListAdapter;
    private DatabaseHelper dbHelper;
    private int currentUserId = 1; // For demo, should be retrieved from session

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper = new DatabaseHelper(requireContext());
        chatListRecyclerView = view.findViewById(R.id.chatListRecyclerView);
        chatListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadChats();
    }

    private void loadChats() {
        // For simplicity, we'll list all friends as potential chats.
        // In a real app, you'd show users with existing message history.
        List<User> users = dbHelper.getUserAndFriends(currentUserId);
        // Remove current user from the list
        users.removeIf(user -> user.getUserId() == currentUserId);

        chatListAdapter = new ChatListAdapter(users, user -> {
            Bundle bundle = new Bundle();
            bundle.putInt("receiverId", user.getUserId());
            bundle.putString("receiverName", user.getName());
            Navigation.findNavController(requireView()).navigate(R.id.action_navigation_chat_list_to_messagingFragment, bundle);
        });
        chatListRecyclerView.setAdapter(chatListAdapter);
    }
}