package com.ind.tr.controller;

import com.ind.tr.service.SolrService;
import com.ind.tr.service.refresh.MFCoreDataSaveService;
import com.ind.tr.service.refresh.MFHistoricalNavRefreshService;
import com.ind.tr.service.refresh.SbDataScanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "/refresh")
public class RefreshDataController {

    @Autowired
    private MFCoreDataSaveService mFCoreDataSaveService;

    @Autowired
    private MFHistoricalNavRefreshService mfHistoricalNavRefreshService;

    @Autowired
    private SbDataScanService sbDataScanService;

    @Autowired
    private SolrService solrService;

    @GetMapping("/mf-scriptbox-data")
    public void pullMfCoreData() {
        sbDataScanService.exploreAPI();
    }

    @GetMapping("/mf-mongo-data")
    public void refreshCore() {
        mFCoreDataSaveService.refresh();
    }

    @GetMapping("/historical-navs")
    public void refreshHistoricalNavs() {
        mfHistoricalNavRefreshService.refresh();
    }

    @GetMapping("/solr-indexes")
    public void refreshSolrIndexes() {
        solrService.refreshSolrIndexes();
    }

}
