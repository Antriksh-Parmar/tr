package com.ind.tr.repository.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.UUID;

public class MutualFundInvestmentEntity {
    private UUID id;
    private UUID mutualFundId;
    private UUID portfolioId;
    private Source source;
    private InvestmentType investmentType;
    private SipInterval sipInterval;
    private Date sipStartDate;
    private BigDecimal sipAmount;
    private Date lumpSumInvestmentDate;
    private BigDecimal lumpSumInvestmentAmount;
    private Date createdDate;
    private Date updatedDate;

    public MutualFundInvestmentEntity(UUID id, UUID mutualFundId, UUID portfolioId, Source source, InvestmentType investmentType, SipInterval sipInterval, Date sipStartDate, BigDecimal sipAmount, Date lumpSumInvestmentDate, BigDecimal lumpSumInvestmentAmount, Date createdDate, Date updatedDate) {
        this.id = id;
        this.mutualFundId = mutualFundId;
        this.portfolioId = portfolioId;
        this.source = source;
        this.investmentType = investmentType;
        this.sipInterval = sipInterval;
        this.sipStartDate = sipStartDate;
        this.sipAmount = sipAmount;
        this.lumpSumInvestmentDate = lumpSumInvestmentDate;
        this.lumpSumInvestmentAmount = lumpSumInvestmentAmount;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public UUID getMutualFundId() {
        return mutualFundId;
    }

    public void setMutualFundId(UUID mutualFundId) {
        this.mutualFundId = mutualFundId;
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

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
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

    public Date getSipStartDate() {
        return sipStartDate;
    }

    public void setSipStartDate(Date sipStartDate) {
        this.sipStartDate = sipStartDate;
    }

    public BigDecimal getSipAmount() {
        return sipAmount;
    }

    public void setSipAmount(BigDecimal sipAmount) {
        this.sipAmount = sipAmount;
    }

    public Date getLumpSumInvestmentDate() {
        return lumpSumInvestmentDate;
    }

    public void setLumpSumInvestmentDate(Date lumpSumInvestmentDate) {
        this.lumpSumInvestmentDate = lumpSumInvestmentDate;
    }

    public BigDecimal getLumpSumInvestmentAmount() {
        return lumpSumInvestmentAmount;
    }

    public void setLumpSumInvestmentAmount(BigDecimal lumpSumInvestmentAmount) {
        this.lumpSumInvestmentAmount = lumpSumInvestmentAmount;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }
}
