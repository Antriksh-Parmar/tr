package com.ind.tr.service.model;

public class MfSolrSearchIndexDocument {
    String mfid;
    String name;

    public MfSolrSearchIndexDocument(String mfid, String name) {
        this.mfid = mfid;
        this.name = name;
    }

    public String getMfid() {
        return mfid;
    }

    public void setMfid(String mfid) {
        this.mfid = mfid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
