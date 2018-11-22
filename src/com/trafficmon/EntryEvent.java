package com.trafficmon;

public class EntryEvent extends ZoneBoundaryCrossing {
    public EntryEvent(Vehicle vehicle, Clock timeWrapper) {
        super(vehicle, timeWrapper);
    }
}
