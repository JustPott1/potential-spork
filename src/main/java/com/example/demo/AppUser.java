package com.example.demo;

public abstract class AppUser {
    protected String username;
    protected String password;

    public AppUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public abstract boolean validate(Database db);

}
