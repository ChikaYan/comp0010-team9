package com.trafficmon;

import org.junit.Test;

import java.time.LocalTime;

import static org.junit.Assert.*;

public class SystemClockTest {
    private final Clock systemClock = new SystemClock();

    @Test
    public void canGetCurrentTimeAccurateToSeconds() {
        LocalTime sys = systemClock.getCurrentTime();
        LocalTime now = LocalTime.now();
        assertTrue(sys.isBefore(now));
        assertTrue(sys.isAfter(now.minusSeconds(1)));

    }
}