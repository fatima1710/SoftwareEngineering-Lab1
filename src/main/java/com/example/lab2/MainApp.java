
package com.example.lab2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Scanner;

public class MainApp extends Application {

    private static int n;
    private static int t;

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/login.fxml")
        );

        Scene scene = new Scene(loader.load());

        stage.setTitle("User Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        if (args.length == 2) {
            try {
                n = Integer.parseInt(args[0]);
                t = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                n = 0;
                t = 0;
            }
        }

        while (n < 1 || t < 1) {
            System.out.println("Enter n (max failed attempts):");
            n = readPositiveInteger(scanner);

            System.out.println("Enter t (blocking time in seconds):");
            t = readPositiveInteger(scanner);
        }
        System.out.println("n = " + n);
        System.out.println("t = " + t);
        launch(args);
    }

    private static int readPositiveInteger(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine();

            try {
                int value = Integer.parseInt(input);

                if (value >= 1) {
                    return value;
                }
            } catch (NumberFormatException e) {
                // Invalid number
            }

            System.out.println("Please enter a positive integer:");
        }
    }

    public static int getN() {
        return n;
    }

    public static int getT() {
        return t;
    }
}

