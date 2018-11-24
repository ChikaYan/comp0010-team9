package com.trafficmon;

import java.math.BigDecimal;

public class PaymentSystem implements AccountManager {
    @Override
    public void triggerInvestigationInto(Vehicle vehicle) {
        OperationsTeam.getInstance().triggerInvestigationInto(vehicle);
    }

    @Override
    public void deductCharge(Vehicle vehicle, BigDecimal charge) throws AccountNotRegisteredException, InsufficientCreditException {
        RegisteredCustomerAccountsService.getInstance().accountFor(vehicle).deduct(charge);
    }

    @Override
    public void issuePenalty(Vehicle vehicle, BigDecimal charge) {
        OperationsTeam.getInstance().issuePenaltyNotice(vehicle, charge);
    }
}
