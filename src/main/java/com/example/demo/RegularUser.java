package com.example.demo;

public class RegularUser extends AppUser {

    public RegularUser(String username, String password) {
        super(username, password);
    }

    @Override
    public boolean validate(Database db) {
        return db.validateUser(username, password);
    }
}
