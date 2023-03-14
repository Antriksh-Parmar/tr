package com.ind.tr.repository;

import com.ind.tr.repository.model.RollingReturnEntity;

import java.util.List;

public interface MutualFundsRollingReturnsDao {
    void saveRollingReturns(List<RollingReturnEntity> rollingReturnEntities);
}
