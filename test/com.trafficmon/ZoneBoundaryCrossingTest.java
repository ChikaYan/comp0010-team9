package com.trafficmon;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.time.LocalTime;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ZoneBoundaryCrossingTest {
    private static final LocalTime LOCALTIME = LocalTime.NOON;
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    private final Clock clock = context.mock(Clock.class);

    private Vehicle vehicle = Vehicle.withRegistration("A123 XYZ");

    private EntryEvent mockEntry;
    private EntryEvent mockExit;

    @Before
    public void setUpMockEvents(){
        context.checking(new Expectations() {{
            exactly(2).of(clock).getCurrentTime();
            will(returnValue(LOCALTIME));
        }});
        mockEntry = new EntryEvent(vehicle, clock);
        mockExit = new EntryEvent(vehicle, clock);
    }

    @Test
    public void canPassAndGetVehicle() {
        assertSame(mockEntry.getVehicle(), vehicle);
        assertSame(mockExit.getVehicle(), vehicle);
    }

    // old test for timestamp()
    @Test
    public void canRecordTimeStamp() throws InterruptedException {
        Clock timeWrapper = new SystemClock();
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
        context.checking(new Expectations() {{
        }});
        assertEquals(mockEntry.getTime(),LOCALTIME);
        assertEquals(mockExit.getTime(),LOCALTIME);
    }

}
