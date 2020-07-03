package com.traffic.analysis.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AlertHelper {
    /**
     * Show an alert given the parameters.
     * @param type The type of alert.
     * @param title The title of the alert.
     * @param message The message of the alert.
     */
    public void showAlert(Alert.AlertType type, String title, String message){
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.setTitle(title);
        alert.show();
    }

    /**
     * Show an alert and wait for user input.
     * @param type The type of alert.
     * @param title The title of the alert.
     * @param message The message of the alert.
     * @return The result of waiting for user input.
     */
    public Optional<ButtonType> showAlertAndWait(Alert.AlertType type, String title, String message){
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.setTitle(title);
        return alert.showAndWait();
    }

    /**
     * Convenience method to show success alert.
     * @param message Success message.
     */
    public void showSuccessAlert(String message){
        this.showAlert(Alert.AlertType.INFORMATION, "Success", message);
    }

    /**
     * Convenience method to show error message.
     * @param message Error message.
     */
    public void showErrorAlert(String message){
        this.showAlert(Alert.AlertType.ERROR, "Error", message);
    }
}
