package com.ind.tr.service.translator;

import com.ind.tr.repository.model.RollingReturnEntity;
import com.ind.tr.service.model.RollingReturn;
import org.springframework.stereotype.Component;

import java.sql.Date;

@Component
public class RollingReturnsTranslatorImpl implements RollingReturnsTranslator {

    @Override
    public RollingReturnEntity toRollingReturnEntity(RollingReturn rollingReturn) {
        return new RollingReturnEntity(
                rollingReturn.getMfId().toString(),
                Date.valueOf(rollingReturn.getRrDate()),
                rollingReturn.getOneMonth(),
                rollingReturn.getThreeMonths(),
                rollingReturn.getSixMonths(),
                rollingReturn.getOneYear(),
                rollingReturn.getThreeYears(),
                rollingReturn.getFiveYears(),
                rollingReturn.getSevenYears(),
                rollingReturn.getTenYears()
        );
    }
}
