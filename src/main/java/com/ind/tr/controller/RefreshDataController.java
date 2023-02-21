package com.ind.tr.controller;

import com.ind.tr.service.refresh.MFCoreDataSaveService;
import com.ind.tr.service.refresh.MFHistoricalNavRefreshService;
import com.ind.tr.service.refresh.SbDataScanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RefreshDataController {

    @Autowired
    private MFCoreDataSaveService mFCoreDataSaveService;

    @Autowired
    private MFHistoricalNavRefreshService mfHistoricalNavRefreshService;

    @Autowired
    private SbDataScanService sbDataScanService;

    @GetMapping("/pull-mf-core-data")
    public void pullMfCoreData() {
        sbDataScanService.exploreAPI();
    }

    @GetMapping("/update-mf-core-data")
    public void refreshCore() {
        mFCoreDataSaveService.refresh();
    }

    @GetMapping("/update-mf-historical-navs")
    public void refreshHistoricalNavs() {
        mfHistoricalNavRefreshService.refresh();
    }

}
