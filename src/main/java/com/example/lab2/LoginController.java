
package com.example.lab2;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    private Thread countdownThread;
    private boolean closed = false;

    @FXML
    private void handleLogin(ActionEvent event) {

        String email = emailField.getText();
        String password = passwordField.getText();

        Button loginButton = (Button) event.getSource();

        setLoginDisabled(loginButton, true);
        errorLabel.setText("");

        // Start login processing in a background thread.
        LoginTask task = new LoginTask(
                email,
                password,
                MainApp.getTracker(),
                MainApp.getQueue(),
                result -> handleResult(result, loginButton)
        );

        Thread loginThread = new Thread(task, "LoginTask");
        loginThread.setDaemon(true);
        loginThread.start();
    }

    private void handleResult(
            LoginResult result,
            Button loginButton
    ) {
        if (closed) {
            return;
        }

        switch (result.status()) {

            case SUCCESS:
                if (openWelcomeScreen()) {
                    Stage loginStage =
                            (Stage) emailField.getScene().getWindow();

                    loginStage.close();
                } else {
                    errorLabel.setText("Could not open Welcome screen");
                    setLoginDisabled(loginButton, false);
                }
                break;

            case ERROR:
                if (result.remainingAttempts() < 0) {
                    errorLabel.setText("Invalid credentials");
                } else {
                    errorLabel.setText(
                            "Invalid credentials. Attempts left: "
                                    + result.remainingAttempts()
                    );
                }

                setLoginDisabled(loginButton, false);
                break;

            case BLOCKED:
                startCountdown(
                        result.remainingBlockMillis(),
                        loginButton
                );
                break;
        }
    }

    private void startCountdown(
            long remainingMillis,
            Button loginButton
    ) {
        if (countdownThread != null) {
            countdownThread.interrupt();
        }

        CountdownTask countdownTask = new CountdownTask(
                remainingMillis,

                seconds -> {
                    if (!closed) {
                        errorLabel.setText(
                                "Account blocked. "
                                        + seconds
                                        + " seconds remaining"
                        );
                    }
                },

                () -> {
                    if (!closed) {
                        errorLabel.setText("");
                        setLoginDisabled(loginButton, false);
                    }
                }
        );

        countdownThread =
                new Thread(countdownTask, "CountdownTask");

        countdownThread.setDaemon(true);
        countdownThread.start();
    }

    private void setLoginDisabled(
            Button loginButton,
            boolean disabled
    ) {
        loginButton.setDisable(disabled);
        emailField.setDisable(disabled);
        passwordField.setDisable(disabled);
    }

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

    @FXML
    private void initialize() {
        Platform.runLater(() -> {
            Stage stage =
                    (Stage) emailField.getScene().getWindow();

            stage.setOnHidden(event -> {
                closed = true;

                if (countdownThread != null) {
                    countdownThread.interrupt();
                }
            });
        });
    }
    @FXML
    private void handleConcurrencyTest() {

        errorLabel.setText("Running concurrency test...");

        Thread testThread = new Thread(() -> {
            try {
                String result = ConcurrencyTest.runTest();

                Platform.runLater(() -> {
                    if (!closed) {
                        errorLabel.setText(
                                result.replace("\n", " | ")
                        );
                    }
                });

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();

                Platform.runLater(() -> {
                    if (!closed) {
                        errorLabel.setText("Test interrupted");
                    }
                });
            }
        }, "ConcurrencyTest");

        testThread.setDaemon(true);
        testThread.start();
    }
}
