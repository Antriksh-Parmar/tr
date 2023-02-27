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
import java.util.UUID;

@Component
public class MutualFundServiceImpl implements MutualFundService {

    @Autowired
    private MutualFundDao mutualFundDao;

    @Autowired
    private MutualFundTranslator mutualFundTranslator;

    @Override
    public void addMutualFund(MutualFundInvestmentRequest request, UUID portfolioId) {
        //TODO verify portfolioId
        MutualFundInvestment mutualFundInvestment = mutualFundTranslator.fromMutualFundInvestmentRequest(request, portfolioId);
        MutualFundInvestmentEntity mutualFundInvestmentEntity = mutualFundTranslator.toMutualFundInvestmentEntity(mutualFundInvestment);
        mutualFundDao.addMutualFund(mutualFundInvestmentEntity);
    }

    @Override
    public MutualFundInvestmentResponse getMutualFund(UUID mfId) {
        MutualFundInvestment mutualFundInvestment = mutualFundTranslator.fromMutualFundInvestmentEntity(mutualFundDao.getMutualFund(mfId));
        return mutualFundTranslator.toMutualFundInvestmentResponse(mutualFundInvestment);
    }

    @Override
    public List<MutualFundInvestmentResponse> getAllMutualFuds(UUID portfolioId) {
        return mutualFundDao.getMutualFunds(portfolioId)
                .stream().map(mutualFundTranslator::fromMutualFundInvestmentEntity)
                .map(mutualFundTranslator::toMutualFundInvestmentResponse).toList();
    }

    @Override
    public void removeMutualFund(UUID mfId) {
        mutualFundDao.deleteMutualFund(mfId);
    }

    @Override
    public void removeAllMutualFunds(UUID portfolioId) {
        mutualFundDao.deleteAllMutualFunds(portfolioId);
    }
}
