package com.trafficmon;

public class ExitEvent extends ZoneBoundaryCrossing {
    public ExitEvent(Vehicle vehicle, Clock timeWrapper) {
        super(vehicle, timeWrapper);
    }
}
