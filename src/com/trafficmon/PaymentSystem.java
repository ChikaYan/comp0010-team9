package com.trafficmon;

import java.math.BigDecimal;

public class PaymentSystem implements AccountManager {
    @Override
    public void triggerInvestigationInto(Vehicle vehicle){
        OperationsTeam.getInstance().triggerInvestigationInto(vehicle);
    }

    @Override
    public void deductCharge(Vehicle vehicle, BigDecimal charge){
        try {
            RegisteredCustomerAccountsService.getInstance().accountFor(vehicle).deduct(charge);
        } catch (InsufficientCreditException ice) {
            OperationsTeam.getInstance().issuePenaltyNotice(vehicle, charge);
        } catch (AccountNotRegisteredException e) {
            OperationsTeam.getInstance().issuePenaltyNotice(vehicle, charge);
        }
    }
}
