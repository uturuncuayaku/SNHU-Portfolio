/*
 * Andres Trujillo
 * 04/20/2025
 * SNHU
 * Final Project
 */
package com.student.snhu.inventorytracker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.student.snhu.inventorytracker.model.InventoryItem;
import com.student.snhu.inventorytracker.model.User;

import java.util.ArrayList;
import java.util.List;

/*
SQLiteOpenHelper
 - manages database creation and version control
 - automatically handles onCreate() and onUpgrade() logic
 - you don't need to manually open/close database files

 */
public class DatabaseConnection extends SQLiteOpenHelper {

    private static DatabaseConnection instance = null;
    private static final String DATABASE_NAME = "inventory.db";
    private static final int DATABASE_VERSION = 1;

    /*
        Constructor
        context - the context calling the database (Activity, Application, etc)
        name - the name of our database file (e.g. "inventory.db")
        factory - using null to enable default cursor
        version - DB version; incremented when schema changes (triggers onUpgrade)
     */
    private DatabaseConnection(@Nullable Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized void init(Context context) {
        if (instance == null) {
            instance = new DatabaseConnection(context.getApplicationContext());
        }
    }
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Call init(context) before getInstance()");
        }
        return instance;
    }

    /*Users account creation */

    public long insertUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("email", user.getEmail());
        values.put("password", user.getPassword());
        long user_row_id = db.insert("users", null, values); // Returns row ID or -1
        if (user_row_id != -1) user.setId(user_row_id);
        return (user_row_id);
    }


    public User getUserById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;

        Cursor cursor = db.query(
                "users",                         // Table name
                new String[]{"id", "email", "password"}, // Columns to return
                "id = ?",                        // WHERE clause
                new String[]{String.valueOf(id)}, // WHERE args
                null, null, null);

        if (cursor.moveToFirst()) {
            user = new User();
            user.setId(cursor.getLong(cursor.getColumnIndexOrThrow("id")));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("username")));
            user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow("password")));
        }
        cursor.close();

        return user;
    }

    public User getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;

        Cursor cursor = db.query(
                "users",                                             // Table name
                new String[]{"id", "email", "password", "fName", "lName"}, // Columns to return
                "email = ?",                                               // WHERE clause
                new String[]{email},                                       // WHERE args
                null, null, null);

        if (cursor.moveToFirst()) {
            user = new User();
            user.setId(cursor.getLong(cursor.getColumnIndexOrThrow("id")));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
            user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow("password")));
            user.setFName(cursor.getString(cursor.getColumnIndexOrThrow("fName")));
            user.setLName(cursor.getString(cursor.getColumnIndexOrThrow("lName")));

        }
        cursor.close();

        return user;
    }



    public boolean checkUserCredentials(User user) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                "users",
                new String[]{"id"},
                "email = ? AND password = ?",
                new String[]{user.getEmail(), user.getPassword()},
                null, null, null
        );

        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }


    public long getUserId(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
          "users",
          new String[]{"id"},
          "email = ?",
          new String[]{username},
          null,null,null
        );
        long userId = -1;
        if (cursor.moveToFirst()){
            userId = cursor.getLong(0);
        }
        cursor.close();
        return userId;
    }

    public User getUserByEmailAndPassword(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        // First get the user
        Cursor userCursor = db.query("users", null,
                "email=? AND password=?", new String[]{email, password},
                null, null, null);

        if (userCursor.moveToFirst()) {
            int userId = userCursor.getInt(userCursor.getColumnIndexOrThrow("id"));
            String fName = userCursor.getString(userCursor.getColumnIndexOrThrow("fName"));
            String lName = userCursor.getString(userCursor.getColumnIndexOrThrow("lName"));


            // Now get the inventory ID linked to this user
            Cursor invCursor = db.query("inventory", new String[]{"id"},
                    "user_id=?", new String[]{String.valueOf(userId)},
                    null, null, null);

            int inventoryId = -1;
            if (invCursor.moveToFirst()) {
                inventoryId = invCursor.getInt(invCursor.getColumnIndexOrThrow("id"));
                invCursor.close();
            }

            userCursor.close();

            return new User(userId, fName, lName, email, password, inventoryId);
        }

        return null;
    }

    /*
        Just for testing purposes
     */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, email, password, fName, lName FROM users", null);

        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow("id"));
                String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
                String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));

                User user = new User(username, password);
                user.setId(id); // if your User object tracks its DB ID
                users.add(user);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return users;
    }

    /*
        Testing purposes.
     */
    public boolean insertInventoryItemForUser(InventoryItem item, long userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("user_id", userId); // associate item to user
        values.put("name", item.getName());
        values.put("description", item.getItemDescription());
        values.put("quantity", item.getQuantity());
        values.put("sms_enabled", item.getSMSEnabled());

        long newId = db.insert("inventory", null, values);
        item.setId(newId);
        //item.setOwnerID(userId);
        return (newId != -1);
    }


    /*CRUD FUNCTIONS*/

    /*
        Insert inventory item
     */
    public InventoryItem insertInventoryItem(InventoryItem item, long id){

        // Get SQLiteDatabase
        SQLiteDatabase db = this.getWritableDatabase();
        // Create android bucket
        ContentValues values = new ContentValues();

        values.put("name", item.getName());
        values.put("description", item.getItemDescription());
        values.put("quantity", item.getQuantity());
        values.put("sms_enabled", item.getSMSEnabled());
        values.put("user_id", id); // associating item to user

        // Insert and get the auto-generated row ID
        long newId = db.insert("inventory", null, values);
        // Update the Inventory item's ID
        item.setRowID(newId);
        return item;
    }

    public List<InventoryItem> getInventoryItemsForUser(User user){

        List<InventoryItem> itemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM inventory WHERE user_id = ?", new String[]{String.valueOf(user.getId())});

        if (cursor.moveToFirst()){

            do {

                InventoryItem item = new InventoryItem();

                item.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                item.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                item.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("description")));
                item.setNotifications(cursor.getInt(cursor.getColumnIndexOrThrow("sms_enabled")));
                item.setQuantity(cursor.getInt(cursor.getColumnIndexOrThrow("quantity")));

                itemList.add(item);

            } while (cursor.moveToNext());
        }

        cursor.close();
        return itemList;
    }

    public int deleteInventoryItem(InventoryItem item){

        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("inventory", "id = ?", new String[]{String.valueOf(item.getId())});

    }

    public int updateInventoryItem(InventoryItem item){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", item.getName());
        values.put("description", item.getItemDescription());
        values.put("sms_enabled", item.getSMSEnabled() ? 1: 0);
        values.put("quantity", item.getQuantity());
        return db.update("inventory", values, "id = ?", new String[]{String.valueOf(item.getId())});
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create Users table
        String createUsersTable = "CREATE TABLE users(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "email TEXT UNIQUE NOT NULL," +
                "password TEXT NOT NULL," +
                "fName TEXT," +
                "lName TEXT);";

        // Create Inventory table with quantity column
        String createInventoryTable = "CREATE TABLE inventory (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER NOT NULL," +
                "name TEXT NOT NULL," +
                "description TEXT," +
                "sms_enabled INTEGER DEFAULT 0," +
                "quantity INTEGER NOT NULL DEFAULT 0," +
                "FOREIGN KEY(user_id) REFERENCES users(id));";

        db.execSQL(createUsersTable);
        db.execSQL(createInventoryTable);
    }



    /*
    Called when version is incremented. Used to ALTER, DROP, or RECREATE tables for schema changes.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Simple strategy to get started
        db.execSQL("DROP TABLE IF EXISTS inventory");
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);

        /*
        TODO: We can use the proper ALTER commands once the application is finished
                because right now it deletes all data.
         */

    }
}
