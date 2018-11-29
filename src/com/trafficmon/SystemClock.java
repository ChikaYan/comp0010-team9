package com.trafficmon;

import java.time.LocalTime;

// a wrapper for java.time
// required for unit test
// can also be a decorator to change the time scheme to summer/winter time/different time zone
// different events should have different clock
// TODO: rewrite this wrapper. How to use different wrapper?
public class SystemClock implements Clock {
    private LocalTime time;

    public SystemClock() {
        time = LocalTime.now();
    }

    public SystemClock(int hour, int minute) {
        time = LocalTime.of(hour, minute);
    }

    private SystemClock(LocalTime time) {
        this.time = time;
    }

    @Override
    public LocalTime getTime() {
        return time;
    }

    @Override
    public long toSecondOfDay() {
        return time.toSecondOfDay();
    }

    @Override
    public Clock plusHours(int hours) {
        return new SystemClock(time.plusHours(hours));
    }

    @Override
    public boolean isBefore(Clock time2) {
        return time.isBefore(time2.getTime());
    }
}
