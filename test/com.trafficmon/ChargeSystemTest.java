package com.trafficmon;

import org.junit.Test;
import static org.junit.Assert.*;

public class ChargeSystemTest {
    @Test
    public void checkVehicleEnteringZone(){
        CongestionChargeSystem ccs = new CongestionChargeSystem();
        Vehicle vehicle = Vehicle.withRegistration("A123 XYZ");
        ccs.vehicleEnteringZone(Vehicle vehicle);

    }

    @Test
    public void checkVehicleLeavingZone(){

    }
//    public void checkMinutesBetween(){
//        CongestionChargeSystem ccs = new CongestionChargeSystem();
//        assertEquals(ccs.minutesBetween(), 1L);
//    }
}
