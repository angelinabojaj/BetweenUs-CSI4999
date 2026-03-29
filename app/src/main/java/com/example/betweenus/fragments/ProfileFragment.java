package com.example.betweenus.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.betweenus.DatabaseHelper;
import com.example.betweenus.R;
import com.example.betweenus.activities.ProfileDetailActivity;
import com.example.betweenus.adapter.FriendAdapter;
import com.example.betweenus.model.Friend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProfileFragment extends Fragment {

    private TextView friendsCount, addFriendLink, username, bio;
    private ImageView pfp; // ✅ FIXED
    private DatabaseHelper dbHelper;
//    private int currentUserID = 15; // Hardcoded for now until we get login set-up
//    private boolean friendsVisible = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        dbHelper = new DatabaseHelper(requireContext());

        username = view.findViewById(R.id.profileUsername);
        bio = view.findViewById(R.id.profileBio);
        pfp = view.findViewById(R.id.profileImage);
        friendsCount = view.findViewById(R.id.friendsCount);
        addFriendLink = view.findViewById(R.id.addFriendLink);

        loadUserData();


        friendsCount.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ProfileDetailActivity.class));
        });

        addFriendLink.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ProfileDetailActivity.class));
        });

        return view;
    }

    private void loadUserData() {
        Cursor cursor = dbHelper.getUserByID(dbHelper.currentUserId);

        if (cursor != null && cursor.moveToFirst()) {

            String userName = cursor.getString(
                    cursor.getColumnIndexOrThrow("UserName")
            );

            String userBio = cursor.getString(
                    cursor.getColumnIndexOrThrow("UserBio")
            );

            int avatarId = cursor.getInt(
                    cursor.getColumnIndexOrThrow("Avatar")
            );

            int friendNum = cursor.getInt(
                    cursor.getColumnIndexOrThrow("FriendNumber")
            );

            // ✅ SET DATA
            username.setText(userName);
            bio.setText(userBio);
            friendsCount.setText(friendNum + " Friends");

            // ✅ SET PROFILE IMAGE
            setProfileImage(avatarId);

            cursor.close();
        }
    }

    private void setProfileImage(int avatarId) {
        int imageRes;

        switch (avatarId) {
            case 1:
                imageRes = R.drawable.bunny_profile;
                break;
            case 2:
                imageRes = R.drawable.giraffe_profile;
                break;
            case 3:
                imageRes = R.drawable.kitty_profile;
                break;
            case 4:
                imageRes = R.drawable.puppy_profile;
                break;
            default:
                imageRes = R.drawable.profilebtn_filled;
        }

        pfp.setImageResource(imageRes);
    }


}