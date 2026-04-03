package com.example.betweenus.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

import androidx.fragment.app.Fragment;

import com.example.betweenus.DatabaseHelper;
import com.example.betweenus.R;
import com.example.betweenus.activities.ProfileDetailActivity;
import com.example.betweenus.activities.ShopActivity;
import com.example.betweenus.adapter.ChatListAdapter;
import com.example.betweenus.model.Task;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class ProfileFragment extends Fragment {

    private TextView friendsCount, addFriendLink, username, bio;

    private ImageButton shopbtn;
    private ShapeableImageView pfp;
    private DatabaseHelper dbHelper;

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
        shopbtn = view.findViewById(R.id.btn_shop);


        loadUserData();

        friendsCount.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ProfileDetailActivity.class));
        });

        addFriendLink.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ProfileDetailActivity.class));
        });

        shopbtn.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ShopActivity.class));
        });

        // call personal schedule
        setupPersonalSchedule(view);
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

            // SET DATA
            username.setText(userName);
            bio.setText(userBio);
            friendsCount.setText(friendNum + " Friends");

            // SET PROFILE IMAGE
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

        // SET BORDER COLOR BASED ON AVATAR
        int borderColor = ChatListAdapter.getBoarder(avatarId);
        pfp.setStrokeColor(ColorStateList.valueOf(borderColor));
    }

    //Calendar view setup
    private void setupPersonalSchedule(View view){
        FrameLayout calendarContainer = view.findViewById(R.id.googleCalendarContainer);

        //create calendarView
        CalendarView calendarView = new CalendarView(requireContext());
        calendarContainer.addView(calendarView);

        //highlight days with tasks
        List<Task> tasks = dbHelper.getTasksForUser(dbHelper.currentUserId);
        calendarView.setOnDateChangeListener((cv, year, month, dayOfMonth) ->{
            String selectDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
            StringBuilder sb = new StringBuilder();
            for(Task t : tasks){
                if(t.getDueDate().startsWith(selectDate)){
                    sb.append("📌 ").append(t.getTaskGoal()).append("\n");

                }
            }
            String msg = sb.length() == 0 ? "No task for today!" : sb.toString().trim();
            String displayDate = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(new GregorianCalendar(year, month, dayOfMonth).getTime());
            new AlertDialog.Builder(requireContext()).setTitle("📅 " + displayDate).setMessage(msg).setPositiveButton("Got it!", null).show();
        });
    }


}
