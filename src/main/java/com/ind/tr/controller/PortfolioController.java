package com.ind.tr.controller;

import com.ind.tr.controller.model.PortfolioResponse;
import com.ind.tr.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/portfolios")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    @PostMapping("/")
    public ResponseEntity<PortfolioResponse> createPortfolio() {
        PortfolioResponse response = portfolioService.createPortfolio("");
        return ResponseEntity.ok(response);
    }

}
