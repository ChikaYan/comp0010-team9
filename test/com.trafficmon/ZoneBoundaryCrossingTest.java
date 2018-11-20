package com.trafficmon;

import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ZoneBoundaryCrossingTest {
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    private final TimeGetter timeGetter = context.mock(TimeGetter.class);

    private Vehicle vehicle = Vehicle.withRegistration("A123 XYZ");

    @Test
    public void canPassAndGetVehicle() {
        EntryEvent entry = new EntryEvent(vehicle, timeGetter);
        ExitEvent exit = new ExitEvent(vehicle, timeGetter);
        assertSame(entry.getVehicle(), vehicle);
        assertSame(exit.getVehicle(), vehicle);
    }

    // old test for timestamp()
    @Test
    public void canRecordTimeStamp() throws InterruptedException {
        TimeGetter timeWrapper = new TimeWrapper();
        EntryEvent entry1 = new EntryEvent(vehicle, timeWrapper);
        Thread.sleep(1000);
        EntryEvent entry2 = new EntryEvent(vehicle, timeWrapper);
        Thread.sleep(1000);
        ExitEvent exit1 = new ExitEvent(vehicle, timeWrapper);
        Thread.sleep(1000);
        ExitEvent exit2 = new ExitEvent(vehicle, timeWrapper);

        assertTrue(entry1.timestamp() < entry2.timestamp());
        assertTrue(entry2.timestamp() < exit1.timestamp());
        assertTrue(exit1.timestamp() < exit2.timestamp());
    }

    @Test
    public void canGetTimeUsingWrapper() {

    }

}
