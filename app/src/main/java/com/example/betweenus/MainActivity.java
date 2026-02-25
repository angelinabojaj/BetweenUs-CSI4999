package com.example.betweenus;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.betweenus.fragments.MemoriesFragment;
import com.example.betweenus.fragments.ProductivityFragment;
import com.example.betweenus.fragments.ProfileFragment;
import com.example.betweenus.fragments.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.betweenus.databinding.ActivityMainBinding;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper myDBHelper;
    public SQLiteDatabase db;
    private ActivityMainBinding binding;
    private static final int NOTIFICATION_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createDB();
        testDatabaseQuery();
        testLogin();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);
        binding.navView.setOnNavigationItemSelectedListener(navListener);

        requestNotificationPermission();
    }

    private void requestNotificationPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        NOTIFICATION_PERMISSION_CODE
                );
                return;
            }
        }

        // Permission already granted OR Android < 13
        showWelcomeNotification();
    }

    private void showWelcomeNotification() {
        NotificationHelper.showNotification(
                this,
                "Welcome!",
                "Log your mood & post a memory for your friends to see!"
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == NOTIFICATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showWelcomeNotification();
            }
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {

                Fragment selectedFragment = null;
                int id = item.getItemId();

                if (id == R.id.navigation_home) {
                    selectedFragment = new HomeFragment();

                } else if (id == R.id.navigation_memories) {
                    selectedFragment = new MemoriesFragment();

                } else if (id == R.id.navigation_profile) {
                    selectedFragment = new ProfileFragment();
                } else if (id == R.id.navigation_productivity) {
                    selectedFragment = new ProductivityFragment();
                }


                if (selectedFragment != null) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.nav_host_fragment_activity_main, selectedFragment)
                            .commit();
                    return true;
                }

                return false;
            };

    public void createDB() {
        myDBHelper = new DatabaseHelper(this);
        try {
            myDBHelper.createDatabase();
        } catch (IOException e) {
            throw new Error ("Unable to create database");
        }
        try{
            db = myDBHelper.getWritableDatabase();
        } catch (SQLException sqle) {
        }

    }

    public long registerUser(String name, String phone, String email,
                             String password, String bio, int avatar) {

        return myDBHelper.insertUser(name, phone, email, password, bio, avatar);
    }

    public Cursor getUserByName(String username) {
        return myDBHelper.getUserByName(username);
    }

    public Cursor getAllUsers() {
        return myDBHelper.getUsers();
    }

    public Cursor searchUsers(String query) {
        return myDBHelper.searchUsers(query);
    }

    public void updateUserBio(int userID, String newBio) {
        myDBHelper.updateUserBio(userID, newBio);
    }

    public void updateUserField(String column,
                                Object newValue,
                                String whereColumn,
                                Object whereValue) {

        myDBHelper.updateColumnValue(column, newValue, whereColumn, whereValue);
    }

    public long addFriend(int firstUserID, int secondUserID) {
        return myDBHelper.newFriend(firstUserID, secondUserID);
    }

    public Cursor getUserFriends(int userID) {
        return myDBHelper.getUserFriends(userID);
    }

    public long createTask(int creatorID,
                           String goal,
                           int isPrivate,
                           String dueDate) {

        return myDBHelper.createTask(creatorID, goal, isPrivate, dueDate);
    }

    public Cursor getUserTasks(int userID) {
        return myDBHelper.getUserTasks(String.valueOf(userID));
    }

    public void completeTask(int taskID) {
        myDBHelper.markTaskComplete(taskID);
    }

    public Cursor getUserSchedule(int userID) {
        return myDBHelper.getUserSchedule(userID);
    }

    public long postComment(int userID, int postID, String commentBody){
        return myDBHelper.postComment(postID, userID, commentBody);
    }

    public Cursor getComments(int postID){
        return myDBHelper.getPostComments(postID);
    }

    public boolean login(int userID, String password){
        return myDBHelper.login(userID, password);
    }
    private void testDatabaseQuery() {

        try {
            SQLiteDatabase database = myDBHelper.getWritableDatabase();

            Cursor cursor = database.rawQuery("SELECT name FROM sqlite_master WHERE type='table';", null);

            if (cursor.moveToFirst()) {
                do {
                    String tableName = cursor.getString(0);
                    Log.d("DB_TEST", "Table found: " + tableName);
                } while (cursor.moveToNext());
            } else {
                Log.d("DB_TEST", "No tables found.");
            }

            cursor.close();

        } catch (Exception e) {
            Log.e("DB_TEST", "Database query failed", e);
        }
    }
    private void testLogin(){
       if (login(1, "pass") == true){
           Log.e("LOG_TEST","Login Test Successful");
       }
       else Log.e("LOG_TEST","Login Test unsuccessful");
    }
}
