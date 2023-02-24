package com.ind.tr.controller.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public class MutualFundRequest {
    private UUID id;
    private UUID portfolioId;
    private UUID mutualFundId;
    private InvestmentSource investmentSource;
    private InvestmentType investmentType;
    private SipInterval sipInterval;
    private Optional<LocalDate> sipStartDate;
    private Optional<BigDecimal> sipAmount;
    private Optional<LocalDate> lumpsumStartDate;
    private Optional<BigDecimal> lumpsumAmount;

    public MutualFundRequest(){}

    public MutualFundRequest(UUID id, UUID portfolioId, UUID mutualFundId, InvestmentSource investmentSource, InvestmentType investmentType, SipInterval sipInterval, Optional<LocalDate> sipStartDate, Optional<BigDecimal> sipAmount, Optional<LocalDate> lumpsumStartDate, Optional<BigDecimal> lumpsumAmount) {
        this.id = id;
        this.portfolioId = portfolioId;
        this.mutualFundId = mutualFundId;
        this.investmentSource = investmentSource;
        this.investmentType = investmentType;
        this.sipInterval = sipInterval;
        this.sipStartDate = sipStartDate;
        this.sipAmount = sipAmount;
        this.lumpsumStartDate = lumpsumStartDate;
        this.lumpsumAmount = lumpsumAmount;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(UUID portfolioId) {
        this.portfolioId = portfolioId;
    }

    public UUID getMutualFundId() {
        return mutualFundId;
    }

    public void setMutualFundId(UUID mutualFundId) {
        this.mutualFundId = mutualFundId;
    }

    public InvestmentSource getInvestmentSource() {
        return investmentSource;
    }

    public void setInvestmentSource(InvestmentSource investmentSource) {
        this.investmentSource = investmentSource;
    }

    public InvestmentType getInvestmentType() {
        return investmentType;
    }

    public void setInvestmentType(InvestmentType investmentType) {
        this.investmentType = investmentType;
    }

    public SipInterval getSipInterval() {
        return sipInterval;
    }

    public void setSipInterval(SipInterval sipInterval) {
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

    public Optional<LocalDate> getLumpsumStartDate() {
        return lumpsumStartDate;
    }

    public void setLumpsumStartDate(Optional<LocalDate> lumpsumStartDate) {
        this.lumpsumStartDate = lumpsumStartDate;
    }

    public Optional<BigDecimal> getLumpsumAmount() {
        return lumpsumAmount;
    }

    public void setLumpsumAmount(Optional<BigDecimal> lumpsumAmount) {
        this.lumpsumAmount = lumpsumAmount;
    }
}
