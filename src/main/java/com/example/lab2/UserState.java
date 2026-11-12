
package com.example.lab2;

public class UserState {

    private int failedAttempts = 0;
    private long blockedUntil = 0;

    // Returns the remaining blocking time in milliseconds.
    public synchronized long getRemainingBlockMillis() {
        long remaining = blockedUntil - System.currentTimeMillis();

        if (remaining > 0) {
            return remaining;
        }

        // Reset the counter when the blocking period expires.
        if (blockedUntil != 0) {
            blockedUntil = 0;
            failedAttempts = 0;
        }

        return 0;
    }

    // Records a failed login attempt.
    public synchronized void recordFailure(int n, int t) {
        if (getRemainingBlockMillis() > 0) {
            return;
        }

        failedAttempts++;

        if (failedAttempts >= n) {
            blockedUntil = System.currentTimeMillis() + t * 1000L;
        }
    }

    // Returns how many attempts are still allowed.
    public synchronized int getRemainingAttempts(int n) {
        getRemainingBlockMillis();
        return Math.max(0, n - failedAttempts);
    }

    // Resets the counter after a successful login.
    public synchronized void reset() {
        failedAttempts = 0;
        blockedUntil = 0;
    }
}
