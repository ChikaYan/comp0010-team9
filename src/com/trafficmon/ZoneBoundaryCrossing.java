package com.trafficmon;

import java.time.LocalTime;

// merge EntryEvent and ExitEvent into parent class to reduce coupling
// ZoneBoundaryCrossing is at the center of this package can it shouldn't be used in external code

public class ZoneBoundaryCrossing {
    public final EventType type;
    private final Vehicle vehicle;
    private LocalTime time;

    // change to use time wrapper
    public ZoneBoundaryCrossing(Vehicle vehicle, Clock time, EventType type) {
        this.vehicle = vehicle;
        this.time = time.getCurrentTime();
        this.type = type;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    // keep this? -- will be removed when implementing new functions
    public long timestamp() {
        return time.toSecondOfDay();
    }

    public LocalTime getTime() {
        return time;
    }
}
