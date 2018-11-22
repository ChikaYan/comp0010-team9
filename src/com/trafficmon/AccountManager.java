package com.trafficmon;

import java.math.BigDecimal;

// interface is for the ease of future change to library and adapter
public interface AccountManager {
    void triggerInvestigationInto(Vehicle vehicle);

    void deductCharge (Vehicle vehicle, BigDecimal charge);
}
