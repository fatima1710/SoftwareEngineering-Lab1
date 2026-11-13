
package com.example.lab2;

import java.util.concurrent.CountDownLatch;

public class ConcurrencyTest {

    public static String runTest() throws InterruptedException {

        int n = 10000;
        int t = 30;

        AttemptTracker tracker = new AttemptTracker(n, t);

        String email = "test@example.com";

        int threadCount = 20;
        int attemptsPerThread = 499;

        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threadCount);

        Thread[] threads = new Thread[threadCount];

        for (int i = 0; i < threadCount; i++) {

            threads[i] = new Thread(() -> {
                try {
                    start.await();

                    for (int j = 0; j < attemptsPerThread; j++) {
                        tracker.recordFailure(email);
                    }

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    done.countDown();
                }
            });

            threads[i].start();
        }

        // Release all threads together.
        start.countDown();

        // Wait for all 20 threads to finish.
        done.await();

        int remainingAfter9980 =
                tracker.getState(email).getRemainingAttempts(n);

        boolean blockedAfter9980 =
                tracker.getRemainingBlockMillis(email) > 0;

        // Add 19 failures.
        for (int i = 0; i < 19; i++) {
            tracker.recordFailure(email);
        }

        int remainingAfter9999 =
                tracker.getState(email).getRemainingAttempts(n);

        boolean blockedAfter9999 =
                tracker.getRemainingBlockMillis(email) > 0;

        // Failure number 10000.
        tracker.recordFailure(email);

        boolean blockedAfter10000 =
                tracker.getRemainingBlockMillis(email) > 0;

        String result =
                "After 9980 failures: remaining = "
                        + remainingAfter9980
                        + ", blocked = " + blockedAfter9980
                        + "\nAfter 9999 failures: remaining = "
                        + remainingAfter9999
                        + ", blocked = " + blockedAfter9999
                        + "\nAfter 10000 failures: blocked = "
                        + blockedAfter10000;

        System.out.println(result);

        return result;
    }
    public static void main(String[] args) throws InterruptedException {
        runTest();
    }
}
