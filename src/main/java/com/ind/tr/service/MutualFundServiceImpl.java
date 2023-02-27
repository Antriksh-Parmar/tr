package com.ind.tr.service;

import com.ind.tr.controller.model.MutualFundInvestmentRequest;
import com.ind.tr.controller.model.MutualFundInvestmentResponse;
import com.ind.tr.repository.MutualFundDao;
import com.ind.tr.repository.model.MutualFundInvestmentEntity;
import com.ind.tr.service.model.MutualFundInvestment;
import com.ind.tr.service.translator.MutualFundTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class MutualFundServiceImpl implements MutualFundService {

    @Autowired
    private MutualFundDao mutualFundDao;

    @Autowired
    private MutualFundTranslator mutualFundTranslator;

    @Override
    public MutualFundInvestmentResponse addMutualFund(MutualFundInvestmentRequest request, UUID portfolioId) {
        //TODO verify portfolioId
        MutualFundInvestment mutualFundInvestment = mutualFundTranslator.fromMutualFundInvestmentRequest(request, portfolioId);
        MutualFundInvestmentEntity mutualFundInvestmentEntity = mutualFundTranslator.toMutualFundInvestmentEntity(mutualFundInvestment);
        mutualFundDao.addMutualFund(mutualFundInvestmentEntity);
        return mutualFundTranslator.toMutualFundInvestmentResponse(mutualFundInvestment);
    }

    @Override
    public Optional<MutualFundInvestmentResponse> getMutualFund(UUID mfId) {
        Optional<MutualFundInvestment> mutualFundInvestment = mutualFundDao.getMutualFund(mfId).map(mutualFundTranslator::fromMutualFundInvestmentEntity);
        return mutualFundInvestment.map(mutualFundTranslator::toMutualFundInvestmentResponse);
    }

    @Override
    public List<MutualFundInvestmentResponse> getAllMutualFuds(UUID portfolioId) {
        return mutualFundDao.getMutualFunds(portfolioId)
                .stream().map(mutualFundTranslator::fromMutualFundInvestmentEntity)
                .map(mutualFundTranslator::toMutualFundInvestmentResponse).toList();
    }

    @Override
    public void removeMutualFund(UUID id) {
        mutualFundDao.deleteMutualFund(id);
    }

    @Override
    public void removeAllMutualFunds(UUID portfolioId) {
        mutualFundDao.deleteAllMutualFunds(portfolioId);
    }
}
