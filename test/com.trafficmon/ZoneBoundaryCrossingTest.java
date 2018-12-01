package com.trafficmon;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.time.LocalTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class ZoneBoundaryCrossingTest {
    private static final LocalTime LOCALTIME = LocalTime.NOON;
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    private final Clock clock = context.mock(Clock.class);

    private Vehicle testVehicle = Vehicle.withRegistration("A123 XYZ");

    private ZoneBoundaryCrossing mockEntry;
    private ZoneBoundaryCrossing mockExit;

    @Before
    public void setUpMockEvents() {
        context.checking(new Expectations() {{
            exactly(2).of(clock).getCurrentTime();
            will(returnValue(LOCALTIME));
        }});
        mockEntry = new ZoneBoundaryCrossing(testVehicle, clock, EventType.ENTRY);
        mockExit = new ZoneBoundaryCrossing(testVehicle, clock, EventType.EXIT);
    }

    @Test
    public void canPassAndGetVehicle() {
        assertSame(mockEntry.getVehicle(), testVehicle);
        assertSame(mockExit.getVehicle(), testVehicle);
    }

    @Test
    public void canGetTimeUsingWrapper() {
        context.checking(new Expectations() {{
        }});
        assertEquals(mockEntry.getTime(), LOCALTIME);
        assertEquals(mockExit.getTime(), LOCALTIME);
    }

}
