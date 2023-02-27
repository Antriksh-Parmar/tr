package com.ind.tr.repository;

import com.ind.tr.repository.model.MutualFundInvestmentEntity;

import java.util.List;
import java.util.UUID;

public interface MutualFundDao {

    void addMutualFund(MutualFundInvestmentEntity mutualFundInvestmentEntity);

    MutualFundInvestmentEntity getMutualFund(UUID mfId);

    List<MutualFundInvestmentEntity> getMutualFunds(UUID portfolioId);

    void deleteMutualFund(UUID mfId);

    void deleteAllMutualFunds(UUID portfolioId);

}
