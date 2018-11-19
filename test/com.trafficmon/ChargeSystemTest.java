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

    @Test
    public void checkVehicleEnteringZone(){
        //test
    }
//    public void checkMinutesBetween(){
//        CongestionChargeSystem ccs = new CongestionChargeSystem();
//        assertEquals(ccs.minutesBetween(), 1L);
//    }
}
