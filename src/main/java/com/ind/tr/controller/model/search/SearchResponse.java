package com.ind.tr.controller.model.search;

import com.ind.tr.controller.model.BaseResponse;

import java.util.List;

public class SearchResponse extends BaseResponse {
    List<MutualFund> mutualFunds;

    public SearchResponse(Status status, List<MutualFund> mutualFunds) {
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

