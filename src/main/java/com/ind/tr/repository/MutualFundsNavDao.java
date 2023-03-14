package com.ind.tr.repository;

import com.ind.tr.repository.model.MutualFundNavEntity;

import java.util.List;

public interface MutualFundsNavDao {

    List<MutualFundNavEntity> queryAllMutualFundsNavs();

    List<MutualFundNavEntity> queryMutualFundsNavs(List<String> fundIds);
}
