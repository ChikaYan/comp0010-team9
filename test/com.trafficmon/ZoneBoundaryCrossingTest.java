package com.trafficmon;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ZoneBoundaryCrossingTest {
    private Vehicle vehicle = Vehicle.withRegistration("A123 XYZ");

    @Test
    public void canPassAndGetVehicle() {
        EntryEvent entry = new EntryEvent(vehicle);
        ExitEvent exit = new ExitEvent(vehicle);
        assertThat(entry.getVehicle() == vehicle, is(true));
        assertThat(exit.getVehicle() == vehicle, is(true));
    }

    @Test
    public void canRecordTimeStamp() throws InterruptedException{ //TODO: does this work
        EntryEvent entry1 = new EntryEvent(vehicle);
        Thread.sleep(1);
        EntryEvent entry2 = new EntryEvent(vehicle);
        Thread.sleep(1);
        ExitEvent exit1 = new ExitEvent(vehicle);
        Thread.sleep(1);
        ExitEvent exit2 = new ExitEvent(vehicle);

        assertThat(entry1.timestamp() < entry2.timestamp(), is(true));
        assertThat(entry2.timestamp() < exit1.timestamp(), is(true));
        assertThat(exit1.timestamp() < exit2.timestamp(), is(true));
    }

}
