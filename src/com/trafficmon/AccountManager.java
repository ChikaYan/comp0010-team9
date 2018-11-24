package com.trafficmon;

// interface is for the ease of future change to library and adapter
public interface AccountManager {
    void triggerInvestigationInto(Vehicle vehicle);

    void deductCharge(Vehicle vehicle, int charge) throws AccountNotRegisteredException, InsufficientCreditException;

    void issuePenalty(Vehicle vehicle, int charge);
}
