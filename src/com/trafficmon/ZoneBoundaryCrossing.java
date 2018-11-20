package com.trafficmon;

import java.time.LocalTime;

public abstract class ZoneBoundaryCrossing {
    private final Vehicle vehicle;
    private LocalTime time;

    // change to use time wrapper
    public ZoneBoundaryCrossing(Vehicle vehicle, TimeWrapper time) {
        this.vehicle = vehicle;
        this.time = time.getCurrentTime();
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    // keep this?
    public long timestamp() {
        return time.toSecondOfDay();
    }

    public LocalTime getTime() {
        return time;
    }
}
