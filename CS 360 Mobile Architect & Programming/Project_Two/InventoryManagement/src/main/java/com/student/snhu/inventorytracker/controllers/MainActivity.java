/*
 * Andres Trujillo
 * 04/20/2025
 * SNHU
 * Final Project
 */
package com.student.snhu.inventorytracker.controllers;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.student.snhu.inventorytracker.R;
import com.student.snhu.inventorytracker.database.DatabaseConnection;
import com.student.snhu.inventorytracker.model.User;
import com.student.snhu.inventorytracker.session.AuthManager;


public class MainActivity extends AppCompatActivity {

    private static final int SMS_PERMISSION_REQUEST_CODE = 1001;
    AuthManager authorization;
    DatabaseConnection db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //this.deleteDatabase("inventory.db");
        DatabaseConnection.init(this);
        AuthManager.init(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CheckForLogin();
    }

    private void CheckForLogin() {

        db = DatabaseConnection.getInstance();
        authorization = AuthManager.getInstance(); //TODO IMPLEMENT LOGGING BACK IN
        boolean isLoggedIn = AuthManager.getInstance().restoreSession(db);

        if (isLoggedIn) {

            navigateToHome();

        } else {

            setupButtons();
        }
    }

    private void setupButtons() {
        //find buttons
        Button loginButton = findViewById(R.id.mainActivity_btn_login);
        Button createAccountButton = findViewById(R.id.mainActivity_btn_create_account);

        //Click listener for login
        loginButton.setOnClickListener(view -> login());

        //Click Listener for account creation
        createAccountButton.setOnClickListener(view -> openCreateAccountScreen());
    }


    private void login(){

        User loggingIn = null;

        EditText user = findViewById(R.id.username_input);
        String user_ = user.getText().toString();

        EditText pass = findViewById(R.id.password_input);
        String pass_ = pass.getText().toString();

        //DatabaseConnection db_helper = DatabaseConnection.getInstance();
        User _user = db.getUserByEmailAndPassword(user_, pass_);
        if (_user != null){

            boolean password_correct = db.checkUserCredentials(_user);
            if (password_correct) {

                //Go to inventory page and pull database inventory.
                Intent intent = new Intent(MainActivity.this, InventoryActivity.class);
                //if (User.fullUserAttributes(_user)){
                authorization.login(_user);
                //}

                startActivity(intent);
                finish();

            }else {
                Toast.makeText(this, "User password incorrect, try again.", Toast.LENGTH_SHORT).show();
                return;
            }

        } else {
            Toast.makeText(this, "User account not found, try again.", Toast.LENGTH_SHORT).show();
            return;

        }
        navigateToHome();

    }

    private void navigateToHome(){
        Intent new_intent = new Intent(MainActivity.this, InventoryActivity.class);
        startActivity(new_intent);
        finish();
    }

    private void openCreateAccountScreen(){
        //Display a message with successful creation
        //then navigateToHome()
//        new AlertDialog.Builder(this)
//                .setTitle("SMS Permission Required")
//                .setMessage("To notify someone via text when an inventory item is low or out of stock, this app needs permission to send SMS messages. Do you want to allow this?")
//                .setPositiveButton("Allow", (dialog, which) -> {
//                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_REQUEST_CODE);
//                })
//                .setNegativeButton("Deny", (dialog, which) -> {
//                    // Optional: mark in SharedPreferences that SMS is not allowed
//                    dialog.dismiss();
//                })
//                .show();
        String TAG = "Main Activity";
        Log.d(TAG, "Create Account button clicked");
        System.out.println("Create Account button clicked");
        Toast.makeText(this, "Create Account clicked", Toast.LENGTH_SHORT).show();


        Intent createAccountIntent = new Intent(MainActivity.this, CreateAccountActivity.class);
        startActivity(createAccountIntent);
        finish();
    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permission granted, proceed to account creation
//                homeActivity();
//            } else {
//                // Permission denied, show a message or proceed without SMS functionality
//                Toast.makeText(this, "SMS permission denied. SMS alerts will not be available.", Toast.LENGTH_LONG).show();
//                homeActivity();
//            }
//        }
//    }
}
