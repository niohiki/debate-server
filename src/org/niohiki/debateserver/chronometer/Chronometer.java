package org.niohiki.debateserver.chronometer;

public class Chronometer {

    private final static int sleepTimeNanos = 1000;

    private long lastTimeNanos;
    private long mainTimeNanos;
    private boolean mainRunning;
    private boolean alive;

    public Chronometer() {
        alive = true;
        mainRunning = false;
        mainTimeNanos = 0;
        new Thread(() -> {
            long currentTimeNanos = System.nanoTime();
            while (alive) {
                lastTimeNanos = currentTimeNanos;
                currentTimeNanos = System.nanoTime();
                long delta = currentTimeNanos - lastTimeNanos;
                if (mainRunning) {
                    mainTimeNanos -= delta;
                }
                try {
                    Thread.sleep(0, sleepTimeNanos);
                } catch (InterruptedException ex) {
                }
            }
        }).start();
    }

    public synchronized void mainRun() {
        mainRunning = true;
    }

    public synchronized void mainPause() {
        mainRunning = false;
    }

    public synchronized void mainReset(int seconds) {
        mainRunning = false;
        mainTimeNanos = ((long) seconds) * 1000 * 1000 * 1000;
    }

    public synchronized long mainTimeNanos() {
        return mainTimeNanos;
    }
}
