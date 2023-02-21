package com.ind.tr.controller;

import com.ind.tr.controller.model.SearchRecommendationResponse;
import com.ind.tr.controller.model.Status;
import com.ind.tr.service.SolrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
public class SearchController {

    @Autowired
    private SolrService solrService;

    @GetMapping("/recommend")
    public ResponseEntity<SearchRecommendationResponse> recommend(@RequestParam("q") String key) {
        if (key == null || key.isEmpty() || key.isBlank()) {
            return ResponseEntity.ok(new SearchRecommendationResponse(Status.FAILURE, Collections.emptyList()));
        }
        return ResponseEntity.ok(solrService.getRecommendations(key));
    }

    @GetMapping("/refresh-solr-indexes")
    public void refreshSolrIndexes() {
        solrService.refreshSolrIndexes();
    }
}
