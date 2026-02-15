package com.example.betweenus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "BetweenUs.db";
    public static final int DATABASE_VERSION = 5; // increase this
    private static String DB_PATH = "/data/data/BetweenUs/databases/";
    private SQLiteDatabase db;
    private Context myContext;
    //Users Table
    private static final String TABLE_USERS = "users";
    private static final String COL_USER_ID = "UserID";
    private static final String COL_NAME = "UserName";
    private static final String COL_EMAIL = "UserEmail";
    private static final String COL_PHONE = "UserPhone";
    private static final String COL_BIO = "UserBio";
    private static final String COL_Premium = "IsPremium";
    private static final String COL_AVATAR = "Avatar";
    private static final String COL_MOOD = "CurrentMood";
    private static final String COL_FRIEND_COUNT = "FriendsNumber";
    private static final String COL_POINTS = "Points";
    private static final String COL_SCHEDULE = "Schedule"; // Add this at top with other column declarations
    private static final String COL_PASSWORD = "password";

    // avatarownership Table
    private static final String TABLE_AVATAR_OWNERSHIP = "avatarownership";
    private static final String COL_OWNERSHIP_ID = "OwnershipID";
    private static final String COL_User_ID = "UserID";
    private static final String COL_AVATAR_OWNED = "AvatarOwned";

    // Dashboard Table
    private static final String TABLE_DASHBOARD = "dashboard";
    private static final String COL_POST_ID = "PostID";
    private static final String COL_SENDER_ID = "SenderID";
    private static final String COL_DASHBOARD_ID = "DashboardID";
    private static final String COL_POST_TEXT = "PostText";
    private static final String COL_POST_TIME = "PostTime";

    // Friends Table
    private static final String TABLE_FRIENDS = "friends";
    private static final String COL_FRIENDSHIP_ID = "FriendshipID";
    private static final String COL_FIRST_FRIEND_ID = "First_Friend_ID";
    private static final String COL_SECOND_FRIEND_ID = "Second_Friend_ID";
    private static final String COL_FRIENDSHIP_DATE = "Freindship_Date";

    // Memories Table
    private static final String TABLE_MEMORIES = "memories";
    private static final String COL_MEMORY_ID = "MemoryID";
    private static final String COL_POSTER_ID = "PosterID";
    private static final String COL_MEMORY_TITLE = "MemoryTitle";
    private static final String COL_MEMORY_BODY = "MemoryBody";
    private static final String COL_MEDIA_TYPE = "MediaType";
    private static final String COL_MEDIA_URL = "MediaURL";
    private static final String COL_TIME_STAMP = "TimeStamp";
    private static final String COL_LIKES = "Likes";

    // Messages Table
    private static final String TABLE_MESSAGES = "messages";
    private static final String COL_MESSAGE_ID = "MessageID";
    private static final String COL_MESSAGE_SENDER_ID = "SenderID";
    private static final String COL_RECEIVER_ID = "ReceiverID";
    private static final String COL_MESSAGE_TEXT = "MessageText";
    private static final String COL_MESSAGE_TIME = "MessageTime";

    // Tasks Table
    private static final String TABLE_TASKS = "tasks";
    private static final String COL_TASK_ID = "TaskID";
    private static final String COL_TASK_CREATOR = "TaskCreator";
    private static final String COL_TASK_GOAL = "TaskGoal";
    private static final String COL_IS_COMPLETE = "IsComplete";
    private static final String COL_IS_PRIVATE = "IsPrivate";
    private static final String COL_DUE_DATE = "DueDate";


    // SubTasks Table
    private static final String TABLE_SUBTASKS = "subtasks";
    private static final String COL_SUBTASK_ID = "SubTaskID";
    private static final String COL_ORIGINAL_TASK_ID = "TaskID";
    private static final String COL_SUBTASK_COMPLETE = "IsComplete";

    // Suggestions Table
    private static final String TABLE_SUGGESTIONS = "suggestions";
    private static final String COL_SUGGESTION_ID = "suggestionID";
    private static final String COL_SUGGESTION = "Suggestion";
    private static final String COL_KEYWORDS = "Keywords";

    // User history Table
    private static final String TABLE_USER_HISTORY = "userhistory";
    private static final String COL_RECORD_ID = "RecordID";
    private static final String COL_USER_HISTORY_ID = "UserID";
    private static final String COL_RECORD_CATEGORY = "RecordCategory";
    private static final String COL_RECORD_VALUE = "RecordValue";
    private static final String COL_TIME_RECORDED = "TimeRecorded";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.myContext = context;
    }

    public void createDatabase() throws IOException {
        boolean dbExist = checkDataBase();

        if(dbExist) {

        } else {
            this.getReadableDatabase();
            copyDatabase(myContext);
        }
    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;

        try {
            File dbFile = myContext.getDatabasePath(DATABASE_NAME); // correct dynamic path
            checkDB = SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLException e) {
            // database does not exist yet
        }

        if (checkDB != null) {
            checkDB.close();
        }

        return checkDB != null;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Users Table
//        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " ("
//                + COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
//                + COL_NAME + " TEXT, "
//                + COL_EMAIL + " TEXT UNIQUE, "
//                + COL_PHONE + " TEXT, "
//                + COL_BIO + " TEXT, "
//                + COL_PREMIUM + " INTEGER, "
//                + COL_AVATAR + " INTEGER, "
//                + COL_MOOD + " INTEGER, "
//                + COL_FRIEND_COUNT + " INTEGER DEFAULT 0, "
//                + COL_POINTS + " INTEGER DEFAULT 0, "
        //        + COL_SCHEDULE + " TEXT, "
        //        + COL_PASSWORD + " TEXT)"
//        db.execSQL(createUsersTable);
//
//        // Create avatar Table
//        String createAvatarTable = "CREATE TABLE " + TABLE_AVATAR_OWNERSHIP + " ("
//                + COL_OWNERSHIP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
//                + COL_USER_ID + " INTEGER, "
//                + COL_AVATAR_OWNED + " INTEGER, "
//                + "FOREIGN KEY(" + COL_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COL_USER_ID + "))";
//        db.execSQL(createAvatarTable);
//
//        // Create Dashboard Table
//       String createDashboardTable = "CREATE TABLE " + TABLE_DASHBOARD + " ("
//        + COL_Post_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
//        + COL_SENDER_ID + " INTEGER, "
//        + COL_DASHBOARD_ID + " INTEGER, "
//        + COL_POST_TEXT + " TEXT, "
//        + COL_POST_TIME +  " DATETIME DEFAULT CURRENT_TIMESTAMP, "
//        + "FOREIGN KEY(" + COL_SENDER_ID + ") REFERENCES " + TABLE_USERS + "(" + COL_USER_ID + "), "
//        + "FOREIGN KEY(" + COL_DASHBOARD_ID + ") REFERENCES " + TABLE_USERS + "(" + COL_USER_ID + "))";
//        db.execSQL(createDashboardTable);
//
//        // Create Friends Table
//        String createLibraryTable = "CREATE TABLE " + TABLE_FRIENDS + " ("
//                + COL_FRIENDSHIP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
//                + COL_FIRST_FRIEND_ID + " INTEGER, "
//                + COL_SECOND_FRIEND_ID + " INTEGER, "
//                + COL_FRIENDSHIP_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
//                + "FOREIGN KEY(" + COL_FIRST_FRIEND_ID + ") REFERENCES " + TABLE_USERS + "(" + COL_USER_ID + "), "
//                + "FOREIGN KEY(" + COL_SECOND_FRIEND_ID + ") REFERENCES " + TABLE_USERS + "(" + COL_USER_ID + "))";
//        db.execSQL(createLibraryTable);
//
//        // Create MEMORIES Table
//        String createMemoriesTable = "CREATE TABLE " + TABLE_MEMORIES + " ("
//                + COL_MEMORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
//                + COL_POSTER_ID + " INTEGER, "
//                + COL_MEMORY_TITLE + " TEXT, "
//                + COL_MEMORY_BODY + " TEXT, "
//                + COL_MEDIA_TYPE + " TEXT, "
//                + COL_MEDIA_URL + " TEXT, "
//                + COL_TIME_STAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
//                + COL_LIKES + " INTEGER, "
//                + "FOREIGN KEY(" + COL_POSTER_ID + ") REFERENCES " + TABLE_USERS + "(" + COL_USER_ID + "))";
//        db.execSQL(createMemoriesTable);
//
//        // Create Messages Table
//        String createMessagesTable = "CREATE TABLE " + TABLE_MESSAGES + " ("
//                + COL_MESSAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
//                + COL_MESSAGE_SENDER_ID + " INTEGER, "
//                + COL_RECEIVER_ID + " INTEGER, "
//                + COL_MESSAGE_TEXT + " TEXT, "
//                + COL_MESSAGE_TIME + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
//                + "FOREIGN KEY(" + COL_MESSAGE_SENDER_ID + ") REFERENCES " + TABLE_USERS + "(" + COL_USER_ID + "), "
//                + "FOREIGN KEY(" + COL_RECEIVER_ID + ") REFERENCES " + TABLE_USERS + "(" + COL_USER_ID + "))";
//        db.execSQL(createMessagesTable);
//
//        // Create Tasks Table
//        String createTaskTable = "CREATE TABLE " + TABLE_TASKS + " ("
//                + COL_Task_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
//                + COL_TASK_CREATOR + " INTEGER, "
//                + COL_TASK_GOAL+ " TEXT, "
//                + COL_IS_COMPLETE + " INTEGER, "
//                + COL_IS_PRIVATE + " INTEGER, "
//                + COL_DUE_DATE + " INTEGER, "
//                + "FOREIGN KEY(" + COL_TASK_CREATOR + ") REFERENCES " + TABLE_USERS + "(" + COL_USER_ID + "))";
//        db.execSQL(createTaskTable);

         // Create SubTasks Table
//        String createSubTaskTable = "CREATE TABLE " + TABLE_SUBTASKS + " ("
//                + COL_SUBTASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
//                + COL_ORIGINAL_TASK_ID + " INTEGER, "
//                + COL_ASSIGNEE_ID + " INTEGER, "
//                + COL_SUBTASK_COMPLETE + " INTEGER, "
//                + "FOREIGN KEY(" + COL_ORIGINAL_TASK_ID + ") REFERENCES " + TABLE_TASKS + "(" + COL_TASK_ID+ ")
//                + "FOREIGN KEY(" + COL_ASSIGNEE_ID + ") REFERENCES " + TABLE_USERS + "(" + COL_USER_ID + "))";
//        db.execSQL(createSubTaskTable);

        //        // Create Suggestions Table
//        String createSuggestionsTable = "CREATE TABLE " + TABLE_SUGGESTIONS + " ("
//                + COL_SUGGESTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
//                + COL_SUGGESTION + " TEXT, "
//                + COL_KEYWORDS + " TEXT)"
//        db.execSQL(createSuggestionsTable);
        //        // Create History Table
//        String createHistoryTable = "CREATE TABLE " + TABLE_USER_HISTORY + " ("
//                + COL_RECORD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
//                + COL_USER_HISTORY_ID + " INTEGER, "
//                + COL_RECORD_CATEGORY + " TEXT, "
//                + COL_RECORD_VALUE + " INTEGER, "
//                + COL_TIME_RECORDED + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
//                + "FOREIGN KEY(" + COL_USER_HISTORY_ID + ") REFERENCES " + TABLE_USERS + "(" + COL_USER_ID + "))";
//        db.execSQL(createHistoryTable);

    }

    public void openDataBase() throws SQLException{
        //Open the database
        String myPath = myContext.getDatabasePath(DATABASE_NAME).getPath();
        db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

    }

    @Override
    public synchronized void close() {
        if (db != null) {
            db.close();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DASHBOARD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AVATAR_OWNERSHIP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FRIENDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBTASKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUGGESTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_HISTORY);
        onCreate(db);
    }

    private void copyDatabase(Context context) {
        File dbPath = context.getDatabasePath(DATABASE_NAME);

        if (!dbPath.exists()) {
            dbPath.getParentFile().mkdirs();

            try (InputStream input = context.getAssets().open(DATABASE_NAME);
                 OutputStream output = new FileOutputStream(dbPath)) {

                byte[] buffer = new byte[1024];
                int length;

                while ((length = input.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }

                output.flush();
            } catch (IOException e) {
                throw new RuntimeException("Error copying database", e);
            }
        }
    }

    // Search for users by username or name
    public Cursor searchUsers(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        String searchQuery = "SELECT * FROM " + TABLE_USERS +
                " WHERE " + COL_NAME + " LIKE ? OR " + COL_NAME + " LIKE ?";
        // " ORDER BY " + COL_FOLLOWERS_COUNT + " DESC LIMIT 20";
        return db.rawQuery(searchQuery, new String[]{"%" + query + "%", "%" + query + "%"});
    }
    // Create User
    public long insertUser(String name, String phone, String email, String password, String bio, Integer avatar) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_PHONE, phone);
        values.put(COL_EMAIL, email);
        values.put(COL_PASSWORD, password);
        values.put(COL_BIO, bio);
        values.put(COL_AVATAR, avatar);
        return db.insert(TABLE_USERS, null, values);
    }
    //Insert New Task
    public long createTask(Integer taskCreator, String goal, Integer isPrivate, String dueDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TASK_CREATOR, taskCreator);
        values.put(COL_TASK_GOAL, goal);
        values.put(COL_IS_PRIVATE, isPrivate);
        values.put(COL_DUE_DATE, dueDate);
        return db.insert(TABLE_TASKS, null, values);
    }
    //Get Tasks For User
    public Cursor getUserTasks(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        String searchQuery = "SELECT * FROM " + TABLE_TASKS +
                " WHERE " + COL_TASK_CREATOR + " LIKE ?";
        return db.rawQuery(searchQuery, new String[]{"%" + query + "%"});
    }
    //List of the names of all users
    public Cursor getUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        String searchQuery = "SELECT UserName FROM " + TABLE_USERS;
        // " ORDER BY " + COL_FOLLOWERS_COUNT + " DESC LIMIT 20";
        return db.rawQuery(searchQuery,null);
    }

    //Gets user given name
    public Cursor getUserByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_USERS, null, COL_NAME + "=?", new String[]{name}, null, null, null);
    }

    public Cursor getUserID(String userName) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_USERS, null, COL_NAME + "=?", new String[]{userName}, null, null, null);
    }

    // Post Operations
    public long newFriend(int firstFriendID, int secondFriendID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_FIRST_FRIEND_ID, firstFriendID);
        values.put(COL_SECOND_FRIEND_ID, secondFriendID);
        updateFriendCount(firstFriendID, 1);
        updateFriendCount(secondFriendID, 1);
        return db.insert(TABLE_FRIENDS, null, values);
    }

    private void updateFriendCount(int userId, int change) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_USERS + " SET " + COL_FRIEND_COUNT + " = " + COL_FRIEND_COUNT + " + " + change + " WHERE " + COL_USER_ID + " = " + userId);
    }

    public Cursor getUserFriends(Integer userID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String searchQuery = "SELECT * FROM " + TABLE_FRIENDS +
                " WHERE " + COL_FIRST_FRIEND_ID + " = ? " +
                " OR " + COL_SECOND_FRIEND_ID + " = ?";

        return db.rawQuery(searchQuery, new String[]{
                String.valueOf(userID),
                String.valueOf(userID)
        });
    }
    //updates user bio
    public void updateUserBio(Integer userID, String newBio) {
        SQLiteDatabase db = this.getWritableDatabase();

        String updateQuery = "UPDATE " + TABLE_USERS +
                " SET " + COL_BIO + " = ? " +
                " WHERE " + COL_USER_ID + " = ?";

        db.execSQL(updateQuery, new Object[]{
                newBio,
                userID
        });
    }
    //marks task complete
    public void markTaskComplete(int taskID) {

        SQLiteDatabase db = this.getWritableDatabase();

        String updateQuery = "UPDATE " + TABLE_TASKS +
                " SET " + COL_IS_COMPLETE + " = 1 " +
                " WHERE " + COL_TASK_ID + " = ?";

        db.execSQL(updateQuery, new Object[]{taskID});
    }

    //generic update profile function
    public void updateColumnValue(String columnToUpdate,
                                  Object newValue,
                                  String whereColumn,
                                  Object whereValue) {

        SQLiteDatabase db = this.getWritableDatabase();

        // Validate column names against allowed constants
        if (!isValidColumn(columnToUpdate) || !isValidColumn(whereColumn)) {
            throw new IllegalArgumentException("Invalid column name");
        }

        String query = "UPDATE " + TABLE_USERS +
                " SET " + columnToUpdate + " = ? " +
                " WHERE " + whereColumn + " = ?";

        db.execSQL(query, new Object[]{newValue, whereValue});
    }

    //column validation check
    private boolean isValidColumn(String column) {
        return column.equals(COL_NAME) ||
                column.equals(COL_PASSWORD) ||
                column.equals(COL_PHONE) ||
                column.equals(COL_EMAIL) ||
                column.equals(COL_Premium) ||
                column.equals(COL_SCHEDULE) ||
                column.equals(COL_POINTS) ||
                column.equals(COL_AVATAR) ||
                column.equals(COL_MOOD);
    }
    //retrieves user schedule
    public Cursor getUserSchedule(Integer userID) {
        SQLiteDatabase db = this.getReadableDatabase();

        String searchQuery = "SELECT " + COL_SCHEDULE +
                " FROM " + TABLE_USERS +
                " WHERE " + COL_USER_ID + " = ?";

        return db.rawQuery(searchQuery,
                new String[]{String.valueOf(userID)});
    }
}

