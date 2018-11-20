package com.trafficmon;

import java.time.LocalTime;

// a wrapper for java.time
public class TimeWrapper implements TimeGetter {
    @Override
    public LocalTime getCurrentTime() {
        return LocalTime.now();
    }
}
