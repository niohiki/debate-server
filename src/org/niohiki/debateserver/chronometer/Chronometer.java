package org.niohiki.debateserver.chronometer;

import org.niohiki.debateserver.Configuration;
import org.niohiki.debateserver.DebateSession.Teams.Team;

public class Chronometer {

    private final static int sleepTimeNanos = 0;
    private final static int sleepTimeMillis = 10;

    private long lastTimeNanos;
    private long mainTimeNanos;
    private long secondaryTimeNanos;
    private boolean mainRunning;
    private boolean secondaryRunning;
    private boolean alive;
    private boolean teamAyesSide;
    private int stance;
    private final Configuration configuration;
    private final Team teamA;
    private final Team teamB;

    public Chronometer(Configuration conf, Team a, Team b) {
        configuration = conf;
        teamA = a;
        teamB = b;
        stance = 0;
        alive = true;
        mainRunning = false;
        secondaryRunning = false;
        teamAyesSide = true;
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
                if (secondaryRunning) {
                    secondaryTimeNanos += delta;
                }
                try {
                    Thread.sleep(sleepTimeMillis, sleepTimeNanos);
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
        mainReset(((long) configuration.stances.get(stance).time)
                * 1000 * 1000 * 1000);
    }

    public synchronized void mainReset(long nanos) {
        mainTimeNanos = nanos;
        secondaryStop();
        mainPause();
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
            mainReset();
        }
    }

    public synchronized void nextStance() {
        setStance(stance + 1);
    }

    public synchronized void kill() {
        alive = false;
    }

    public synchronized void mainToggle() {
        if (mainRunning) {
            mainPause();
        } else {
            mainRun();
        }
    }

    public synchronized void secondaryToggle() {
        if (secondaryRunning) {
            secondaryStop();
        } else {
            secondaryStart();
        }
    }

    public synchronized void swapTeamSides() {
        teamAyesSide = !teamAyesSide;
    }

    public boolean isMainRunning() {
        return mainRunning;
    }

    public boolean isSecondaryRunning() {
        return secondaryRunning;
    }

    public String shortName() {
        return teamA.name + " vs " + teamB.name;
    }

    public String fullName() {
        return teamA.name + " (" + (teamAyesSide ? configuration.sides.yes : configuration.sides.no)
                + ") vs " + teamB.name + " (" + (teamAyesSide ? configuration.sides.no : configuration.sides.yes) + ")";
    }

    public String stance() {
        return configuration.stances.get(stance).fullName;
    }

    public int getStanceIndex() {
        return stance;
    }

    @Override
    public String toString() {
        return fullName();
    }
}
