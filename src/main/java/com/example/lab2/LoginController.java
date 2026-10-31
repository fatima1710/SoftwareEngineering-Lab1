package com.example.lab2;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import java.util.ArrayList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        // load valid users from the input file
        ArrayList<User> users = UsersApp.loadUsers();

        boolean loginSuccessful = false;

        for (User user : users) {
            if (email.equals(user.getName()) &&
                    password.equals(user.getPassword())) {

                loginSuccessful = true;
                break;
            }
        }

        if (loginSuccessful) {
            openWelcomeScreen();
            // close the login window after a successful login
            Stage loginStage = (Stage) emailField.getScene().getWindow();
            loginStage.close();
        } else {
            errorLabel.setText("user or password do not match");
        }
    }

    // opens the Welcome screen after a successful login
    private void openWelcomeScreen() {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/welcome.fxml")
            );

            Stage stage = new Stage();
            stage.setTitle("Welcome");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}