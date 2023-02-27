package com.ind.tr.controller.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class MutualFundInvestmentRequest {
    private UUID mutualFundId;
    private InvestmentType investmentType;
    private SipInterval sipInterval;
    private LocalDate sipStartDate;
    private BigDecimal sipAmount;
    private LocalDate lumpSumInvestmentDate;
    private BigDecimal lumpSumInvestmentAmount;

    public MutualFundInvestmentRequest(){}

    public MutualFundInvestmentRequest(UUID mutualFundId, InvestmentType investmentType, SipInterval sipInterval, LocalDate sipStartDate, BigDecimal sipAmount, LocalDate lumpSumInvestmentDate, BigDecimal lumpSumInvestmentAmount) {
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

    public SipInterval getSipInterval() {
        return sipInterval;
    }

    public LocalDate getSipStartDate() {
        return sipStartDate;
    }

    public BigDecimal getSipAmount() {
        return sipAmount;
    }

    public LocalDate getLumpSumInvestmentDate() {
        return lumpSumInvestmentDate;
    }

    public BigDecimal getLumpSumInvestmentAmount() {
        return lumpSumInvestmentAmount;
    }
}
