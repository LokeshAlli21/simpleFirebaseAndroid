package com.example.goodlifegym;

public class AdminAuthModel {
    String name,email,pass,userID;

    public AdminAuthModel() {
    }

    public AdminAuthModel(String name, String email, String pass, String userID) {
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
