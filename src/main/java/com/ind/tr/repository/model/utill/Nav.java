package com.ind.tr.repository.model.utill;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Nav {
    private int schemaCode;
    private String isin;
    private BigDecimal nav;
    private Timestamp navDate;

    public Nav(int schemaCode, String isin, BigDecimal nav, Timestamp navDate) {
        this.schemaCode = schemaCode;
        this.isin = isin;
        this.nav = nav;
        this.navDate = navDate;
    }

    public int getSchemaCode() {
        return schemaCode;
    }

    public void setSchemaCode(int schemaCode) {
        this.schemaCode = schemaCode;
    }

    public String getIsin() {
        return isin;
    }

    public void setIsin(String isin) {
        this.isin = isin;
    }

    public BigDecimal getNav() {
        return nav;
    }

    public void setNav(BigDecimal nav) {
        this.nav = nav;
    }

    public Timestamp getNavDate() {
        return navDate;
    }

    public void setNavDate(Timestamp navDate) {
        this.navDate = navDate;
    }
}
