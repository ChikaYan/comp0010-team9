package com.trafficmon;

import java.math.BigDecimal;

public class PaymentSystem implements AccountManager {

    private PenaltiesService operationTeam = OperationsTeam.getInstance();
    private AccountsService accountsService = RegisteredCustomerAccountsService.getInstance();

    @Override
    public void triggerInvestigationInto(Vehicle vehicle) {
        operationTeam.triggerInvestigationInto(vehicle);
    }

    @Override
    public void deductCharge(Vehicle vehicle, int charge) throws AccountNotRegisteredException, InsufficientCreditException {
        accountsService.accountFor(vehicle).deduct(BigDecimal.valueOf(charge));
    }

    @Override
    public void issuePenalty(Vehicle vehicle, int charge) {
        operationTeam.issuePenaltyNotice(vehicle, BigDecimal.valueOf(charge));
    }
}
