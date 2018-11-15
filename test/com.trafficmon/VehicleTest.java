package com.trafficmon;

import org.junit.Test;
import static org.junit.Assert.*; // NEED STATIC!!
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class VehicleTest {
    @Test
    public void refactorMethodCreatesVehicleWithRegistration() {
        Vehicle vehicle = Vehicle.withRegistration("A123 XYZ");
        assertThat(vehicle.toString(), is("Vehicle [A123 XYZ]"));
        vehicle = Vehicle.withRegistration("test test");
        assertThat(vehicle.toString(), is("Vehicle [test test]"));
        vehicle = Vehicle.withRegistration("");
        assertThat(vehicle.toString(), is("Vehicle []"));
    }

    @Test
    public void canCheckIfVehiclesAreTheSame() {
        Vehicle v1 = Vehicle.withRegistration("A123 XYZ");
        Vehicle v2 = v1;
        //assertTrue(v1.equals(v2),is(true));
        assertTrue(v1.equals(v2));
        v2 = Vehicle.withRegistration("A123 XYZ");
        assertTrue(v1.equals(v2));

        v2 = Vehicle.withRegistration("A123 XZZ");
        assertFalse(v1.equals(v2));
        v2 = null;
        assertFalse(v1.equals(v2));
        assertFalse(v1.equals("A123 XYZ"));

    }

    //TODO: need to test hashcode?

}
