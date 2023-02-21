package com.ind.tr.persistance.model.analytics;

import java.math.BigDecimal;
import java.util.Map;

public class MfEquityExposureRead {
    private int id;
    private Map<String, BigDecimal> equityExposure;

    public MfEquityExposureRead(int id, Map<String, BigDecimal> equityExposure) {
        this.id = id;
        this.equityExposure = equityExposure;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Map<String, BigDecimal> getEquityExposure() {
        return equityExposure;
    }

    public void setEquityExposure(Map<String, BigDecimal> equityExposure) {
        this.equityExposure = equityExposure;
    }
}
