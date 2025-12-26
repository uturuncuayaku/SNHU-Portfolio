/*
 * Andres Trujillo
 * 4/20/2025
 * SNHU
 * Final Project
 */
package com.student.snhu.inventorytracker.controllers;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.student.snhu.inventorytracker.R;


public class SMSActivity extends AppCompatActivity {

    private Switch smsSwitch;
    private Button requestPermissionsButton;
    // Activity Result Launcher for requesting permissions
    private ActivityResultLauncher<String[]> requestPermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_settings);

        // Initialize Switch and Button
        smsSwitch = findViewById(R.id.sms_int_switch);
        requestPermissionsButton = findViewById(R.id.request_permissions_button);

        // Initialize the ActivityResultLauncher for permissions
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                permissions -> {
                    // Check if all permissions were granted
                    boolean allPermissionsGranted = true;
                    for (Boolean isGranted : permissions.values()) {
                        if (!isGranted) {
                            allPermissionsGranted = false;
                            break;
                        }
                    }

                    // Notify user about the result
                    if (allPermissionsGranted) {
                        Snackbar.make(smsSwitch, "Permissions granted", Snackbar.LENGTH_LONG).show();
                    } else {
                        Snackbar.make(smsSwitch, "Permissions denied", Snackbar.LENGTH_LONG).show();
                        smsSwitch.setChecked(false); // Uncheck switch if permissions are denied
                    }
                });

        // Set listener for the switch to enable/disable permissions request button
        smsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Check permissions
                if (arePermissionsGranted()) {
                    Snackbar.make(smsSwitch, "Permissions already granted", Snackbar.LENGTH_SHORT).show();
                } else {
                    // Show the button to request permissions
                    requestPermissionsButton.setVisibility(View.VISIBLE);
                }
            } else {
                // Hide the request permissions button when switch is off
                requestPermissionsButton.setVisibility(View.GONE);
            }
        });

        // Set listener for the button to request permissions
        requestPermissionsButton.setOnClickListener(v -> requestPermissions());

    }


    // Helper function to check if the necessary permissions are granted
    private boolean arePermissionsGranted() {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_NUMBERS) == PackageManager.PERMISSION_GRANTED;
    }

    // Request the necessary permissions
    private void requestPermissions() {
        requestPermissionLauncher.launch(new String[]{
                android.Manifest.permission.SEND_SMS,
                android.Manifest.permission.READ_PHONE_STATE,
                android.Manifest.permission.READ_PHONE_NUMBERS
        });
    }

    // Handle permission result callback if needed
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // You can handle result here if using older permission APIs.
    }


}
