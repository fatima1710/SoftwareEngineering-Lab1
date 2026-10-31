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

// login controller controls the login process, validates user credentials,
// and opens the welcome screen after a successful authentication
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
            if (openWelcomeScreen()) {
                // close the login window only if "Welcome" opened successfully
                Stage loginStage = (Stage) emailField.getScene().getWindow();
                loginStage.close();
            }
        } else {
            errorLabel.setText("user or password do not match");
        }
    }

    // opens the Welcome screen after a successful login
    private boolean openWelcomeScreen() {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/welcome.fxml")
            );

            Stage stage = new Stage();
            stage.setTitle("Welcome");
            stage.setScene(new Scene(root));
            stage.show();

            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}