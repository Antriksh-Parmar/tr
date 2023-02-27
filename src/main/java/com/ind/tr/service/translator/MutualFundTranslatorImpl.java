package com.ind.tr.service.translator;

import com.ind.tr.controller.model.MutualFundInvestmentRequest;
import com.ind.tr.controller.model.MutualFundInvestmentResponse;
import com.ind.tr.repository.model.MutualFundInvestmentEntity;
import com.ind.tr.repository.model.Source;
import com.ind.tr.service.ISTClock;
import com.ind.tr.service.model.InvestmentSource;
import com.ind.tr.service.model.InvestmentType;
import com.ind.tr.service.model.MutualFundInvestment;
import com.ind.tr.service.model.SipInterval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.Optional;
import java.util.UUID;

@Component
public class MutualFundTranslatorImpl implements MutualFundTranslator {

    @Autowired
    private ISTClock istClock;

    @Override
    public MutualFundInvestment fromMutualFundInvestmentRequest(
            MutualFundInvestmentRequest request,
            UUID portfolioId) {
        return new MutualFundInvestment(
                request.getMutualFundId(),
                portfolioId,
                InvestmentSource.DIRECT,
                fromRequestInvestmentType(request.getInvestmentType()),
                request.getSipInterval().map(this::fromRequestSipInterval),
                request.getSipStartDate(),
                request.getSipAmount(),
                request.getLumpSumInvestmentDate(),
                request.getLumpSumInvestmentAmount()
        );
    }

    @Override
    public MutualFundInvestmentResponse toMutualFundInvestmentResponse(MutualFundInvestment mutualFundInvestment) {
        return new MutualFundInvestmentResponse(
                mutualFundInvestment.getMutualFundId(),
                toResponseInvestmentType(mutualFundInvestment.getInvestmentType()),
                mutualFundInvestment.getSipInterval().map(this::toResponseSipInterval),
                mutualFundInvestment.getSipStartedDate(),
                mutualFundInvestment.getSipAmount(),
                mutualFundInvestment.getLumpSumInvestmentDate(),
                mutualFundInvestment.getLumpSumInvestmentAmount()
        );
    }

    @Override
    public MutualFundInvestmentEntity toMutualFundInvestmentEntity(MutualFundInvestment mutualFundInvestment) {
        return new MutualFundInvestmentEntity(
                UUID.randomUUID(),
                mutualFundInvestment.getMutualFundId(),
                mutualFundInvestment.getPortfolioId(),
                toEntitySource(mutualFundInvestment.getSource()),
                toEntityInvestmentType(mutualFundInvestment.getInvestmentType()),
                mutualFundInvestment.getSipInterval().map(this::toEntitySipInterval).orElse(null),
                mutualFundInvestment.getSipStartedDate().map(Date::valueOf).orElse(null),
                mutualFundInvestment.getSipAmount().orElse(null),
                mutualFundInvestment.getLumpSumInvestmentDate().map(Date::valueOf).orElse(null),
                mutualFundInvestment.getLumpSumInvestmentAmount().orElse(null),
                istClock.getTodaySqlDate(),
                istClock.getTodaySqlDate()

        );
    }

    @Override
    public MutualFundInvestment fromMutualFundInvestmentEntity(MutualFundInvestmentEntity entity) {
        return new MutualFundInvestment(
                entity.getMutualFundId(),
                entity.getPortfolioId(),
                fromEntitySource(entity.getSource()),
                fromEntityInvestmentType(entity.getInvestmentType()),
                Optional.ofNullable((entity.getSipInterval() == null) ? null : fromEntitySipInterval(entity.getSipInterval())),
                Optional.ofNullable((entity.getSipStartDate() == null) ? null : entity.getSipStartDate().toLocalDate()),
                Optional.ofNullable(entity.getSipAmount()),
                Optional.ofNullable((entity.getLumpSumInvestmentDate() == null) ? null : entity.getLumpSumInvestmentDate().toLocalDate()),
                Optional.ofNullable(entity.getLumpSumInvestmentAmount())
        );
    }

    private InvestmentSource fromEntitySource(Source source) {
        return switch (source) {
            case DIRECT -> InvestmentSource.DIRECT;
            case MUTUAL_FUND -> InvestmentSource.MUTUAL_FUND;
        };
    }

    private InvestmentType fromEntityInvestmentType(com.ind.tr.repository.model.InvestmentType investmentType) {
        return switch (investmentType) {
            case SIP -> InvestmentType.SIP;
            case LUMP_SUM -> InvestmentType.LUMP_SUM;
        };
    }

    private SipInterval fromEntitySipInterval(com.ind.tr.repository.model.SipInterval sipInterval) {
        return switch (sipInterval) {
            case DAILY -> SipInterval.DAILY;
            case WEEKLY -> SipInterval.WEEKLY;
            case FORTNIGHTLY -> SipInterval.FORTNIGHTLY;
            case MONTHLY -> SipInterval.MONTHLY;
            case QUARTERLY -> SipInterval.QUARTERLY;
            case HALF_YEARLY -> SipInterval.HALF_YEARLY;
            case YEARLY -> SipInterval.YEARLY;
        };
    }

    private com.ind.tr.controller.model.SipInterval toResponseSipInterval(SipInterval sipInterval) {
        return switch (sipInterval) {
            case DAILY -> com.ind.tr.controller.model.SipInterval.DAILY;
            case WEEKLY -> com.ind.tr.controller.model.SipInterval.WEEKLY;
            case FORTNIGHTLY -> com.ind.tr.controller.model.SipInterval.FORTNIGHTLY;
            case MONTHLY -> com.ind.tr.controller.model.SipInterval.MONTHLY;
            case QUARTERLY -> com.ind.tr.controller.model.SipInterval.QUARTERLY;
            case HALF_YEARLY -> com.ind.tr.controller.model.SipInterval.HALF_YEARLY;
            case YEARLY -> com.ind.tr.controller.model.SipInterval.YEARLY;
        };
    }

    private com.ind.tr.controller.model.InvestmentType toResponseInvestmentType(InvestmentType investmentType) {
        return switch (investmentType) {
            case SIP -> com.ind.tr.controller.model.InvestmentType.SIP;
            case LUMP_SUM -> com.ind.tr.controller.model.InvestmentType.LUMP_SUM;
        };
    }

    private com.ind.tr.repository.model.SipInterval toEntitySipInterval(SipInterval sipInterval) {
        return switch (sipInterval) {
            case DAILY -> com.ind.tr.repository.model.SipInterval.DAILY;
            case WEEKLY -> com.ind.tr.repository.model.SipInterval.WEEKLY;
            case FORTNIGHTLY -> com.ind.tr.repository.model.SipInterval.FORTNIGHTLY;
            case MONTHLY -> com.ind.tr.repository.model.SipInterval.MONTHLY;
            case QUARTERLY -> com.ind.tr.repository.model.SipInterval.QUARTERLY;
            case HALF_YEARLY -> com.ind.tr.repository.model.SipInterval.HALF_YEARLY;
            case YEARLY -> com.ind.tr.repository.model.SipInterval.YEARLY;
        };
    }

    private com.ind.tr.repository.model.InvestmentType toEntityInvestmentType(InvestmentType investmentType) {
        return switch (investmentType) {
            case SIP -> com.ind.tr.repository.model.InvestmentType.SIP;
            case LUMP_SUM -> com.ind.tr.repository.model.InvestmentType.LUMP_SUM;
        };
    }

    private Source toEntitySource(InvestmentSource source) {
        return switch (source) {
            case DIRECT -> Source.DIRECT;
            case MUTUAL_FUND -> Source.MUTUAL_FUND;
        };
    }

    private InvestmentType fromRequestInvestmentType(com.ind.tr.controller.model.InvestmentType investmentType) {
        return switch (investmentType) {
            case SIP -> InvestmentType.SIP;
            case LUMP_SUM -> InvestmentType.LUMP_SUM;
        };
    }

    private SipInterval fromRequestSipInterval(com.ind.tr.controller.model.SipInterval sipInterval) {
        return switch (sipInterval) {
            case DAILY -> SipInterval.DAILY;
            case WEEKLY -> SipInterval.WEEKLY;
            case FORTNIGHTLY -> SipInterval.FORTNIGHTLY;
            case MONTHLY -> SipInterval.MONTHLY;
            case QUARTERLY -> SipInterval.QUARTERLY;
            case HALF_YEARLY -> SipInterval.HALF_YEARLY;
            case YEARLY -> SipInterval.YEARLY;
        };
    }
}
