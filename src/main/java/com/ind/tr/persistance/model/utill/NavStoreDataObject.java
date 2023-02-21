package com.ind.tr.persistance.model.utill;

import java.util.List;

public class NavStoreDataObject {
    List<NavResponse> navs;
    int mfId;

    public NavStoreDataObject(List<NavResponse> navs, int mfId) {
        this.navs = navs;
        this.mfId = mfId;
    }

    public List<NavResponse> getNavs() {
        return navs;
    }

    public void setNavs(List<NavResponse> navs) {
        this.navs = navs;
    }

    public int getMfId() {
        return mfId;
    }

    public void setMfId(int mfId) {
        this.mfId = mfId;
    }
}