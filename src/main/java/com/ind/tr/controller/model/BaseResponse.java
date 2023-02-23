package com.ind.tr.controller.model;

import com.ind.tr.controller.model.search.Status;

public abstract class BaseResponse {
    Status status;

    protected BaseResponse(Status status) {
        this.status = status;
    }
}