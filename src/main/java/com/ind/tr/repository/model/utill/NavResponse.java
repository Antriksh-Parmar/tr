package com.ind.tr.repository.model.utill;

import java.math.BigDecimal;
import java.util.Date;

public class NavResponse {
    BigDecimal nav;
    BigDecimal adjustedNav;
    Date navDate;

    public NavResponse(BigDecimal nav, BigDecimal adjustedNav, Date navDate) {
        this.nav = nav;
        this.adjustedNav = adjustedNav;
        this.navDate = navDate;
    }

    public BigDecimal getNav() {
        return nav;
    }

    public void setNav(BigDecimal nav) {
        this.nav = nav;
    }

    public BigDecimal getAdjustedNav() {
        return adjustedNav;
    }

    public void setAdjustedNav(BigDecimal adjustedNav) {
        this.adjustedNav = adjustedNav;
    }

    public Date getNavDate() {
        return navDate;
    }

    public void setNavDate(Date navDate) {
        this.navDate = navDate;
    }
}