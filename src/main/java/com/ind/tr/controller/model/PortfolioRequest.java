package com.ind.tr.controller.model;

public class PortfolioRequest {

    private String userId;

    public PortfolioRequest() {
    }

    public PortfolioRequest(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
