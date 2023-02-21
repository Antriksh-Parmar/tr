package com.ind.tr.controller.model;

public abstract class BaseResponse {
    Status status;

    protected BaseResponse(Status status) {
        this.status = status;
    }
}