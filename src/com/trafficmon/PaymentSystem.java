package com.trafficmon;

import java.math.BigDecimal;

public class PaymentSystem implements AccountManager {
    @Override
    public void triggerInvestigationInto(Vehicle vehicle) {
        OperationsTeam.getInstance().triggerInvestigationInto(vehicle);
    }

    @Override
    public void deductCharge(Vehicle vehicle, int charge) throws AccountNotRegisteredException, InsufficientCreditException {
        RegisteredCustomerAccountsService.getInstance()
                .accountFor(vehicle).deduct(BigDecimal.valueOf(charge));
    }

    @Override
    public void issuePenalty(Vehicle vehicle, int charge) {
        OperationsTeam.getInstance().issuePenaltyNotice(vehicle, BigDecimal.valueOf(charge));
    }
}
