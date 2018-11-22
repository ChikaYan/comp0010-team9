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

    private ZoneBoundaryCrossing mockEntry;
    private ZoneBoundaryCrossing mockExit;

    @Before
    public void setUpMockEvents() {
        context.checking(new Expectations() {{
            exactly(2).of(clock).getCurrentTime();
            will(returnValue(LOCALTIME));
        }});
        mockEntry = new ZoneBoundaryCrossing(vehicle, clock, EventType.ENTRY);
        mockExit = new ZoneBoundaryCrossing(vehicle, clock, EventType.EXIT);
    }

    @Test
    public void canPassAndGetVehicle() {
        assertSame(mockEntry.getVehicle(), vehicle);
        assertSame(mockExit.getVehicle(), vehicle);
    }

//     old test for timestamp()
    @Test
    public void canRecordTimeStamp() throws InterruptedException {
        Clock timeWrapper = new SystemClock();
        ZoneBoundaryCrossing entry1 = new ZoneBoundaryCrossing(vehicle, timeWrapper,EventType.ENTRY);
        Thread.sleep(1000);
        ZoneBoundaryCrossing entry2 = new ZoneBoundaryCrossing(vehicle, timeWrapper,EventType.EXIT);
        Thread.sleep(1000);
        ZoneBoundaryCrossing exit1 = new ZoneBoundaryCrossing(vehicle, timeWrapper,EventType.EXIT);
        Thread.sleep(1000);
        ZoneBoundaryCrossing exit2 = new ZoneBoundaryCrossing(vehicle, timeWrapper,EventType.EXIT);

        assertTrue(entry1.timestamp() < entry2.timestamp());
        assertTrue(entry2.timestamp() < exit1.timestamp());
        assertTrue(exit1.timestamp() < exit2.timestamp());
    }

    @Test
    public void canGetTimeUsingWrapper() {
        context.checking(new Expectations() {{
        }});
        assertEquals(mockEntry.getTime(), LOCALTIME);
        assertEquals(mockExit.getTime(), LOCALTIME);
    }

}
