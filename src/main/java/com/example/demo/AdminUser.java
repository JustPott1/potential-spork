package com.example.demo;

public class AdminUser extends AppUser {

    public AdminUser(String username, String password) {
        super(username, password);
    }

    @Override
    public boolean validate(Database db) {
        return db.validateAdmin(username, password);
    }
}
