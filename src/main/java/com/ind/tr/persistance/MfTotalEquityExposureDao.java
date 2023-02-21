package com.ind.tr.persistance;

import com.ind.tr.persistance.model.analytics.MfEquityExposureRead;

import java.util.List;

public interface MfTotalEquityExposureDao {
    List<MfEquityExposureRead> getTotalEquityExposure(List<Integer> mfKeys);
}