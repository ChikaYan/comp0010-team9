package com.trafficmon;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CongestionChargeSystem {
    private final AccountManager accountManager;
    private final Clock clock;
    private final List<ZoneBoundaryCrossing> eventLog = new ArrayList<ZoneBoundaryCrossing>();

    // use polymorphism to enable testing or convenient future changes
    public CongestionChargeSystem() {
        accountManager = new PaymentSystem();
        clock = new SystemClock();
    }

    public CongestionChargeSystem(AccountManager accountManager, Clock clock) {
        this.accountManager = accountManager;
        this.clock = clock;
    }

    public void vehicleEnteringZone(Vehicle vehicle) {
        eventLog.add(new ZoneBoundaryCrossing(vehicle, clock, EventType.ENTRY));
    }

    public void vehicleLeavingZone(Vehicle vehicle) {
        if (!previouslyRegistered(vehicle)) {
            return;
        }
        eventLog.add(new ZoneBoundaryCrossing(vehicle, clock, EventType.EXIT));
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
                accountManager.triggerInvestigationInto(vehicle);
            } else {

                int charge = calculateChargeForTimeInZone(crossings);
                try {
                    accountManager.deductCharge(vehicle, charge);
                } catch (InsufficientCreditException ice) {
                    accountManager.issuePenalty(vehicle, charge);
                } catch (AccountNotRegisteredException e) {
                    accountManager.issuePenalty(vehicle, charge);
                }

            }
        }
    }

    private int calculateChargeForTimeInZone(List<ZoneBoundaryCrossing> crossings) {
        LocalTime lastEntryTime = null;
        Duration overallTime = Duration.ZERO;
        int charge = 0;
        for (int i = 0; i < crossings.size(); i++) {
            if (crossings.get(i).type == EventType.ENTRY) {
                LocalTime currentTime = crossings.get(i).getTime();
                if (lastEntryTime == null || lastEntryTime.plusHours(4).isBefore(currentTime)) {
                    lastEntryTime = currentTime;
                    // calc charges depending on the time (accurate to minutes)
                    Duration timeSpent = Duration.ofMinutes(
                            currentTime.until(crossings.get(i + 1).getTime(), ChronoUnit.MINUTES));
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
                }
                // vehicle has entered within 4 hours, don't count this towards charge
            }
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
            if (crossing.getTime().isBefore(lastEvent.getTime())) {
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
}
