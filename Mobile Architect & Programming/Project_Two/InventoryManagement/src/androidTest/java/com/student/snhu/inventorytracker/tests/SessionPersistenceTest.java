package com.student.snhu.inventorytracker.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import androidx.test.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import android.content.Context;

import com.student.snhu.inventorytracker.database.DatabaseConnection;
import com.student.snhu.inventorytracker.model.InventoryItem;
import com.student.snhu.inventorytracker.model.User;
import com.student.snhu.inventorytracker.session.AuthManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SessionPersistenceTest {
    private Context context;
    private AuthManager auth;
    private DatabaseConnection db;

    @Before
    public void setup(){
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        DatabaseConnection.init(context);
        AuthManager.init(context);

    }

    @Test
    public void testSessionPersistenceAfterLogoutAndLogin(){

        //Creating user
        User test_user = new User("test@example.com", "password123");
        //Getting database
        db = DatabaseConnection.getInstance();
        //Log recently created user in to the application
        auth.login(test_user);
        //Add user to db
        long user_row_id = db.insertUser(test_user);
        assertTrue(user_row_id != -1);
        //Create item
        InventoryItem test_item = new InventoryItem("legos","little blocks", 50, true);
        //assertTrue(db.insertInventoryItemForUser(test_item, test_user.getId()));

        //logout after adding test_item
        auth.logout();
        assertFalse(auth.isLoggedIn());

        auth.login(test_user);

    }

}
