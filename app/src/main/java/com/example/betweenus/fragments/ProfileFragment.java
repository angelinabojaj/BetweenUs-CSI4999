package com.example.betweenus.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.betweenus.DatabaseHelper;
import com.example.betweenus.R;
import com.example.betweenus.adapter.FriendAdapter;
import com.example.betweenus.model.Friend;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private RecyclerView recyclerView;
    private FriendAdapter friendAdapter;
    private List<Friend> friendList;
    private DatabaseHelper dbHelper;
    private int currentUserID = 1; // Hardcoded for now until we get login set-up

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
            recyclerView.setVisibility(View.VISIBLE);
            loadFriends();
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