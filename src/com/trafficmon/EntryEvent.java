package com.trafficmon;

public class EntryEvent extends ZoneBoundaryCrossing {
    public EntryEvent(Vehicle vehicle,TimeGetter timeWrapper) {
        super(vehicle, timeWrapper);
    }
}
