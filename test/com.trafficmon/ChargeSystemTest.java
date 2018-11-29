package com.trafficmon;

import org.jmock.Expectations;
import org.junit.Rule;
import org.junit.Test;

import org.jmock.integration.junit4.JUnitRuleMockery;

import java.time.LocalTime;

public class ChargeSystemTest {
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    private final AccountManager mockPayment = context.mock(AccountManager.class);

    private final Vehicle testVehicle = Vehicle.withRegistration("A123 XYZ");

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
    public void enterAndGoOutBefore2ThenGoBackAfter2AndStayUpTo4IsChargedFor6() throws AccountNotRegisteredException, InsufficientCreditException {
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
    public void enterAfter2AndStayUpTo4IsChargedFor4() throws AccountNotRegisteredException, InsufficientCreditException {
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
    public void enterAfter2AndGoOutThenGoBackAndStayUpTo4IsChargedFor4() throws AccountNotRegisteredException, InsufficientCreditException {
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
    public void enterAfter2AndStayUpTo5IsChargedFor12() throws AccountNotRegisteredException, InsufficientCreditException {
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

        public MockClock(int hour, int minute) {
            time = LocalTime.of(hour, minute);
        }

        private MockClock(LocalTime time) {
            this.time = time;
        }

        @Override
        public LocalTime getTime() {
            return time;
        }

        @Override
        public long toSecondOfDay() {
            return time.toSecondOfDay();
        }

        @Override
        public Clock plusHours(int hours) {
            return new MockClock(time.plusHours(hours));
        }

        @Override
        public boolean isBefore(Clock time2) {
            return time.isBefore(time2.getTime());
        }

        void advanceBy(int hour, int min) {
            time = time.plusHours(hour).plusMinutes(min);
        }
    }

    //--------------------------OLD TESTS---------------------------


}
