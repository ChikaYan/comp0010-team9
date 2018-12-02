package com.trafficmon;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalTime;

public class ChargeSystemTest {
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    private final AccountManager mockPayment = context.mock(AccountManager.class);

    private final Vehicle testVehicle = Vehicle.withRegistration("A123 XYZ");

    private class MockClock implements Clock {
        private LocalTime time;

        MockClock(int hour, int min) {
            this.time = LocalTime.of(hour, min);
        }

        @Override
        public LocalTime getCurrentTime() {
            return time;
        }

        void advanceBy(int hour, int min) {
            time = time.plusHours(hour).plusMinutes(min);
        }
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
    public void enterBefore2AndStayFor4HoursIsChargedFor6() throws AccountNotRegisteredException, InsufficientCreditException {
        MockClock mockClock = new MockClock(6, 0); //enter at 6:00
        CongestionChargeSystem chargeSystem =
                new CongestionChargeSystem(mockPayment, mockClock);
        chargeSystem.vehicleEnteringZone(testVehicle);
        mockClock.advanceBy(4, 0);
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

    @Test
    public void enter3TimesBefore2pmWith4HourIntervalsIsChargedFor18() throws AccountNotRegisteredException, InsufficientCreditException {
        MockClock mockClock = new MockClock(0, 0);
        CongestionChargeSystem chargeSystem =
                new CongestionChargeSystem(mockPayment, mockClock);
        chargeSystem.vehicleEnteringZone(testVehicle); //12am in
        mockClock.advanceBy(0, 1);
        chargeSystem.vehicleLeavingZone(testVehicle); //leave at 12.01
        mockClock.advanceBy(4, 1);
        chargeSystem.vehicleEnteringZone(testVehicle); //4.02 in
        mockClock.advanceBy(0, 1);
        chargeSystem.vehicleLeavingZone(testVehicle);//leave at 4.03
        mockClock.advanceBy(4, 1);
        chargeSystem.vehicleEnteringZone(testVehicle);//8.04 in
        mockClock.advanceBy(0, 1);
        chargeSystem.vehicleLeavingZone(testVehicle);//8.05 out
        context.checking(new Expectations() {{
            oneOf(mockPayment).deductCharge(testVehicle, 18);
        }});
        chargeSystem.calculateCharges();
    }

    @Test
    public void onceBefore2andAfter2IsChargedFor10() throws AccountNotRegisteredException, InsufficientCreditException{
        MockClock mockClock = new MockClock(0, 0);
        CongestionChargeSystem chargeSystem =
                new CongestionChargeSystem(mockPayment, mockClock);
        chargeSystem.vehicleEnteringZone(testVehicle); //12am in
        mockClock.advanceBy(0, 1);
        chargeSystem.vehicleLeavingZone(testVehicle); //leave at 12.01
        mockClock.advanceBy(15, 1);
        chargeSystem.vehicleEnteringZone(testVehicle); //15.02 in
        mockClock.advanceBy(0, 1);
        chargeSystem.vehicleLeavingZone(testVehicle);//leave at 15.03
        context.checking(new Expectations() {{
            oneOf(mockPayment).deductCharge(testVehicle, 10);
        }});
        chargeSystem.calculateCharges();
    }

    @Test
    public void enter2HoursBefore2pmAnd3HoursAfter2pmIsChargedFor12() throws AccountNotRegisteredException, InsufficientCreditException{
        MockClock mockClock = new MockClock(0, 0);
        CongestionChargeSystem chargeSystem =
                new CongestionChargeSystem(mockPayment, mockClock);
        chargeSystem.vehicleEnteringZone(testVehicle); //12am in
        mockClock.advanceBy(2, 0);
        chargeSystem.vehicleLeavingZone(testVehicle); //leave at 2.00
        mockClock.advanceBy(13, 0);
        chargeSystem.vehicleEnteringZone(testVehicle); //15.00 in
        mockClock.advanceBy(3, 0);
        chargeSystem.vehicleLeavingZone(testVehicle);//leave at 18.00
        context.checking(new Expectations() {{
            oneOf(mockPayment).deductCharge(testVehicle, 12);
        }});
        chargeSystem.calculateCharges();
    }

    @Test
    public void mismatchedEntriesTriggerInvestigation() {
        MockClock mockClock = new MockClock(0, 0);
        CongestionChargeSystem chargeSystem =
                new CongestionChargeSystem(mockPayment, mockClock);
        chargeSystem.vehicleEnteringZone(testVehicle);
        chargeSystem.vehicleEnteringZone(testVehicle);
        context.checking(new Expectations() {{
            oneOf(mockPayment).triggerInvestigationInto(testVehicle);
        }});

        chargeSystem.calculateCharges();
    }

    @Test
    public void mismatchedExitsTriggerInvestigation() {
        MockClock mockClock = new MockClock(0, 0);
        CongestionChargeSystem chargeSystem =
                new CongestionChargeSystem(mockPayment, mockClock);
        chargeSystem.vehicleEnteringZone(testVehicle);
        chargeSystem.vehicleLeavingZone(testVehicle);
        chargeSystem.vehicleLeavingZone(testVehicle);
        context.checking(new Expectations() {{
            oneOf(mockPayment).triggerInvestigationInto(testVehicle);
        }});

        chargeSystem.calculateCharges();
    }

    @Test
    public void eventsWithWrongTimeTriggerInvestigation() {
        MockClock mockClock = new MockClock(1, 0);
        CongestionChargeSystem chargeSystem =
                new CongestionChargeSystem(mockPayment, mockClock);
        chargeSystem.vehicleEnteringZone(testVehicle);
        mockClock.advanceBy(0, -1);
        chargeSystem.vehicleLeavingZone(testVehicle);
        context.checking(new Expectations() {{
            oneOf(mockPayment).triggerInvestigationInto(testVehicle);
        }});

        chargeSystem.calculateCharges();
    }

    @Test
    public void unenteredVehicleCannotLeave() {
        MockClock mockClock = new MockClock(0, 0);
        CongestionChargeSystem chargeSystem =
                new CongestionChargeSystem(mockPayment, mockClock);
        chargeSystem.vehicleLeavingZone(testVehicle);
        context.checking(new Expectations() {{
        }});

        chargeSystem.calculateCharges();
    }

    @Test
    public void insufficientCreditTriggersPenalty() throws AccountNotRegisteredException, InsufficientCreditException {
        MockClock mockClock = new MockClock(0, 0);
        CongestionChargeSystem chargeSystem =
                new CongestionChargeSystem(mockPayment, mockClock);
        chargeSystem.vehicleEnteringZone(testVehicle);
        chargeSystem.vehicleLeavingZone(testVehicle);
        context.checking(new Expectations() {{
            oneOf(mockPayment).deductCharge(with(equal(testVehicle)),with(any(int.class))); will(throwException(new InsufficientCreditException(BigDecimal.valueOf(0))));
            oneOf(mockPayment).issuePenalty(with(equal(testVehicle)),with(any(int.class)));
        }});

        chargeSystem.calculateCharges();
    }
}
