package com.ind.tr.repository.model;

import java.math.BigDecimal;
import java.sql.Date;

public class RollingReturnEntity {

    private final String mfId;
    private final Date rrDate;
    private final BigDecimal oneMonth;
    private final BigDecimal threeMonths;
    private final BigDecimal sixMonths;
    private final BigDecimal oneYear;
    private final BigDecimal threeYears;
    private final BigDecimal fiveYears;
    private final BigDecimal sevenYears;
    private final BigDecimal tenYears;

    public RollingReturnEntity(String mfId, Date rrDate, BigDecimal oneMonth, BigDecimal threeMonths, BigDecimal sixMonths, BigDecimal oneYear, BigDecimal threeYears, BigDecimal fiveYears, BigDecimal sevenYears, BigDecimal tenYears) {
        this.mfId = mfId;
        this.rrDate = rrDate;
        this.oneMonth = oneMonth;
        this.threeMonths = threeMonths;
        this.sixMonths = sixMonths;
        this.oneYear = oneYear;
        this.threeYears = threeYears;
        this.fiveYears = fiveYears;
        this.sevenYears = sevenYears;
        this.tenYears = tenYears;
    }

    public String getMfId() {
        return mfId;
    }

    public Date getRrDate() {
        return rrDate;
    }

    public BigDecimal getOneMonth() {
        return oneMonth;
    }

    public BigDecimal getThreeMonths() {
        return threeMonths;
    }

    public BigDecimal getSixMonths() {
        return sixMonths;
    }

    public BigDecimal getOneYear() {
        return oneYear;
    }

    public BigDecimal getThreeYears() {
        return threeYears;
    }

    public BigDecimal getFiveYears() {
        return fiveYears;
    }

    public BigDecimal getSevenYears() {
        return sevenYears;
    }

    public BigDecimal getTenYears() {
        return tenYears;
    }
}
