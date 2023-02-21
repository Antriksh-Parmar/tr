package com.ind.tr.service.analytics;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface MfTotalEquityExposureService {

    Map<String, BigDecimal> getTotalEquityExposure(List<Integer> mfKeys);
}
