package com.student.snhu.inventorytracker.tests;

import static org.junit.Assert.*;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.student.snhu.inventorytracker.database.DatabaseConnection;
import com.student.snhu.inventorytracker.database.InventoryDAO;
import com.student.snhu.inventorytracker.model.InventoryItem;
import com.student.snhu.inventorytracker.model.User;
import com.student.snhu.inventorytracker.session.AuthManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class InventoryDaoTest {

    private Context context;
    private InventoryDAO inventoryDAO;
    private AuthManager auth;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        DatabaseConnection.init(context); // Initialize the singleton instance
        inventoryDAO = new InventoryDAO(context);
        auth = AuthManager.getInstance();
    }

    @Test
    public void testAddItemPersistenceAcrossSessions() {
        // Login
        User testUser = new User("test@example.com", "password123");
        auth.login(testUser);
        assertTrue(auth.isLoggedIn());

        // Insert item
        InventoryItem item = new InventoryItem("Water Bottle", "Insulated steel", 2, true);
        inventoryDAO.insertItem(item);

        // Logout
        auth.logout();
        assertFalse(auth.isLoggedIn());

        // Login again
        auth.login(testUser);
       // assertTrue(auth.restoreSession(context.getApplicationContext(), db));

        // Retrieve and verify item
        List<InventoryItem> items = inventoryDAO.getAllItems();
        assertFalse(items.isEmpty());

        boolean found = false;
        for (InventoryItem i : items) {
            if (i.getName().equals("Water Bottle") &&
                    i.getItemDescription().equals("Insulated steel") &&
                    i.getQuantity() == 2 &&
                    i.getSMSEnabled()) {
                found = true;
                break;
            }
        }

        assertTrue("Inserted item should persist across sessions", found);
        inventoryDAO.close(); // Clean up
    }
}
