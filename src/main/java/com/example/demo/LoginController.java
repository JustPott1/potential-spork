package com.example.demo;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final Database db = Database.getInstance();

    @FXML
    public void handleLoginButton(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        AppUser user = new AdminUser(username, password);
        if (user.validate(db)) {
            switchToDashboard("admindashboard.fxml", "Admin Dashboard");
            return;
        }

        user = new RegularUser(username, password);
        if (user.validate(db)) {
            switchToDashboard("userdashboard.fxml", "Gebruiker Dashboard");
            return;
        }

        showError("Ongeldige gebruikersnaam of wachtwoord.");
    }

    private void switchToDashboard(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
        } catch (Exception e) {
            e.printStackTrace();
            showError("Kon scherm niet laden.");
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(e -> errorLabel.setText(""));
        pause.play();
    }
}
