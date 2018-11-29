package com.trafficmon;

import java.time.LocalTime;

public interface Clock {
    LocalTime getTime();

    long toSecondOfDay();

    Clock plusHours(int hours);

    boolean isBefore(Clock time2);
}
