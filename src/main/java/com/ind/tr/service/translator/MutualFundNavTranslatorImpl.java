package com.ind.tr.service.translator;

import com.ind.tr.repository.model.MutualFundNavEntity;
import com.ind.tr.service.model.MutualFundNav;
import org.springframework.stereotype.Component;

@Component
public class MutualFundNavTranslatorImpl implements MutualFundNavTranslator {

    @Override
    public MutualFundNav fromMutualFundNavEntity(MutualFundNavEntity mutualFundNavEntity) {
        return new MutualFundNav(
                mutualFundNavEntity.getId(),
                mutualFundNavEntity.getNav(),
                mutualFundNavEntity.getNav_date().toLocalDate()
        );
    }
}
