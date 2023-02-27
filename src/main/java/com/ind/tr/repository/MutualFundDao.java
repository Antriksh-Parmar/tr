package com.ind.tr.repository;

import com.ind.tr.repository.model.MutualFundInvestmentEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MutualFundDao {

    void addMutualFund(MutualFundInvestmentEntity mutualFundInvestmentEntity);

    Optional<MutualFundInvestmentEntity> getMutualFund(UUID mfId);

    List<MutualFundInvestmentEntity> getMutualFunds(UUID portfolioId);

    void deleteMutualFund(UUID id);

    void deleteAllMutualFunds(UUID portfolioId);

}
