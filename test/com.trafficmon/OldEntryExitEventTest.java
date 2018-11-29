package com.trafficmon;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class OldEntryExitEventTest {

    private Vehicle testVehicle = Vehicle.withRegistration("A123 XYZ");

    @Test
    public void canRecordTimeStamp() throws InterruptedException {
        EntryEvent entry1 = new EntryEvent(testVehicle);
        Thread.sleep(1000);
        EntryEvent entry2 = new EntryEvent(testVehicle);
        Thread.sleep(1000);
        ExitEvent exit1 = new ExitEvent(testVehicle);
        Thread.sleep(1000);
        ExitEvent exit2 = new ExitEvent(testVehicle);

        assertTrue(entry1.timestamp() < entry2.timestamp());
        assertTrue(entry2.timestamp() < exit1.timestamp());
        assertTrue(exit1.timestamp() < exit2.timestamp());
    }
}
