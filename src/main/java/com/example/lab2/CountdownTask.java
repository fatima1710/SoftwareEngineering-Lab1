
package com.example.lab2;

import javafx.application.Platform;
import java.util.function.LongConsumer;

public class CountdownTask implements Runnable {

    private final long remainingMillis;
    private final LongConsumer onTick;
    private final Runnable onFinished;

    public CountdownTask(
            long remainingMillis,
            LongConsumer onTick,
            Runnable onFinished
    ) {
        this.remainingMillis = remainingMillis;
        this.onTick = onTick;
        this.onFinished = onFinished;
    }

    @Override
    public void run() {
        long endTime = System.nanoTime()
                + remainingMillis * 1_000_000L;

        try {
            while (true) {
                long leftMillis = Math.max(
                        0,
                        (endTime - System.nanoTime()) / 1_000_000L
                );

                if (leftMillis == 0) {
                    break;
                }

                long seconds = (leftMillis + 999) / 1000;

                Platform.runLater(() -> onTick.accept(seconds));

                Thread.sleep(Math.min(1000, leftMillis));
            }

            Platform.runLater(onFinished);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
