package com.trafficmon;

public class EntryEvent extends ZoneBoundaryCrossing {
    public EntryEvent(Vehicle vehicle) {
        super(vehicle, new TimeWrapper());
    }
}
