package com.ind.tr.persistance.model;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("mutual_funds")
public class MfSolrIndexRead {

    @Field("id")
    int id;

    @Field("fund_name")
    String name;

    public MfSolrIndexRead(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
