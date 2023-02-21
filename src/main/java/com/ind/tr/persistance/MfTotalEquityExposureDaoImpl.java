package com.ind.tr.persistance;

import com.ind.tr.persistance.model.analytics.MfEquityExposureRead;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class MfTotalEquityExposureDaoImpl implements MfTotalEquityExposureDao {

    public List<MfEquityExposureRead> getTotalEquityExposure(List<Integer> mfKeys) {

        return Collections.emptyList();
    }
}
