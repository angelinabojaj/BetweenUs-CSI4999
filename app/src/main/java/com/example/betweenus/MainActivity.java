package com.example.betweenus;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.betweenus.databinding.ActivityMainBinding;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper myDBHelper;
    public SQLiteDatabase db;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        createDB();
        testDatabaseQuery();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

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



}