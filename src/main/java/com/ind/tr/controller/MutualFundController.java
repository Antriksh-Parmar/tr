package com.ind.tr.controller;

import com.ind.tr.controller.model.MutualFundInvestmentRequest;
import com.ind.tr.controller.model.MutualFundInvestmentResponse;
import com.ind.tr.service.MutualFundService;
import com.ind.tr.service.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/portfolios/{portfolioId}/mutual-funds")
public class MutualFundController {

    @Autowired
    private MutualFundService mutualFundService;

    @PostMapping("/")
    public ResponseEntity<MutualFundInvestmentResponse> addMutualFund(
            @PathVariable UUID portfolioId,
            @RequestBody MutualFundInvestmentRequest mutualFundInvestmentRequest,
            @AuthenticationPrincipal User user) {
        MutualFundInvestmentResponse mutualFundInvestmentResponse = mutualFundService.addMutualFund(mutualFundInvestmentRequest, portfolioId);
        return ResponseEntity.ok(mutualFundInvestmentResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MutualFundInvestmentResponse> getMutualFund(@PathVariable("id") UUID mutualFundId) {
         Optional<MutualFundInvestmentResponse> response = mutualFundService.getMutualFund(mutualFundId);
         return response.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/")
    public ResponseEntity<List<MutualFundInvestmentResponse>> getMutualFunds(@PathVariable UUID portfolioId) {
        List<MutualFundInvestmentResponse> response = mutualFundService.getAllMutualFuds(portfolioId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public void removeMutualFund(@PathVariable("id") UUID id) {
        mutualFundService.removeMutualFund(id);
    }

    @DeleteMapping("/")
    public void removeAllMutualFunds(@PathVariable UUID portfolioId) {
        mutualFundService.removeAllMutualFunds(portfolioId);
    }
}
