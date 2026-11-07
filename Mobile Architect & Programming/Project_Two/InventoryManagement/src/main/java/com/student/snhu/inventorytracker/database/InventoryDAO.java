package com.student.snhu.inventorytracker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.student.snhu.inventorytracker.model.InventoryItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for handling operations on the inventory.
 * Provides methods to insert, retrieve, and close the database.
 */
public class InventoryDAO {

    private final SQLiteDatabase database;
    private final DatabaseConnection dbHelper;

    /**
     * Constructor to initialize the InventoryDAO with the given context.
     * @param context the context of the calling component (e.g. Activity)
     */
    public InventoryDAO(Context context) {
        // Use DatabaseHelper's singleton instance to ensure only one instance of the database helper.
        dbHelper = DatabaseConnection.getInstance();
        database = dbHelper.getWritableDatabase();
    }

    /**
     * Close the database connection properly when no longer needed.
     */
    public void close() {
        dbHelper.close();
    }

    /**
     * Retrieve all inventory items from the database.
     * @return a list of all InventoryItems in the database
     */
    public List<InventoryItem> getAllItems() {
        List<InventoryItem> items = new ArrayList<>();
        Cursor cursor = null;

        try {
            // Query the inventory table for all rows
            cursor = database.query("inventory", null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
                boolean notifications = cursor.getInt(cursor.getColumnIndexOrThrow("notifications")) == 1;

                // Create a new InventoryItem and add it to the list
                InventoryItem item = new InventoryItem(name, description, quantity, notifications);
                items.add(item);
            }
        } finally {
            // Ensure cursor is closed even if an exception occurs
            if (cursor != null) {
                cursor.close();
            }
        }

        return items;
    }

    /**
     * Insert a new InventoryItem into the database.
     * @param item the InventoryItem to be inserted
     */
    public void insertItem(InventoryItem item) {
        ContentValues values = new ContentValues();
        values.put("name", item.getName());
        values.put("description", item.getItemDescription());
        values.put("quantity", item.getQuantity());
        values.put("notifications", item.getSMSEnabled() ? 1 : 0);

        // Insert the item into the "inventory" table
        database.insert("inventory", null, values);
    }
}
