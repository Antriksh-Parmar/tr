package com.ind.tr.service.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public class MutualFundInvestment {
    private UUID id;
    private UUID mutualFundId;
    private UUID portfolioId;
    private InvestmentSource source;
    private InvestmentType investmentType;
    private Optional<SipInterval> sipInterval;
    private Optional<LocalDate> sipStartedDate;
    private Optional<BigDecimal> sipAmount;
    private Optional<LocalDate> lumpSumInvestmentDate;
    private Optional<BigDecimal> lumpSumInvestmentAmount;

    public MutualFundInvestment(UUID id, UUID mutualFundId, UUID portfolioId, InvestmentSource source, InvestmentType investmentType, Optional<SipInterval> sipInterval, Optional<LocalDate> sipStartedDate, Optional<BigDecimal> sipAmount, Optional<LocalDate> lumpSumInvestmentDate, Optional<BigDecimal> lumpSumInvestmentAmount) {
        this.id = id;
        this.mutualFundId = mutualFundId;
        this.portfolioId = portfolioId;
        this.source = source;
        this.investmentType = investmentType;
        this.sipInterval = sipInterval;
        this.sipStartedDate = sipStartedDate;
        this.sipAmount = sipAmount;
        this.lumpSumInvestmentDate = lumpSumInvestmentDate;
        this.lumpSumInvestmentAmount = lumpSumInvestmentAmount;
    }

    public UUID getId() {
        return id;
    }

    public UUID getMutualFundId() {
        return mutualFundId;
    }

    public UUID getPortfolioId() {
        return portfolioId;
    }

    public InvestmentSource getSource() {
        return source;
    }

    public InvestmentType getInvestmentType() {
        return investmentType;
    }

    public Optional<SipInterval> getSipInterval() {
        return sipInterval;
    }

    public Optional<LocalDate> getSipStartedDate() {
        return sipStartedDate;
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
