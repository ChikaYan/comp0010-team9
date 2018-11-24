package com.trafficmon;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class CongestionChargeSystem {

    public static final BigDecimal CHARGE_RATE_POUNDS_PER_MINUTE = new BigDecimal(0.05);

    private final AccountManager paymentSystem;
    private final Clock systemClock;
    private final List<ZoneBoundaryCrossing> eventLog = new ArrayList<ZoneBoundaryCrossing>();

    // use polymorphism to enable testing or convenient future changes
    public CongestionChargeSystem() {
        paymentSystem = new PaymentSystem();
        systemClock = new SystemClock();
    }

    public CongestionChargeSystem(AccountManager paymentSystem, Clock systemClock) {
        this.paymentSystem = paymentSystem;
        this.systemClock = systemClock;
    }

    public void vehicleEnteringZone(Vehicle vehicle) {
        eventLog.add(new ZoneBoundaryCrossing(vehicle, systemClock, EventType.ENTRY));
    }

    public void vehicleLeavingZone(Vehicle vehicle) {
        if (!previouslyRegistered(vehicle)) {
            return;
        }
        eventLog.add(new ZoneBoundaryCrossing(vehicle, systemClock, EventType.EXIT));
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

                int charge = calculateChargeForTimeInZone(crossings);
                try {
                    paymentSystem.deductCharge(vehicle, charge);
                } catch (InsufficientCreditException ice) {
                    paymentSystem.issuePenalty(vehicle, charge);
                } catch (AccountNotRegisteredException e) {
                    paymentSystem.issuePenalty(vehicle, charge);
                }

            }
        }
    }

    private int calculateChargeForTimeInZone(List<ZoneBoundaryCrossing> crossings) {
        // make a copy of crossings
        ArrayList<ZoneBoundaryCrossing> crosses = new ArrayList<>(crossings);
        LocalTime lastEntryTime = null;
        Duration overallTime = Duration.ZERO;
        int charge = 0;
        for (int i = 0; i < crosses.size(); i++) {
            if (crosses.get(i).type == EventType.ENTRY) {
                LocalTime currentTime = crosses.get(i).getTime();
                if (lastEntryTime == null || lastEntryTime.plusHours(4).isBefore(currentTime)) {
                    lastEntryTime = currentTime;
                    // calc charges depending on the time (accurate to minutes)
                    Duration timeSpent = Duration.ofMinutes(
                            currentTime.until(crosses.get(i + 1).getTime(), ChronoUnit.MINUTES));
                    overallTime = overallTime.plus(timeSpent);
                    if (overallTime.toHours() > 4) {
                        return 12;
                    }
                    if (currentTime.isBefore(LocalTime.of(14, 0))) {
                        // enter before 2pm
                        charge += 6;
                    } else {
                        charge += 4;
                    }
                } else {
                    // vehicle has entered within 4 hours, don't count this towards charge
                }
            }
        }

        ZoneBoundaryCrossing lastEvent = crossings.get(0);
        for (ZoneBoundaryCrossing crossing : crossings.subList(1, crossings.size())) {
            if (crossing.type == EventType.EXIT) {

            }
            lastEvent = crossing;
        }
        return charge;
//
//        BigDecimal charge = new BigDecimal(0);
//
//        ZoneBoundaryCrossing lastEvent = crossings.get(0);
//
//        for (ZoneBoundaryCrossing crossing : crossings.subList(1, crossings.size())) {
//
//            if (crossing.type == EventType.EXIT) {
//                charge = charge.add(
//                        new BigDecimal(minutesBetween(lastEvent.timestamp(), crossing.timestamp()))
//                                .multiply(CHARGE_RATE_POUNDS_PER_MINUTE));
//            }
//
//            lastEvent = crossing;
//        }
//
//        return charge;
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
