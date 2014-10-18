package org.niohiki.debateserver.chronometer;

import org.niohiki.debateserver.Configuration;
import org.niohiki.debateserver.Session;

public class Chronometer {

    private final static int sleepTimeNanos = 1000;

    private long lastTimeNanos;
    private long mainTimeNanos;
    private long secondaryTimeNanos;
    private boolean mainRunning;
    private boolean secondaryRunning;
    private boolean alive;
    private int stance;
    private final Configuration configuration;

    public Chronometer(Configuration conf) {
        configuration = conf;
        stance = 0;
        alive = true;
        mainRunning = false;
        secondaryRunning = false;
        mainTimeNanos = 0;
        secondaryTimeNanos = 0;
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

    public synchronized void mainReset() {
        mainRunning = false;
        mainTimeNanos = ((long) configuration.stances.get(stance).time)
                * 1000 * 1000 * 1000;
    }

    public synchronized void secondaryStart() {
        secondaryRunning = true;
    }

    public synchronized void secondaryStop() {
        secondaryRunning = false;
        secondaryTimeNanos = 0;
    }

    public synchronized long mainTimeNanos() {
        return mainTimeNanos;
    }

    public synchronized long secondaryTimeNanos() {
        return secondaryTimeNanos;
    }

    public synchronized void setStance(int i) {
        if (i >= 0 && i < configuration.stances.number) {
            stance = i;
        }
    }

    public synchronized void nextStance() {
        setStance(stance + 1);
    }

    public synchronized void kill() {
        alive = false;
    }
}
