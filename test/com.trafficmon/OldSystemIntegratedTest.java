package com.trafficmon;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class OldSystemIntegratedTest {
    private final CongestionChargeSystem chargeSystem = new CongestionChargeSystem();
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
    public void mismatchedEntryExitsTriggerInvestigation(){
//        chargeSystem.vehicleEnteringZone(testVehicle); // not necessary as all vehicles leave before charge is deducted
//        chargeSystem.calculateCharges();
//        assertTrue(output.toString().contains(
//                "Charge made to account of Fred Bloggs, £0.00 deducted, balance:"));
//        output.reset();
        chargeSystem.vehicleEnteringZone(testVehicle);
        chargeSystem.vehicleEnteringZone(testVehicle);
        chargeSystem.calculateCharges();
        assertEquals(output.toString(),
                "Mismatched entries/exits. Triggering investigation into vehicle: Vehicle [A123 XYZ]\r\n");
        output.reset();
        chargeSystem.vehicleLeavingZone(testVehicle);
        chargeSystem.calculateCharges();
        assertEquals(output.toString(),
                "Mismatched entries/exits. Triggering investigation into vehicle: Vehicle [A123 XYZ]\r\n");
    }

    @Test
    public void nonExistVehicleTriggersPenalty() throws InterruptedException {
        chargeSystem.vehicleEnteringZone(Vehicle.withRegistration("none-exist vehicle"));
        Thread.sleep(1000);
        chargeSystem.vehicleLeavingZone(Vehicle.withRegistration("none-exist vehicle"));
        chargeSystem.calculateCharges();
        assertEquals(output.toString(),
                "Penalty notice for: Vehicle [none-exist vehicle]\r\n");
    }
}
