package com.example.demo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTest {

    @Test
    public void testRegisterAndValidateUser() {
        // Arrange
        Database db = Database.getInstance();
        String username = "testuser";
        String password = "testpass";

        // Act
        boolean added = db.registerUser(username, password);
        boolean isValid = db.validateUser(username, password);

        // Assert
        assertTrue(added || isValid);
    }

    @Test
    public void testRegisterAndValidateAdmin() {
        // Arrange
        Database db = Database.getInstance();
        String username = "adminTest";
        String password = "adminPass";

        // Act
        boolean added = db.registerAdmin(username, password);
        boolean isValid = db.validateAdmin(username, password);

        // Assert
        assertTrue(added || isValid);
    }

    @Test
    public void testInvalidLogin() {
        // Arrange
        Database db = Database.getInstance();

        // Act
        boolean result = db.validateUser("nonexistent", "wrongpass");

        // Assert
        assertFalse(result);
    }
}
