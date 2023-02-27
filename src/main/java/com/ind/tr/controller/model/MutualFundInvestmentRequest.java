package com.ind.tr.controller.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public class MutualFundInvestmentRequest {
    private UUID mutualFundId;
    private InvestmentType investmentType;
    private Optional<SipInterval> sipInterval;
    private Optional<LocalDate> sipStartDate;
    private Optional<BigDecimal> sipAmount;
    private Optional<LocalDate> lumpSumInvestmentDate;
    private Optional<BigDecimal> lumpSumInvestmentAmount;

    public MutualFundInvestmentRequest(){}

    public MutualFundInvestmentRequest(UUID mutualFundId, InvestmentType investmentType, Optional<SipInterval> sipInterval, Optional<LocalDate> sipStartDate, Optional<BigDecimal> sipAmount, Optional<LocalDate> lumpSumInvestmentDate, Optional<BigDecimal> lumpSumInvestmentAmount) {
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

    public void setMutualFundId(UUID mutualFundId) {
        this.mutualFundId = mutualFundId;
    }

    public InvestmentType getInvestmentType() {
        return investmentType;
    }

    public void setInvestmentType(InvestmentType investmentType) {
        this.investmentType = investmentType;
    }

    public Optional<SipInterval> getSipInterval() {
        return sipInterval;
    }

    public void setSipInterval(Optional<SipInterval> sipInterval) {
        this.sipInterval = sipInterval;
    }

    public Optional<LocalDate> getSipStartDate() {
        return sipStartDate;
    }

    public void setSipStartDate(Optional<LocalDate> sipStartDate) {
        this.sipStartDate = sipStartDate;
    }

    public Optional<BigDecimal> getSipAmount() {
        return sipAmount;
    }

    public void setSipAmount(Optional<BigDecimal> sipAmount) {
        this.sipAmount = sipAmount;
    }

    public Optional<LocalDate> getLumpSumInvestmentDate() {
        return lumpSumInvestmentDate;
    }

    public void setLumpSumInvestmentDate(Optional<LocalDate> lumpSumInvestmentDate) {
        this.lumpSumInvestmentDate = lumpSumInvestmentDate;
    }

    public Optional<BigDecimal> getLumpSumInvestmentAmount() {
        return lumpSumInvestmentAmount;
    }

    public void setLumpSumInvestmentAmount(Optional<BigDecimal> lumpSumInvestmentAmount) {
        this.lumpSumInvestmentAmount = lumpSumInvestmentAmount;
    }
}
