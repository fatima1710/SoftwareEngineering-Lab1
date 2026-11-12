
package com.example.lab2;

public record LoginResult(
        Status status,
        int remainingAttempts,
        long remainingBlockMillis
) {
    public enum Status {
        SUCCESS,
        ERROR,
        BLOCKED
    }
}
