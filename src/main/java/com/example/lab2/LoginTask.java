
package com.example.lab2;

import javafx.application.Platform;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;

public class LoginTask implements Runnable {

    private final String email;
    private final String password;
    private final AttemptTracker tracker;
    private final BlockingQueue<FailedAttempt> queue;
    private final Consumer<LoginResult> onResult;

    public LoginTask(
            String email,
            String password,
            AttemptTracker tracker,
            BlockingQueue<FailedAttempt> queue,
            Consumer<LoginResult> onResult
    ) {
        this.email = email;
        this.password = password;
        this.tracker = tracker;
        this.queue = queue;
        this.onResult = onResult;
    }

    @Override
    public void run() {
        try {
            // Read valid users on a background thread.
            ArrayList<User> users = UsersApp.loadUsers();

            User matchingUser = null;

            for (User user : users) {
                if (email.equals(user.getName())) {
                    matchingUser = user;
                    break;
                }
            }

            // Unknown emails are not tracked.
            if (matchingUser == null) {
                sendResult(new LoginResult(
                        LoginResult.Status.ERROR, -1, 0
                ));
                return;
            }

            // Check blocking in a separate Thread.
            BlockCheckThread blockThread =
                    new BlockCheckThread(tracker, email);

            blockThread.start();
            blockThread.join();

            if (blockThread.isBlocked()) {
                sendResult(new LoginResult(
                        LoginResult.Status.BLOCKED,
                        0,
                        blockThread.getRemainingBlockMillis()
                ));
                return;
            }

            // Correct password.
            if (password.equals(matchingUser.getPassword())) {
                tracker.reset(email);

                sendResult(new LoginResult(
                        LoginResult.Status.SUCCESS, 0, 0
                ));
                return;
            }

            // Wrong password for an existing email.
            FailedAttempt attempt = new FailedAttempt(email);
            queue.put(attempt);

            AttemptTracker.FailureStatus status =
                    attempt.waitForResult();

            if (status.remainingBlockMillis() > 0) {
                sendResult(new LoginResult(
                        LoginResult.Status.BLOCKED,
                        0,
                        status.remainingBlockMillis()
                ));
            } else {
                sendResult(new LoginResult(
                        LoginResult.Status.ERROR,
                        status.remainingAttempts(),
                        0
                ));
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void sendResult(LoginResult result) {
        Platform.runLater(() -> onResult.accept(result));
    }
}
