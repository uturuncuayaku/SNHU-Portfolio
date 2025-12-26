package com.student.snhu.inventorytracker.controllers;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import com.student.snhu.inventorytracker.R;
import com.student.snhu.inventorytracker.database.DatabaseConnection;
import com.student.snhu.inventorytracker.model.InventoryItem;
import com.student.snhu.inventorytracker.model.User;
import com.student.snhu.inventorytracker.session.AuthManager;


public class CreateInventoryItem extends AppCompatActivity {
    private AuthManager authorization;
    User currentUser;
    private EditText itemNameInput, itemDescriptionInput, itemQuantityInput;
    private ToggleButton smsToggle;
    private boolean smsEnabled;
    private static final int SMS_PERMISSION_REQUEST_CODE = 1001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_item);
        authorization = AuthManager.getInstance();
        currentUser = authorization.getUser();

        itemNameInput = findViewById(R.id.item_name_input);
        itemDescriptionInput = findViewById(R.id.item_description_input);
        itemQuantityInput = findViewById(R.id.item_quantity_input);
        smsToggle = findViewById(R.id.createItemActivity_smsTgl);

        smsToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.SEND_SMS},
                            SMS_PERMISSION_REQUEST_CODE);

                    // Optionally turn it back off until permission granted
                    smsToggle.setChecked(false);
                } else {
                    Toast.makeText(this, "SMS Notifications Enabled", Toast.LENGTH_SHORT).show();
                    // TODO: Save this state for use when sending notifications
                    smsEnabled = true;
                }
            } else {
                Toast.makeText(this, "SMS Notifications Disabled", Toast.LENGTH_SHORT).show();
            }
        });

        Button saveButton = findViewById(R.id.save_button);
        createSaveButtonListener(saveButton);
    }

    private void createSaveButtonListener(Button saveButton) {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                saveItem();
            }
        });
    }

    private void saveItem() {
        String itemName =        itemNameInput.getText().toString().trim();
        String itemDescription = itemDescriptionInput.getText().toString().trim();
        String itemQuantity_in = itemQuantityInput.getText().toString().trim();
        int itemQuantityIn = 0;

        // Validate inputs
        if (itemName.isEmpty()) {
            itemNameInput.setError("Item name is required");
            return;
        }
        if (itemDescription.isEmpty()) {
            itemDescriptionInput.setError("Item description is required");
            return;
        }
        if (itemQuantity_in.isEmpty()) {
            itemQuantityInput.setError("Quantity is required");
            return;
        }
        try {
            itemQuantityIn = Integer.parseInt(itemQuantity_in);
            if (itemQuantityIn < 0){
                itemQuantityInput.setError("Must be greater than zero!");
                return;
            }
        } catch (NumberFormatException nfe){
            itemQuantityInput.setError("Enter a valid number!");
            return;
        }


        Log.d("CreateInventoryItem::saveItem() function", "Saving item!");
        Toast.makeText(this, "Saved item.", Toast.LENGTH_SHORT).show();

        // Save item to internal database
        DatabaseConnection db_helper = DatabaseConnection.getInstance();
        InventoryItem item = new InventoryItem(itemName,itemDescription,itemQuantityIn,smsEnabled);
        item = db_helper.insertInventoryItem(item, currentUser.getId());

        //Return item to populate the Recycler view
        Intent resultIntent = new Intent();
        resultIntent.putExtra("new_item", item);
        setResult(AppCompatActivity.RESULT_OK, resultIntent);

        // return to previous screen.
        finish();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted. SMS Notifications Enabled.", Toast.LENGTH_SHORT).show();
                smsToggle.setChecked(true);
            } else {
                Toast.makeText(this, "Permission denied. SMS will not be sent.", Toast.LENGTH_LONG).show();
                smsToggle.setChecked(false);
            }
        }
    }

}
