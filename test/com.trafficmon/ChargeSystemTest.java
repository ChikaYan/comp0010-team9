package com.trafficmon;

import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.*;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;

public class ChargeSystemTest {
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    @Test
    public void enterBefore2AndStayUpTo4IsChargedFor6(){

    }

<<<<<<< HEAD
//    @Test
//    public void checkVehicleEnteringZone(){
//        CongestionChargeSystem ccs = new CongestionChargeSystem();
//        Vehicle vehicle = Vehicle.withRegistration("A123 XYZ");
//        //ccs.vehicleEnteringZone(Vehicle vehicle);
//
//    }
//
//    @Test
//    public void checkVehicleLeavingZone(){
//
//    }
=======
    @Test
    public void checkVehicleEnteringZone(){
        CongestionChargeSystem ccs = new CongestionChargeSystem();
        Vehicle vehicle = Vehicle.withRegistration("A123 XYZ");
        //ccs.vehicleEnteringZone(Vehicle vehicle);

    }

    @Test
    public void checkVehicleLeavingZone(){

    }

>>>>>>> 39806bbab715494ead85c0b9beab053fb086a69e
//    public void checkMinutesBetween(){
//        CongestionChargeSystem ccs = new CongestionChargeSystem();
//        assertEquals(ccs.minutesBetween(), 1L);
//    }
}
