
package com.example.lab2;

import java.util.concurrent.ConcurrentHashMap;

public class AttemptTracker {

    private final ConcurrentHashMap<String, UserState> states =
            new ConcurrentHashMap<>();

    private final int n;
    private final int t;

    public AttemptTracker(int n, int t) {
        if (n < 1 || t < 1) {
            throw new IllegalArgumentException(
                    "n and t must be positive integers"
            );
        }

        this.n = n;
        this.t = t;
    }

    public UserState getState(String email) {
        return states.computeIfAbsent(
                email, key -> new UserState()
        );
    }

    public long getRemainingBlockMillis(String email) {
        return getState(email).getRemainingBlockMillis();
    }

    public FailureStatus recordFailure(String email) {
        UserState state = getState(email);

        synchronized (state) {
            if (state.getRemainingBlockMillis() > 0) {
                return new FailureStatus(
                        0, state.getRemainingBlockMillis()
                );
            }

            state.recordFailure(n, t);

            return new FailureStatus(
                    state.getRemainingAttempts(n),
                    state.getRemainingBlockMillis()
            );
        }
    }

    public void reset(String email) {
        getState(email).reset();
    }

    public record FailureStatus(
            int remainingAttempts,
            long remainingBlockMillis
    ) {}
}
