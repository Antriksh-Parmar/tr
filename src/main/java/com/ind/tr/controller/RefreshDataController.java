package com.ind.tr.controller;

import com.ind.tr.service.SolrService;
import com.ind.tr.service.refresh.MFHistoricalNavRefreshService;
import com.ind.tr.service.refresh.SbDataScanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/refresh")
public class RefreshDataController {

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

    @GetMapping("/historical-navs")
    public void refreshHistoricalNavs() {
        mfHistoricalNavRefreshService.refresh();
    }

    @GetMapping("/solr-indexes")
    public void refreshSolrIndexes() {
        solrService.refreshSolrIndexes();
    }

}
