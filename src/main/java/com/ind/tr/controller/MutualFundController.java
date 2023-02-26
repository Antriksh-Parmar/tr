package com.ind.tr.controller;

import com.ind.tr.controller.model.MutualFundRequest;
import com.ind.tr.service.model.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/portfolios/{portfolioId}/mutual-funds")
public class MutualFundController {

    @PostMapping("/{mutualFundId}")
    public void addMutualFund(
            @PathVariable UUID portfolioId,
            @PathVariable UUID mutualFundId,
            @RequestBody MutualFundRequest mutualFundRequest,
            @AuthenticationPrincipal User user) {

    }
}
