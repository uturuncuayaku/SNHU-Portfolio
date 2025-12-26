/*
 * Andres Trujillo
 * 04/20/2025
 * SNHU
 * Final Project
 */
package com.student.snhu.inventorytracker.model;

import java.io.Serializable;

public class InventoryItem implements Serializable {

    /*
        Field members
     */
    private String name;
    private String description;
    private int quantity;
    private long ID;
    private boolean notifications;
    private long rowID;

    /*
        Default constructor
     */
    public InventoryItem() {
        this.name = "none";
        this.description = "none";
        this.quantity = -1;
        this.ID = -1;
        this.rowID = -1;
    }


    /*
        Constructor for Inventory item.
     */
    public InventoryItem(String name,
                         String description,
                         int quantity,
                         boolean notifications){
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.notifications = notifications;
        this.rowID = -1;
        this.ID = -1;
    }


    /*
        Getters & Setters
     */
    public void setName(String name){ this.name = name; }
    public String getName() { return this.name;  }
    public long getRowID(){ return this.rowID; }
    public void setRowID(long userID){ this.rowID = userID; }
    public void setDescription(String description){ this.description = description;  }
    public String getItemDescription() { return description; }
    public void setQuantity(int quantity){ this.quantity = quantity;  }
    public int getQuantity() { return this.quantity; }
    public void setId(long id){ this.ID = id; }
    public long getId() { return ID; }
    public void setNotifications(int notifications){ this.notifications = notifications == 1;  }
    public boolean getSMSEnabled(){ return this.notifications; }

}