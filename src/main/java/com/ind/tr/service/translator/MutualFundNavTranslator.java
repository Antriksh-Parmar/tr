package com.ind.tr.service.translator;

import com.ind.tr.repository.model.MutualFundNavEntity;
import com.ind.tr.service.model.MutualFundNav;

public interface MutualFundNavTranslator {

    MutualFundNav fromMutualFundNavEntity(MutualFundNavEntity mutualFundNavEntity);
}
