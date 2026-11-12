
package com.example.lab2;

public class BlockCheckThread extends Thread {

    private final AttemptTracker tracker;
    private final String email;

    private long remainingBlockMillis;

    public BlockCheckThread(
            AttemptTracker tracker,
            String email
    ) {
        this.tracker = tracker;
        this.email = email;
    }

    @Override
    public void run() {
        remainingBlockMillis =
                tracker.getRemainingBlockMillis(email);
    }

    public long getRemainingBlockMillis() {
        return remainingBlockMillis;
    }

    public boolean isBlocked() {
        return remainingBlockMillis > 0;
    }
}
