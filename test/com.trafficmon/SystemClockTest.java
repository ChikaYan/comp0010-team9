package com.trafficmon;

import org.junit.Test;

import java.time.LocalTime;

import static org.junit.Assert.*;

public class SystemClockTest {
    private final Clock systemClock = new SystemClock();

    @Test
    public void canGetCurrentTimeAccurateToSeconds() { // TODO: solve the unstability
        LocalTime sys = systemClock.getTime();
        LocalTime now = LocalTime.now();
        assertTrue(sys.isBefore(now) || sys.equals(now));
        assertTrue(sys.isAfter(now.minusSeconds(1)));
    }
}