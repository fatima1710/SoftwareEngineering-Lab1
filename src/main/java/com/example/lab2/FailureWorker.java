
package com.example.lab2;

import java.util.concurrent.BlockingQueue;

public class FailureWorker extends Thread {

    private final BlockingQueue<FailedAttempt> queue;
    private final AttemptTracker tracker;

    public FailureWorker(
            BlockingQueue<FailedAttempt> queue,
            AttemptTracker tracker,
            String workerName
    ) {
        super(workerName);

        this.queue = queue;
        this.tracker = tracker;

        setDaemon(true);
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                // Wait until a failed login attempt arrives.
                FailedAttempt attempt = queue.take();

                // Update the failed-attempt counter.
                AttemptTracker.FailureStatus result =
                        tracker.recordFailure(attempt.getEmail());

                // Return the result to the waiting LoginTask.
                attempt.sendResult(result);

            } catch (InterruptedException e) {
                interrupt();
                break;
            }
        }
    }
}

