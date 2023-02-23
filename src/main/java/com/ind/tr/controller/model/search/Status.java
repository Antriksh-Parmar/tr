package com.ind.tr.controller.model.search;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Status {
    @JsonProperty("Success")
    SUCCESS,

    @JsonProperty("Failure")
    FAILURE
}