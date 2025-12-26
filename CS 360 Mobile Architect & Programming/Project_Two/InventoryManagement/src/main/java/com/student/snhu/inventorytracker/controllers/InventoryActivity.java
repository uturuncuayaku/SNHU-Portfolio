package com.student.snhu.inventorytracker.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;
import com.student.snhu.inventorytracker.R;
import com.student.snhu.inventorytracker.adapters.InventoryAdapter;
import com.student.snhu.inventorytracker.database.DatabaseConnection;
import com.student.snhu.inventorytracker.model.InventoryItem;
import com.student.snhu.inventorytracker.model.User;
import com.student.snhu.inventorytracker.session.AuthManager;

import java.util.List;

public class InventoryActivity extends AppCompatActivity {

    private AuthManager authorization;
    private User currentUser;
    private InventoryAdapter inventoryAdapter;
    private static final int CREATE_ITEM_REQUEST_CODE = 1;
    private static final int EDIT_ITEM_REQUEST_CODE = 2;
    protected List<InventoryItem> itemList;


    protected InventoryItem clickedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CheckLogin();
        SetView();
        SetToolbar();
        SetNavView();
        SetFooter();

        // If currentUser exists, update email in the navigation drawer and fetch inventory items
        // Update email in navigation drawer
        /*
        Async to get off main thread-
        Get instance of the database
        DatabaseHelper db_helper = DatabaseHelper.getInstance();

        // Update email in the navigation drawer (safe, fast, UI code)
        userEmailTextView.setText(currentUser.getEmail());

        // Fetch inventory items off the main thread
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<InventoryItem> items = db_helper.getInventoryItemsForUser(currentUser);

            runOnUiThread(() -> {
                // Now you're back on the main thread, safe to update UI
                itemList = items;
                inventoryAdapter = new InventoryAdapter(this, itemList, new InventoryAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        InventoryItem clickedItem = itemList.get(position);
                        Toast.makeText(InventoryActivity.this,
                                "Clicked: " + clickedItem.getName(),
                                Toast.LENGTH_SHORT).show();
                    }
                });

                recyclerView.setAdapter(inventoryAdapter);

                // Optional: notify adapter if needed
                inventoryAdapter.notifyDataSetChanged();
            });
        });

         */

        // Set up RecyclerView with GridLayoutManager
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1)); // 1 column in the grid

        // Grabbing the user's inventory items
        DatabaseConnection db_helper = DatabaseConnection.getInstance();

        //Setting nav drawer menu with account text view.

        // Fetch inventory items for the current user
        itemList = db_helper.getInventoryItemsForUser(currentUser);

        // Set up the adapter to display the inventory items in the RecyclerView
        inventoryAdapter = new InventoryAdapter(this, itemList, new InventoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                clickedItem = itemList.get(position);

                Toast.makeText(InventoryActivity.this,
                        "Clicked: " + clickedItem.getName(),
                        Toast.LENGTH_SHORT).show();
            }
        }, new InventoryAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(int position) {
                // Handle item deletion
                InventoryItem itemToDelete = itemList.get(position);
                DatabaseConnection dbHelper = DatabaseConnection.getInstance();
                dbHelper.deleteInventoryItem(itemToDelete);  // Delete item from the database
                inventoryAdapter.deleteItem(position);  // Remove item from the list and notify the adapter
                Toast.makeText(InventoryActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setAdapter(inventoryAdapter);
    }

    private void SetFooter() {
        // Set up buttons for adding, editing, and deleting items
        ImageButton addButton = findViewById(R.id.footer_btn_add);
        ImageButton editButton = findViewById(R.id.footer_btn_edit);
        // Set up the "add item" button listener
        createAddButtonListener(addButton);
        createEditButtonListener(editButton);
    }

    private void SetNavView() {

        NavigationView navView = findViewById(R.id.navigation_view);

        currentUser = AuthManager.getInstance().getUser();
        if (currentUser == null || currentUser.getEmail() == null) {

            Log.w("InventoryActivity", "User is null in SetNavView. Redirecting to Login.");
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        TextView userEmailTextView = navView.getHeaderView(0).findViewById(R.id.user_email);
        userEmailTextView.setText(currentUser.getEmail());

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_account){
                    //logout
                    authorization.logout();
                    Intent intent = new Intent(InventoryActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                } else if (id == R.id.nav_inventory){
                    Intent intent = new Intent(InventoryActivity.this, SMSActivity.class);
                    startActivity(intent);
                }
                DrawerLayout drawerLayout = findViewById(R.id.nav_drawer);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    private void SetView() {
        setContentView(R.layout.nav_drawer);
    }

    private void SetToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout myDrawerLayout = findViewById(R.id.nav_drawer);

        // Set up ActionBarDrawerToggle to open/close the navigation drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, myDrawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        myDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void CheckLogin() {
        authorization = AuthManager.getInstance();
        currentUser = authorization.getUser();
        if (currentUser == null){
            Log.w("InventoryActivity", "Unauthorized access detected. Redirecting to login.");
            authorization.logout();
            Intent intent = new Intent(InventoryActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        //authorization.login(currentUser);
    }


    private void createAddButtonListener(ImageButton addButton) {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the activity to create a new inventory item
                Intent addItemIntent = new Intent(InventoryActivity.this, CreateInventoryItem.class);
                startActivityForResult(addItemIntent, CREATE_ITEM_REQUEST_CODE);
            }
        });
    }


    private void createEditButtonListener(ImageButton editButton) {
        editButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (clickedItem != null) {
                    Intent editIntent = new Intent(InventoryActivity.this, EditActivity.class);
                    editIntent.putExtra("selected_item", clickedItem);
                    startActivityForResult(editIntent, EDIT_ITEM_REQUEST_CODE);
                } else {
                    Toast.makeText(InventoryActivity.this, "Please select an item to edit", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Handle the result when a new item is created
        if (requestCode == CREATE_ITEM_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            InventoryItem newItem = (InventoryItem) data.getSerializableExtra("new_item");
            if (newItem != null) {
                inventoryAdapter.addItem(newItem); // Add the new item to the RecyclerView
            } else {
                Toast.makeText(this, "Failed to create item", Toast.LENGTH_SHORT).show();
            }
        }

        // Handle item edit
        if (requestCode == EDIT_ITEM_REQUEST_CODE && resultCode == RESULT_OK) {
            DatabaseConnection db = DatabaseConnection.getInstance();
            itemList.clear(); // clear the current list
            itemList.addAll(db.getInventoryItemsForUser(currentUser)); // re-fetch updated data
            inventoryAdapter.notifyDataSetChanged(); // update the UI
        }
    }
}
