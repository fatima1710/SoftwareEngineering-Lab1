
package com.example.lab2;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class FailedAttempt {

    private final String email;

    private final BlockingQueue<AttemptTracker.FailureStatus> replyQueue =
            new ArrayBlockingQueue<>(1);

    public FailedAttempt(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void sendResult(AttemptTracker.FailureStatus result)
            throws InterruptedException {
        replyQueue.put(result);
    }

    public AttemptTracker.FailureStatus waitForResult()
            throws InterruptedException {
        return replyQueue.take();
    }
}
