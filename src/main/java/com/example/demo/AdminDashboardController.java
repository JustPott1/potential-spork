package com.example.demo;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AdminDashboardController {

    @FXML private TextField newUsernameField;
    @FXML private PasswordField newPasswordField;
    @FXML private TextField newAdminUsernameField;
    @FXML private PasswordField newAdminPasswordField;
    @FXML private ListView<String> userListView;
    @FXML private Label statusLabel;

    private final Database db = Database.getInstance();

    @FXML
    public void initialize() {
        loadUserList();
    }

    private void loadUserList() {
        List<String> usernames = new ArrayList<>();
        String sql = "SELECT username FROM users";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                usernames.add(rs.getString("username"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        userListView.setItems(FXCollections.observableArrayList(usernames));
    }

    @FXML
    public void handleAddUser() {
        String username = newUsernameField.getText().trim();
        String password = newPasswordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showStatus("Velden mogen niet leeg zijn.", "red");
            return;
        }

        if (db.registerUser(username, password)) {
            showStatus("Gebruiker toegevoegd!", "#00FF00");
            newUsernameField.clear();
            newPasswordField.clear();
            loadUserList();
        } else {
            showStatus("Gebruiker bestaat al of fout bij toevoegen.", "red");
        }
    }

    @FXML
    public void handleAddAdmin() {
        String username = newAdminUsernameField.getText().trim();
        String password = newAdminPasswordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showStatus("Adminvelden mogen niet leeg zijn.", "red");
            return;
        }

        if (db.registerAdmin(username, password)) {
            showStatus("Administrator toegevoegd!", "#00FF00");
            newAdminUsernameField.clear();
            newAdminPasswordField.clear();
        } else {
            showStatus("Admin bestaat al of fout bij toevoegen.", "red");
        }
    }

    @FXML
    public void handleRemoveSelectedUser() {
        String selectedUser = userListView.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showStatus("Selecteer een gebruiker om te verwijderen.", "red");
            return;
        }

        String sql = "DELETE FROM users WHERE username=?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, selectedUser);
            int affected = stmt.executeUpdate();

            if (affected > 0) {
                showStatus("Gebruiker verwijderd.", "#00FF00");
                loadUserList();
            } else {
                showStatus("Gebruiker niet gevonden.", "red");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showStatus("Fout bij verwijderen.", "red");
        }
    }

    private void showStatus(String msg, String color) {
        statusLabel.setText(msg);
        statusLabel.setStyle("-fx-text-fill: " + color);
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(e -> statusLabel.setText(""));
        pause.play();
    }
}
