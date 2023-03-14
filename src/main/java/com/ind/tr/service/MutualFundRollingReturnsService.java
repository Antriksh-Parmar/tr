package com.ind.tr.service;

import java.util.List;

public interface MutualFundRollingReturnsService {
    void refreshMutualFundsRollingReturns();

    void refreshMutualFundsRollingReturns(List<String> fundIds);
}
