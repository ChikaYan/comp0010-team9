package com.trafficmon;

public class Test {
    public static void main(String[] args) throws Exception {
        CongestionChargeSystem ccs = new CongestionChargeSystem();
        ccs.vehicleEnteringZone(Vehicle.withRegistration("A123 XYZ"));
        delaySeconds(3);
        ccs.vehicleEnteringZone(Vehicle.withRegistration("A123 XYZ"));
//        delaySeconds(3);
//        ccs.vehicleEnteringZone(Vehicle.withRegistration("A123 XYZ"));
//        delaySeconds(3);
//        ccs.vehicleLeavingZone(Vehicle.withRegistration("A123 XYZ"));
        ccs.calculateCharges();
    }

    private static void delayMins(int mins) throws InterruptedException {
        delaySeconds(mins * 60);
    }

    private static void delaySeconds(int secs) throws InterruptedException {
        Thread.sleep(secs * 1000);
    }
}
