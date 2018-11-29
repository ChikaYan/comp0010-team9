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
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    private final Clock clock = context.mock(Clock.class);

    private Vehicle testVehicle = Vehicle.withRegistration("A123 XYZ");
    private ZoneBoundaryCrossing mockEntry = new ZoneBoundaryCrossing(testVehicle, clock, EventType.ENTRY);
    private ZoneBoundaryCrossing mockExit = new ZoneBoundaryCrossing(testVehicle, clock, EventType.EXIT);

    @Test
    public void canPassAndGetVehicle() {
        assertSame(mockEntry.getVehicle(), testVehicle);
        assertSame(mockExit.getVehicle(), testVehicle);
    }

    @Test
    public void canGetTimeUsingWrapper() {
        context.checking(new Expectations() {{
        }});
        assertEquals(mockEntry.getClock(), clock);
        assertEquals(mockExit.getClock(), clock);
    }
}
