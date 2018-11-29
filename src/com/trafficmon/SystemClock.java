package com.trafficmon;

import java.time.LocalTime;

// a wrapper for java.time
// required for unit test
// can also be a decorator to change the time scheme to summer/winter time/different time zone
public class SystemClock implements Clock {
    @Override
    public LocalTime getCurrentTime() {
        return LocalTime.now();
    }
}
