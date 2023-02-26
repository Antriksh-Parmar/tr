package com.ind.tr.controller;

import com.ind.tr.controller.model.search.SearchResponse;
import com.ind.tr.controller.model.search.Status;
import com.ind.tr.service.SolrService;
import com.ind.tr.service.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
public class SearchController {

    @Autowired
    private SolrService solrService;

    @GetMapping("/search")
    public ResponseEntity<SearchResponse> recommend(
            @RequestParam("q") String key,
            @AuthenticationPrincipal User user
    ) {
        if (key == null || key.isEmpty() || key.isBlank()) {
            return ResponseEntity.ok(new SearchResponse(Status.FAILURE, Collections.emptyList()));
        }
        return ResponseEntity.ok(solrService.getRecommendations(key));
    }
}
