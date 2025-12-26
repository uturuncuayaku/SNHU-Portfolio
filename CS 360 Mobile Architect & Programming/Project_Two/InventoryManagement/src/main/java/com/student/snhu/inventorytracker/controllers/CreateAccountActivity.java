package com.student.snhu.inventorytracker.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.student.snhu.inventorytracker.R;
import com.student.snhu.inventorytracker.model.User;
import com.student.snhu.inventorytracker.database.DatabaseConnection;
import com.student.snhu.inventorytracker.session.AuthManager;

public class CreateAccountActivity extends AppCompatActivity {
    protected User newUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //Bundle information
        super.onCreate(savedInstanceState);
        //Create the view, which sets the layout
        setContentView(R.layout.activity_create_account);

        //Creating button listener
        Button submit_acct = findViewById(R.id.btnCreateAccount);
        //Capture user information after hitting submit button
        createSubmitButtonListener(submit_acct);
    }

    private void createSubmitButtonListener(Button submit) {
        submit.setOnClickListener(v -> {
            submit.setEnabled(false);
            captureUserInfo();
            submit.setEnabled(true); // Re-enable after processing if needed
        });
    }


    private void captureUserInfo() {
        newUser = new User();
        //getting first name
        EditText fName = findViewById(R.id.etFirstName);
        newUser.setFName(fName.getText().toString());
        //getting last name
        EditText lName = findViewById(R.id.etLastName);
        newUser.setLName(lName.getText().toString());
        //getting email address
        EditText email = findViewById(R.id.etEmail);
        newUser.setEmail(email.getText().toString());
        //comparing passwords
        EditText pass1 = findViewById(R.id.etPassword);
        EditText pass2 = findViewById(R.id.etConfirmPassword);
        boolean pass_match = User.verifyPW(pass1.getText().toString(), pass2.getText().toString());
        //
        if (!pass_match){
            TextView pass_mismatch = findViewById(R.id.create_acct_password_fail);
            pass_mismatch.setVisibility(View.VISIBLE);

        } else {
            newUser.setPassword(pass1.getText().toString());
            setUpDatabaseWithNewUser();

        }

    }
    private void setUpDatabaseWithNewUser(){
        //First time user
        //Create User within users table
        DatabaseConnection db_helper = DatabaseConnection.getInstance();
        long db_user_id = db_helper.insertUser(newUser);
        //Now stay logged in with current user
        AuthManager auth = AuthManager.getInstance();
        auth.login(newUser);

        if (db_user_id == -1) {
            String TAG = "CreateAccountActivity::setUpDatabase_NewUser function:";
            Log.d(TAG, "Login button clicked");
            Toast.makeText(this, "User not created, error.", Toast.LENGTH_SHORT).show();
            return;
        }

        //TODO: TEST! logout login again
        newUser.setId(db_user_id);
        launchInventoryPage();
    }

    private void launchInventoryPage(){
        Intent inventory_page = new Intent(CreateAccountActivity.this, InventoryActivity.class);
        startActivity(inventory_page);
        finish();
    }
}
