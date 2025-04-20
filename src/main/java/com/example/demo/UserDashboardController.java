package com.example.demo;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.awt.Desktop;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UserDashboardController {

    @FXML private TextField linkField;
    @FXML private Label statusLabel;

    @FXML
    public void handleGenerateQRCode() {
        String url = linkField.getText().trim();
        if (url.isEmpty()) {
            showStatus("Voer eerst een link in.", "red");
            return;
        }

        Path downloads = Paths.get(System.getProperty("user.home"), "Downloads");
        Path file = downloads.resolve("qrcode.png");

        QRGenerator generator = new URLQRGenerator(); // polymorfisme

        try {
            generator.generate(url, file);
            showStatus("QR opgeslagen in Downloads", "#00FF00");

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file.toFile());
            }
        } catch (Exception e) {
            e.printStackTrace();
            showStatus("Fout bij genereren", "red");
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