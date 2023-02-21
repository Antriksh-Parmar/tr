package com.ind.tr.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Status {
    @JsonProperty("Success")
    SUCCESS,

    @JsonProperty("Failure")
    FAILURE
}