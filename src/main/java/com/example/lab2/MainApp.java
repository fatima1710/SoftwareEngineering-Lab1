
package com.example.lab2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MainApp extends Application {

    private static int n;
    private static int t;

    private static AttemptTracker tracker;

    private static final BlockingQueue<FailedAttempt> queue =
            new LinkedBlockingQueue<>();

    private FailureWorker worker1;
    private FailureWorker worker2;

    @Override
    public void start(Stage stage) throws Exception {

        // Shared tracker for all login attempts
        tracker = new AttemptTracker(n, t);

        // Start two background workers
        worker1 = new FailureWorker(
                queue, tracker, "FailureWorker-1"
        );

        worker2 = new FailureWorker(
                queue, tracker, "FailureWorker-2"
        );

        worker1.start();
        worker2.start();

        // Open the Login screen
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/login.fxml")
        );

        Scene scene = new Scene(loader.load());

        stage.setTitle("User Login");
        stage.setScene(scene);
        stage.show();
    }

    public static AttemptTracker getTracker() {
        return tracker;
    }

    public static BlockingQueue<FailedAttempt> getQueue() {
        return queue;
    }

    @Override
    public void stop() {
        // Stop the workers when the app closes
        if (worker1 != null) {
            worker1.interrupt();
        }

        if (worker2 != null) {
            worker2.interrupt();
        }
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
                // Invalid input
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
