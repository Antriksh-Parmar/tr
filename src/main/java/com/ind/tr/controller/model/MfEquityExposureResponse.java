package com.ind.tr.controller.model;

import java.math.BigDecimal;
import java.util.Map;

public class MfEquityExposureResponse extends BaseResponse {
    private final Map<String, BigDecimal> equityExposure;

    public MfEquityExposureResponse(Map<String, BigDecimal> equityExposure, Status status) {
        super(status);
        this.equityExposure = equityExposure;
    }

    public Map<String, BigDecimal> getEquityExposure() {
        return equityExposure;
    }

    public Status getStatus() {
        return status;
    }
}
