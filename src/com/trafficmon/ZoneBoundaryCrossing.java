package com.trafficmon;

// merge EntryEvent and ExitEvent into parent class to reduce coupling
// EntryEvent and ExitEvent will still be added to this package, but the package itself stops using it
// ZoneBoundaryCrossing is at the center of this package can it shouldn't be used in external code

public class ZoneBoundaryCrossing {
    public final EventType type;
    private final Vehicle vehicle;
    private final Clock clock;

    // change to use clock wrapper
    public ZoneBoundaryCrossing(Vehicle vehicle, Clock clock, EventType type) {
        this.vehicle = vehicle;
        this.clock = clock;
        this.type = type;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    // this is kept for Entry/ExitEvent
    public long timestamp() {
        return clock.toSecondOfDay();
    }

    public Clock getClock() {
        return clock;
    }
}
