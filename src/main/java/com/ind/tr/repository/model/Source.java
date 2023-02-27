package com.ind.tr.repository.model;

public enum Source {
    DIRECT, MUTUAL_FUND;

    Source fromValue(String value) {
        if(value.equals("DIRECT")) {
            return DIRECT;
        } else if(value.equals("MUTUAL_FUND")) {
            return MUTUAL_FUND;
        } else {
            throw new RuntimeException("Illegal value: Source");
        }
    }
}
