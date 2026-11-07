/*
 * Andres Trujillo
 * 04/20/2025
 * SNHU
 * Final Project
 */
package com.student.snhu.inventorytracker.controllers;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.student.snhu.inventorytracker.R;
import com.student.snhu.inventorytracker.database.DatabaseConnection;
import com.student.snhu.inventorytracker.model.InventoryItem;

public class EditActivity extends AppCompatActivity {

    InventoryItem selectedItem;
    EditText nameEdit;
    EditText descEdit;
    EditText quantityEdit;
    ToggleButton smsNotifToggle;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_item);

        // Receiving inventory item
        selectedItem = (InventoryItem) getIntent().getSerializableExtra("selected_item");

        if (selectedItem != null){
            // Get the item fields
            String name = selectedItem.getName();
            String desc = selectedItem.getItemDescription();
            int quant = selectedItem.getQuantity();
            long id = selectedItem.getId();
            boolean notifications = selectedItem.getSMSEnabled();
            long ownerId = selectedItem.getId();

            // Populate the fields in the UI
            nameEdit = findViewById(R.id.item_name_input);
            descEdit = findViewById(R.id.item_description_input);
            quantityEdit = findViewById(R.id.item_quantity_input);
            smsNotifToggle = findViewById(R.id.createItemActivity_smsTgl);

            // Set existing data in the UI fields
            nameEdit.setText(name);
            descEdit.setText(desc);
            quantityEdit.setText(String.valueOf(quant));
            smsNotifToggle.setChecked(notifications);
        }

        // Set up save button to save updated item fields
        Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get updated values from UI
                String updatedName = nameEdit.getText().toString();
                String updatedDesc = descEdit.getText().toString();
                int updatedQuantity = Integer.parseInt(quantityEdit.getText().toString());
                boolean updatedNotifications = smsNotifToggle.isChecked();

                // Update the selectedItem with new values
                selectedItem.setName(updatedName);
                selectedItem.setDescription(updatedDesc);
                selectedItem.setQuantity(updatedQuantity);
                selectedItem.setNotifications((updatedNotifications?1:0));

                // Save the updated item to the database (you can modify this part to call your DB helper)
                DatabaseConnection dbHelper = DatabaseConnection.getInstance();

                int rowsUpdated = dbHelper.updateInventoryItem(selectedItem);
                if (rowsUpdated > 0) {
                    // Success, rows were updated
                    Toast.makeText(EditActivity.this, "Item updated successfully", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();  // Close the activity
                } else {
                    Toast.makeText(EditActivity.this, "Item didn't update.", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_CANCELED);
                    finish();
                }

            }
        });
    }

}
