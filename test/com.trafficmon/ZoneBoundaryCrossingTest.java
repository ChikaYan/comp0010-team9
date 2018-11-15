package com.trafficmon;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ZoneBoundaryCrossingTest {
    private Vehicle vehicle = Vehicle.withRegistration("A123 XYZ");

    @Test
    public void canPassAndGetVehicle() {
        EntryEvent entry = new EntryEvent(vehicle);
        ExitEvent exit = new ExitEvent(vehicle);
        assertSame(entry.getVehicle(), vehicle);
        assertSame(exit.getVehicle(), vehicle);
    }

    @Test
    public void canRecordTimeStamp() throws InterruptedException{ //TODO: does this work?
        EntryEvent entry1 = new EntryEvent(vehicle);
        Thread.sleep(1);
        EntryEvent entry2 = new EntryEvent(vehicle);
        Thread.sleep(1);
        ExitEvent exit1 = new ExitEvent(vehicle);
        Thread.sleep(1);
        ExitEvent exit2 = new ExitEvent(vehicle);

        assertTrue(entry1.timestamp() < entry2.timestamp());
        assertTrue(entry2.timestamp() < exit1.timestamp());
        assertTrue(exit1.timestamp() < exit2.timestamp());
    }

}
