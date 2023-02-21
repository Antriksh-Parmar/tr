package com.ind.tr.controller.model;

import java.util.List;

public class SearchRecommendationResponse extends BaseResponse {
    List<MutualFund> mutualFunds;

    public SearchRecommendationResponse(Status status, List<MutualFund> mutualFunds) {
        super(status);
        this.mutualFunds = mutualFunds;
    }

    public List<MutualFund> getMutualFunds() {
        return mutualFunds;
    }

    public void setMutualFunds(List<MutualFund> mutualFunds) {
        this.mutualFunds = mutualFunds;
    }
}

