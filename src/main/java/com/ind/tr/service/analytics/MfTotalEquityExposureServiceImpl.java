package com.ind.tr.service.analytics;

import com.ind.tr.persistance.MfTotalEquityExposureDao;
import com.ind.tr.persistance.model.analytics.MfEquityExposureRead;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class MfTotalEquityExposureServiceImpl implements MfTotalEquityExposureService {

    @Autowired
    private MfTotalEquityExposureDao mfTotalEquityExposureDao;

    public Map<String, BigDecimal> getTotalEquityExposure(List<Integer> mfKeys) {
        List<MfEquityExposureRead> mfEquityExposureReads = mfTotalEquityExposureDao.getTotalEquityExposure(mfKeys);

        return Collections.emptyMap();
    }
}

/**
 * Mf1 - 1,00,000
 *      stock1: 70%    70,000
 *      stock2: 30%    30,000
 *
 * Mf2 - 20,000
 *      stock1: 20%    4,000
 *      stock2: 40%    8,000
 *      stock3: 40%    8,000
 *
 *      Method#1
 *      stock1 - 74,000  - 61.67
 *      stock2 - 38,000  - 31.67
 *      stock3 - 8,000   - 6.67
 *
 *      Method#2
 *      stock1: 90% - 45
 *      stock2: 70% - 35
 *      stock3: 40% - 20
 *
 */