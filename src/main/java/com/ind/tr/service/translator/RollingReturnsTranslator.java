package com.ind.tr.service.translator;

import com.ind.tr.repository.model.RollingReturnEntity;
import com.ind.tr.service.model.RollingReturn;

public interface RollingReturnsTranslator {

    RollingReturnEntity toRollingReturnEntity(RollingReturn rollingReturn);
}
