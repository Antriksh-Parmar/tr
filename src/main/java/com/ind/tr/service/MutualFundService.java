package com.ind.tr.service;

import com.ind.tr.controller.model.MutualFundInvestmentRequest;
import com.ind.tr.controller.model.MutualFundInvestmentResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MutualFundService {

    MutualFundInvestmentResponse addMutualFund(MutualFundInvestmentRequest request, UUID portfolioId);

    Optional<MutualFundInvestmentResponse> getMutualFund(UUID mfId);

    List<MutualFundInvestmentResponse> getAllMutualFuds(UUID portfolioId);

    void removeMutualFund(UUID mfId);

    void removeAllMutualFunds(UUID portfolioId);
}
