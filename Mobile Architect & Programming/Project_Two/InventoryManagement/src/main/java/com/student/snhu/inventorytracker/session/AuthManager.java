package com.student.snhu.inventorytracker.session;

import android.content.Context;
import android.content.SharedPreferences;

import com.student.snhu.inventorytracker.database.DatabaseConnection;
import com.student.snhu.inventorytracker.model.User;
public class AuthManager {
    private static AuthManager instance;
    private static final String PREFS_NAME = "user_session";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_FNAME = "fName";
    private static final String KEY_LNAME = "lName";
    private static Context appContext;

    private static User currentUser;
    private boolean isLoggedIn;

    private AuthManager() {}

    public static void init(Context context) {
        if (appContext == null) {
            appContext = context.getApplicationContext();
            SharedPreferences prefs = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putLong(KEY_USER_ID, -1);
            editor.putString(KEY_USERNAME, null);
            editor.putString(KEY_FNAME, null);
            editor.putString(KEY_LNAME, null);
            editor.apply();
            instance = new AuthManager();
        }
    }

    public static synchronized AuthManager getInstance() {
        return instance;
    }

    public boolean isLoggedIn() {
        if (currentUser != null) return true;

        SharedPreferences prefs = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        long userId = prefs.getLong(KEY_USER_ID, -1);
        String username = prefs.getString(KEY_USERNAME, null);
        String fName = prefs.getString(KEY_FNAME, null);
        String lName = prefs.getString(KEY_LNAME, null);

        if (userId != -1 && username != null){
            currentUser = new User(userId, fName, lName, username);
            return true;
        }
        return false;
    }

    public void login(User user) {
        currentUser = user;
        this.isLoggedIn = true;

        SharedPreferences prefs = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(KEY_USER_ID, user.getId());
        editor.putString(KEY_USERNAME, user.getEmail());
        editor.putString(KEY_FNAME, user.getFName());
        editor.putString(KEY_LNAME, user.getLName());
        editor.apply();
    }

    public boolean restoreSession(DatabaseConnection dbHelper) {
        SharedPreferences prefs = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        long userId = prefs.getLong(KEY_USER_ID, -1);
        if (userId != -1) {
            User user = dbHelper.getUserById(userId);
            if (user != null) {
                login(user); // now no need to pass context
                return true;
            } else {
                prefs.edit().clear().apply(); // clean up
            }
        }
        return false;
    }

    public void logout() {
        currentUser = null;
        isLoggedIn = false;
        SharedPreferences prefs = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
    }

    public User getUser() {
        SharedPreferences prefs = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String username = prefs.getString(KEY_USERNAME, null);
        if (username != null) {
            return DatabaseConnection.getInstance().getUserByEmail(username);
        }
        return null;
    }
}
