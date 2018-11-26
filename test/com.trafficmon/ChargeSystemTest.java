package com.trafficmon;

import org.jmock.Expectations;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

import org.jmock.integration.junit4.JUnitRuleMockery;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.time.LocalTime;

public class ChargeSystemTest {
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    private final AccountManager mockPayment = context.mock(AccountManager.class);
    private final Clock clock = context.mock(Clock.class);

    private final CongestionChargeSystem chargeSystem = new CongestionChargeSystem();
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();
    private final Vehicle testVehicle = Vehicle.withRegistration("A123 XYZ");

    @Before
    public void setUpSystemOut() {
        System.setOut(new PrintStream(output));
    }

    @Test
    public void enterBefore2AndStayUpTo4IsChargedFor6() throws AccountNotRegisteredException, InsufficientCreditException {
        MockClock mockClock = new MockClock(6, 0); //enter at 6:00
        CongestionChargeSystem chargeSystem =
                new CongestionChargeSystem(mockPayment, mockClock);
        chargeSystem.vehicleEnteringZone(testVehicle);
        mockClock.advanceBy(2, 0);
        chargeSystem.vehicleLeavingZone(testVehicle);
        context.checking(new Expectations() {{
            oneOf(mockPayment).deductCharge(testVehicle, 6);
        }});
        chargeSystem.calculateCharges();
    }

    @Test
    public void enterAndGoOutBefore2ThenGoBackAfter2AndStayUpTo4IsChargedFor6()throws AccountNotRegisteredException, InsufficientCreditException{
        MockClock mockClock = new MockClock(12, 0);     //enter at 12:00
        CongestionChargeSystem chargeSystem =
                new CongestionChargeSystem(mockPayment, mockClock);
        chargeSystem.vehicleEnteringZone(testVehicle); //12:00 in
        mockClock.advanceBy(1, 0);
        chargeSystem.vehicleLeavingZone(testVehicle); //13:00 out
        mockClock.advanceBy(1, 0);
        chargeSystem.vehicleEnteringZone(testVehicle);  //14:00 in
        mockClock.advanceBy(1, 0);
        chargeSystem.vehicleLeavingZone(testVehicle); //15:00 out
        context.checking(new Expectations() {{
            oneOf(mockPayment).deductCharge(testVehicle, 6);
        }});
        chargeSystem.calculateCharges();
    }

    @Test
    public void enterAfter2AndStayUpTo4IsChargedFor4()throws AccountNotRegisteredException, InsufficientCreditException{
        MockClock mockClock = new MockClock(15, 0); //enter at 15:00
        CongestionChargeSystem chargeSystem =
                new CongestionChargeSystem(mockPayment, mockClock);
        chargeSystem.vehicleEnteringZone(testVehicle);
        mockClock.advanceBy(2, 0);
        chargeSystem.vehicleLeavingZone(testVehicle);
        context.checking(new Expectations() {{
            oneOf(mockPayment).deductCharge(testVehicle, 4);
        }});
        chargeSystem.calculateCharges();
    }

    @Test
    public void enterAfter2AndGoOutThenGoBackAndStayUpTo4IsChargedFor4()throws AccountNotRegisteredException, InsufficientCreditException{
        MockClock mockClock = new MockClock(15, 0);
        CongestionChargeSystem chargeSystem =
                new CongestionChargeSystem(mockPayment, mockClock);
        chargeSystem.vehicleEnteringZone(testVehicle);//15:00 In
        mockClock.advanceBy(1, 0);
        chargeSystem.vehicleLeavingZone(testVehicle);//16:00 Out
        mockClock.advanceBy(1, 0);
        chargeSystem.vehicleEnteringZone(testVehicle);//17:00 In
        mockClock.advanceBy(1, 0);
        chargeSystem.vehicleLeavingZone(testVehicle);//18:00 Out
        context.checking(new Expectations() {{
            oneOf(mockPayment).deductCharge(testVehicle, 4);
        }});
        chargeSystem.calculateCharges();
    }

    @Test
    public void enterAfter2AndStayUpTo5IsChargedFor12()throws AccountNotRegisteredException, InsufficientCreditException{
        MockClock mockClock = new MockClock(15, 0);
        CongestionChargeSystem chargeSystem =
                new CongestionChargeSystem(mockPayment, mockClock);
        chargeSystem.vehicleEnteringZone(testVehicle);
        mockClock.advanceBy(5, 0);
        chargeSystem.vehicleLeavingZone(testVehicle);
        context.checking(new Expectations() {{
            oneOf(mockPayment).deductCharge(testVehicle, 12);
        }});
        chargeSystem.calculateCharges();
    }


    private class MockClock implements Clock {

        private LocalTime time;

        MockClock(int hour, int min) {
            this.time = LocalTime.of(hour, min);
        }

        @Override
        public LocalTime getCurrentTime() {
            return time;
        }

        public void advanceBy(int hour, int min) {
            time = time.plusHours(hour).plusMinutes(min);
        }
    }

    //--------------------------OLD TESTS---------------------------

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
    public void mismatchedEntryExitsTriggerInvestigation() throws InterruptedException {
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
    public void insufficientCreditTriggersPenalty() throws InterruptedException {
        chargeSystem.vehicleEnteringZone(Vehicle.withRegistration("none-exist vehicle"));
        Thread.sleep(1000);
        chargeSystem.vehicleLeavingZone(Vehicle.withRegistration("none-exist vehicle"));
        chargeSystem.calculateCharges();
        assertEquals(output.toString(),
                "Penalty notice for: Vehicle [none-exist vehicle]\r\n");

    }

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
//    public void checkMinutesBetween(){
//        CongestionChargeSystem ccs = new CongestionChargeSystem();
//        assertEquals(ccs.minutesBetween(), 1L);
//    }
}
