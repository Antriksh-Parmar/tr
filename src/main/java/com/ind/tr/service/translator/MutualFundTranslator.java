package com.ind.tr.service.translator;

import com.ind.tr.controller.model.MutualFundInvestmentRequest;
import com.ind.tr.controller.model.MutualFundInvestmentResponse;
import com.ind.tr.repository.model.MutualFundInvestmentEntity;
import com.ind.tr.service.model.MutualFundInvestment;

import java.util.UUID;

public interface MutualFundTranslator {

    MutualFundInvestment fromMutualFundInvestmentRequest(
            MutualFundInvestmentRequest request,
            UUID portfolioId
    );

    MutualFundInvestmentResponse toMutualFundInvestmentResponse(MutualFundInvestment mutualFundInvestment);

    MutualFundInvestmentEntity toMutualFundInvestmentEntity(MutualFundInvestment mutualFundInvestment);

    MutualFundInvestment fromMutualFundInvestmentEntity(MutualFundInvestmentEntity entity);

}
