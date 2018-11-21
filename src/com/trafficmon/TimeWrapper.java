package com.trafficmon;

import java.time.LocalTime;

// a wrapper for java.time
// required for unit test
// can also be a decorator to change the time scheme to summer/winter time
public class TimeWrapper implements TimeGetter {
    @Override
    public LocalTime getCurrentTime() {
        return LocalTime.now();
    }
}
