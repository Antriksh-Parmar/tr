package com.ind.tr.repository.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.UUID;

public class MutualFundNavEntity {
    private final UUID id;
    private final String isin;
    private final BigDecimal nav;
    private final Date nav_date;

    public MutualFundNavEntity(UUID id, String isin, BigDecimal nav, Date nav_date) {
        this.id = id;
        this.isin = isin;
        this.nav = nav;
        this.nav_date = nav_date;
    }

    public UUID getId() {
        return id;
    }

    public String getIsin() {
        return isin;
    }

    public BigDecimal getNav() {
        return nav;
    }

    public Date getNav_date() {
        return nav_date;
    }
}
