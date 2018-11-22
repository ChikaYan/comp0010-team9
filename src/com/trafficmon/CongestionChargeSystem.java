package com.trafficmon;

import java.math.BigDecimal;
import java.util.*;

public class CongestionChargeSystem {

    public static final BigDecimal CHARGE_RATE_POUNDS_PER_MINUTE = new BigDecimal(0.05);

    private final AccountManager paymentSystem = new PaymentSystem();
    private final List<ZoneBoundaryCrossing> eventLog = new ArrayList<ZoneBoundaryCrossing>();

    public void vehicleEnteringZone(Vehicle vehicle) {
        eventLog.add(new ZoneBoundaryCrossing(vehicle, new SystemClock(),EventType.ENTRY));
    }

    public void vehicleLeavingZone(Vehicle vehicle) {
        if (!previouslyRegistered(vehicle)) {
            return;
        }
        eventLog.add(new ZoneBoundaryCrossing(vehicle, new SystemClock(),EventType.EXIT));
    }

    // DON'T CHANGE PUBLIC API
    public void calculateCharges() {

        Map<Vehicle, List<ZoneBoundaryCrossing>> crossingsByVehicle = new HashMap<Vehicle, List<ZoneBoundaryCrossing>>();

        for (ZoneBoundaryCrossing crossing : eventLog) {
            if (!crossingsByVehicle.containsKey(crossing.getVehicle())) {
                crossingsByVehicle.put(crossing.getVehicle(), new ArrayList<ZoneBoundaryCrossing>());
            }
            crossingsByVehicle.get(crossing.getVehicle()).add(crossing);
        }

        for (Map.Entry<Vehicle, List<ZoneBoundaryCrossing>> vehicleCrossings : crossingsByVehicle.entrySet()) {
            Vehicle vehicle = vehicleCrossings.getKey();
            List<ZoneBoundaryCrossing> crossings = vehicleCrossings.getValue();

            if (!checkOrderingOf(crossings)) {
                paymentSystem.triggerInvestigationInto(vehicle);
            } else {

                BigDecimal charge = calculateChargeForTimeInZone(crossings);

                paymentSystem.deductCharge(vehicle,charge);
            }
        }
    }

    private BigDecimal calculateChargeForTimeInZone(List<ZoneBoundaryCrossing> crossings) {

        BigDecimal charge = new BigDecimal(0);

        ZoneBoundaryCrossing lastEvent = crossings.get(0);

        for (ZoneBoundaryCrossing crossing : crossings.subList(1, crossings.size())) {

            if (crossing.type == EventType.EXIT) {
                charge = charge.add(
                        new BigDecimal(minutesBetween(lastEvent.timestamp(), crossing.timestamp()))
                                .multiply(CHARGE_RATE_POUNDS_PER_MINUTE));
            }

            lastEvent = crossing;
        }

        return charge;
    }

    private boolean previouslyRegistered(Vehicle vehicle) {
        for (ZoneBoundaryCrossing crossing : eventLog) {
            if (crossing.getVehicle().equals(vehicle)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkOrderingOf(List<ZoneBoundaryCrossing> crossings) {

        ZoneBoundaryCrossing lastEvent = crossings.get(0);

        for (ZoneBoundaryCrossing crossing : crossings.subList(1, crossings.size())) {
            if (crossing.timestamp() < lastEvent.timestamp()) {
                return false;
            }
            if (crossing.type == EventType.ENTRY && lastEvent.type == EventType.ENTRY) {
                return false;
            }
            if (crossing.type == EventType.EXIT && lastEvent.type == EventType.EXIT) {
                return false;
            }
            lastEvent = crossing;
        }

        return true;
    }

    private int minutesBetween(long startTimeMs, long endTimeMs) {
        return (int) Math.ceil((endTimeMs - startTimeMs) / (1000.0 * 60.0));
    }

}
