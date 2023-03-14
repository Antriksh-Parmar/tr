package com.ind.tr.service.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class MutualFundNav {
    private final UUID id;
    private final BigDecimal nav;
    private final LocalDate nav_date;

    public MutualFundNav(UUID id, BigDecimal nav, LocalDate nav_date) {
        this.id = id;
        this.nav = nav;
        this.nav_date = nav_date;
    }

    public UUID getId() {
        return id;
    }

    public BigDecimal getNav() {
        return nav;
    }

    public LocalDate getNav_date() {
        return nav_date;
    }
}
