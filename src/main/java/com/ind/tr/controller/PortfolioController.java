package com.ind.tr.controller;

import com.ind.tr.controller.model.PortfolioResponse;
import com.ind.tr.service.PortfolioService;
import com.ind.tr.service.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/portfolios")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    @PostMapping("/")
    public ResponseEntity<PortfolioResponse> createPortfolio(@AuthenticationPrincipal User user) {
        PortfolioResponse response = portfolioService.createPortfolio(user);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{portfolioId}")
    public ResponseEntity<PortfolioResponse> getPortfolio(@AuthenticationPrincipal User user, @PathVariable("portfolioId") UUID portfolioId) {
        Optional<PortfolioResponse> response = portfolioService.getPortfolio(portfolioId);
        return response.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/")
    public ResponseEntity<List<PortfolioResponse>> getPortfolios(@AuthenticationPrincipal User user) {
        List<PortfolioResponse> response = portfolioService.getPortfolios(user);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{portfolioId}")
    public ResponseEntity deletePortfolio(@AuthenticationPrincipal User user, @PathVariable("portfolioId") UUID portfolioId) {
        portfolioService.deletePortfolio(portfolioId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/")
    public ResponseEntity deletePortfolios(@AuthenticationPrincipal User user) {
        portfolioService.deletePortfolios(user);
        return ResponseEntity.ok().build();
    }

}
