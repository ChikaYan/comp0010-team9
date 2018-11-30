package com.trafficmon;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class OldSystemIntegratedTest {
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();
    private final Vehicle testVehicle = Vehicle.withRegistration("A123 XYZ");


    @Before
    public void setUpSystemOut() {
        System.setOut(new PrintStream(output));
    }

//    @Test
//    public void oldSystemCharges5pEveryMinRoundUp() throws InterruptedException {
//        chargeSystem.vehicleEnteringZone(testVehicle);
//        Thread.sleep(1000);
//        chargeSystem.vehicleLeavingZone(testVehicle);
//        chargeSystem.calculateCharges();
//        assertTrue(output.toString().contains(
//                "Charge made to account of Fred Bloggs, £0.05 deducted, balance:"));
//    }

    @Test
    public void mismatchedEntriesTriggerInvestigation() {
        CongestionChargeSystem chargeSystem = new CongestionChargeSystem();
        chargeSystem.vehicleEnteringZone(testVehicle);
        chargeSystem.vehicleEnteringZone(testVehicle);
        chargeSystem.calculateCharges();
        assertEquals(output.toString(),
                "Mismatched entries/exits. Triggering investigation into vehicle: Vehicle [A123 XYZ]\r\n");
    }

    @Test
    public void mismatchedExitsTriggerInvestigation() {
        CongestionChargeSystem chargeSystem = new CongestionChargeSystem();
        chargeSystem.vehicleEnteringZone(testVehicle);
        chargeSystem.vehicleLeavingZone(testVehicle);
        chargeSystem.vehicleLeavingZone(testVehicle);
        chargeSystem.calculateCharges();
        assertEquals(output.toString(),
                "Mismatched entries/exits. Triggering investigation into vehicle: Vehicle [A123 XYZ]\r\n");
    }

    @Test
    public void nonExistVehicleTriggersPenalty() throws InterruptedException {
        CongestionChargeSystem chargeSystem = new CongestionChargeSystem();
        chargeSystem.vehicleEnteringZone(Vehicle.withRegistration("none-exist vehicle"));
        Thread.sleep(1000);
        chargeSystem.vehicleLeavingZone(Vehicle.withRegistration("none-exist vehicle"));
        chargeSystem.calculateCharges();
        assertEquals(output.toString(),
                "Penalty notice for: Vehicle [none-exist vehicle]\r\n");
    }
}
