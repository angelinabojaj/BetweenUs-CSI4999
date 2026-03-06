package com.example.betweenus.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.betweenus.DatabaseHelper;
import com.example.betweenus.R;
import com.example.betweenus.adapter.FriendAdapter;
import com.example.betweenus.model.Friend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProfileFragment extends Fragment {

    private RecyclerView recyclerView;
    private FriendAdapter friendAdapter;
    private List<Friend> friendList;
    private DatabaseHelper dbHelper;
    private int currentUserID = 1; // Hardcoded for now until we get login set-up
    private boolean friendsVisible = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view_friends);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setVisibility(View.GONE); // hide initially

        dbHelper = new DatabaseHelper(requireContext());
        friendList = new ArrayList<>();
        friendAdapter = new FriendAdapter(requireContext(), friendList);
        recyclerView.setAdapter(friendAdapter);

        // Button to show friendslist
        Button viewFriendsButton = view.findViewById(R.id.viewFriendsButton);
        viewFriendsButton.setOnClickListener(v -> {
            if (friendsVisible) {
                recyclerView.setVisibility(View.GONE);
                friendsVisible = false;
                viewFriendsButton.setText("View Friends");
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                loadFriends();
                friendsVisible = true;
                viewFriendsButton.setText("Hide Friends");
            }
        });

        // Add Friend Button and edge cases
        EditText addFriendInput = view.findViewById(R.id.addFriendInput);
        Button addFriendButton = view.findViewById(R.id.addFriendButton);
        addFriendButton.setOnClickListener(v -> {
            String input = addFriendInput.getText().toString().trim();
            if(input.isEmpty()){
                Toast.makeText(requireContext(), "Enter a friend's name or ID", Toast.LENGTH_SHORT).show();
                return;
            }

            Cursor userCursor;

            // Check if input is a number then treat it as a userID
            if(input.matches("\\d+")){
                int userID = Integer.parseInt(input);
                userCursor = dbHelper.getUserByID(userID);
            } else {
                // else it's username
                userCursor = dbHelper.getUserByName(input);
            }

            if(userCursor == null || !userCursor.moveToFirst()){
                Toast.makeText(requireContext(), "User not found", Toast.LENGTH_SHORT).show();
                if(userCursor != null) userCursor.close();
                return;
            }

            int friendID = userCursor.getInt(userCursor.getColumnIndexOrThrow(DatabaseHelper.COL_USER_ID));
            userCursor.close();

            if(friendID == currentUserID){
                Toast.makeText(requireContext(), "You cannot add yourself", Toast.LENGTH_SHORT).show();
                return;
            }

            // Add friend
            long result = dbHelper.newFriend(currentUserID, friendID);
            boolean success = result != -1;
            if(success){
                Toast.makeText(requireContext(), "Friend added!", Toast.LENGTH_SHORT).show();
                addFriendInput.setText("");

                // Refresh list if visible
                if(friendsVisible){
                    loadFriends();
                }
            } else {
                Toast.makeText(requireContext(), "Already friends or invalid", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void loadFriends() {
        Cursor cursor = dbHelper.getUserFriends(currentUserID);
        friendList.clear();

        if(cursor.moveToFirst()) {
            do {
                int firstFriendID = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_FIRST_FRIEND_ID));
                int secondFriendID = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_SECOND_FRIEND_ID));

                int friendID = (firstFriendID == currentUserID) ? secondFriendID : firstFriendID;

                Cursor friendCursor = dbHelper.getUserByID(friendID);
                if(friendCursor.moveToFirst()){
                    String friendName = friendCursor.getString(friendCursor.getColumnIndexOrThrow(DatabaseHelper.COL_NAME));
                    String friendEmail = friendCursor.getString(friendCursor.getColumnIndexOrThrow(DatabaseHelper.COL_EMAIL));
                    Friend friend = new Friend(friendID, friendName, friendEmail);
                    friendList.add(friend);
                }
                friendCursor.close();

            } while (cursor.moveToNext());
        }
        cursor.close();
        friendAdapter.notifyDataSetChanged();
    }
}