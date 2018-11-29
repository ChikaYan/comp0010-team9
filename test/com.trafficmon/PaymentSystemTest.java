package com.trafficmon;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PaymentSystemTest {
    private final int CHARGE = 3;
    private final AccountManager payment = new PaymentSystem();
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();
    private final Vehicle testVehicle = Vehicle.withRegistration("A123 XYZ");

    @Before
    public void setUpSystemOut() {
        System.setOut(new PrintStream(output));
    }

    @Test
    public void canTriggerInvestigation() {
        payment.triggerInvestigationInto(testVehicle);
        assertEquals(output.toString(),
                "Mismatched entries/exits. Triggering investigation into vehicle: Vehicle [A123 XYZ]\r\n");
    }

    @Test
    public void canDeductCharge() throws AccountNotRegisteredException, InsufficientCreditException {
        payment.deductCharge(testVehicle,CHARGE);
        assertTrue(output.toString().contains(
                "Charge made to account of Fred Bloggs, £3.00 deducted, balance:"));
    }

    @Test(expected = AccountNotRegisteredException.class)
    public void canThrowAccountNotRegisteredException() throws AccountNotRegisteredException, InsufficientCreditException {
        payment.deductCharge(Vehicle.withRegistration("random vehicle"),CHARGE);
        assertEquals(output.toString(),
                "Penalty notice for: Vehicle [random vehicle]\r\n");
    }

    @Test(expected = InsufficientCreditException.class)
    public void canThrowInsufficientCreditException() throws AccountNotRegisteredException, InsufficientCreditException {
        payment.deductCharge(testVehicle,1000);
        assertEquals(output.toString(),
                "Penalty notice for: Vehicle [random vehicle]\r\n");
    }

    @Test
    public void canIssuePenalty(){
        payment.issuePenalty(testVehicle,CHARGE);
        assertEquals(output.toString(),
                "Penalty notice for: Vehicle [A123 XYZ]\r\n");
    }
}
