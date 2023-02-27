package com.ind.tr.controller.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public class MutualFundInvestmentResponse {
    private UUID mutualFundId;
    private InvestmentType investmentType;
    private Optional<SipInterval> sipInterval;
    private Optional<LocalDate> sipStartDate;
    private Optional<BigDecimal> sipAmount;
    private Optional<LocalDate> lumpSumInvestmentDate;
    private Optional<BigDecimal> lumpSumInvestmentAmount;

    public MutualFundInvestmentResponse(UUID mutualFundId, InvestmentType investmentType, Optional<SipInterval> sipInterval, Optional<LocalDate> sipStartDate, Optional<BigDecimal> sipAmount, Optional<LocalDate> lumpSumInvestmentDate, Optional<BigDecimal> lumpSumInvestmentAmount) {
        this.mutualFundId = mutualFundId;
        this.investmentType = investmentType;
        this.sipInterval = sipInterval;
        this.sipStartDate = sipStartDate;
        this.sipAmount = sipAmount;
        this.lumpSumInvestmentDate = lumpSumInvestmentDate;
        this.lumpSumInvestmentAmount = lumpSumInvestmentAmount;
    }

    public UUID getMutualFundId() {
        return mutualFundId;
    }

    public InvestmentType getInvestmentType() {
        return investmentType;
    }

    public Optional<SipInterval> getSipInterval() {
        return sipInterval;
    }

    public Optional<LocalDate> getSipStartDate() {
        return sipStartDate;
    }

    public Optional<BigDecimal> getSipAmount() {
        return sipAmount;
    }

    public Optional<LocalDate> getLumpSumInvestmentDate() {
        return lumpSumInvestmentDate;
    }

    public Optional<BigDecimal> getLumpSumInvestmentAmount() {
        return lumpSumInvestmentAmount;
    }
}
