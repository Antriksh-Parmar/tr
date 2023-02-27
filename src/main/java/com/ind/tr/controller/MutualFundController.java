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
import java.util.UUID;

@RestController
@RequestMapping("/portfolios/{portfolioId}/mutual-funds")
public class MutualFundController {

    @Autowired
    private MutualFundService mutualFundService;

    @PostMapping("/")
    public void addMutualFund(
            @PathVariable UUID portfolioId,
            @RequestBody MutualFundInvestmentRequest mutualFundInvestmentRequest,
            @AuthenticationPrincipal User user) {
        mutualFundService.addMutualFund(mutualFundInvestmentRequest, portfolioId);
    }

    @GetMapping("/{mutualFundId}")
    public ResponseEntity<MutualFundInvestmentResponse> getMutualFund(@RequestParam("mutualFundId") UUID mutualFundId) {
         MutualFundInvestmentResponse response = mutualFundService.getMutualFund(mutualFundId);
         return ResponseEntity.ok(response);
    }

    @GetMapping("/")
    public ResponseEntity<List<MutualFundInvestmentResponse>> getMutualFunds(@PathVariable UUID portfolioId) {
        List<MutualFundInvestmentResponse> response = mutualFundService.getAllMutualFuds(portfolioId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{mutualFundId}")
    public void removeMutualFund(@RequestParam("mutualFundId") UUID mutualFundId) {
        mutualFundService.removeMutualFund(mutualFundId);
    }

    @DeleteMapping("/")
    public void removeAllMutualFunds(@PathVariable UUID portfolioId) {
        mutualFundService.removeAllMutualFunds(portfolioId);
    }
}
