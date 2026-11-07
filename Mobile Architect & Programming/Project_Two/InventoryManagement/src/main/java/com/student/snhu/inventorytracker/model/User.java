/*
 * Andres Trujillo
 * 04/20/2025
 * SNHU
 * Final Project
 */

package com.student.snhu.inventorytracker.model;

import androidx.annotation.NonNull;

public class User {
    private long id;
    private String email;
    private String password;
    private String fName;
    private String lName;
    private long inventoryTableID;

    public User(){
        //empty
        this.id = -101;
    }

    public User(String username, String password){
        this.email = username;
        this.password = password;
    }
    public User(long id, String fName, String lName, String username){
        this.id = id;
        this.fName = fName;
        this.lName = lName;
        this.email = username;
    }
    public User(long id, String fName, String lName, String username, String password, long itemsTable_id){
        this.id = id;
        this.fName = fName;
        this.lName = lName;
        this.email = username;
        this.password = password;
        this.inventoryTableID = itemsTable_id;
    }

    public User(long id, String email) {
        this.id = id;
        this.email = email;
    }
    @Override
    public String toString() {
        return "User{id=" + id + ", fName='" + fName + "', lName='" + lName + "', email='" + email + "', password='" + password + "'}";
    }

    public static boolean verifyPW(@NonNull String one, String two){
        return one.equals(two);
    }

    // Getters
    public long getId() {
        return this.id;
    }
    public String getEmail() { return this.email; }
    public String getFName(){return this.fName;}
    public String getLName(){return this.lName;}

    public String getPassword() {
        return password;
    }

    // ******Setters*******
    public void setId(long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFName(String name) {
        this.fName = name;
    }

    public void setLName(String name){ this.lName = name; }
    public void setPassword(String password) {
        this.password = password;
    }

}
