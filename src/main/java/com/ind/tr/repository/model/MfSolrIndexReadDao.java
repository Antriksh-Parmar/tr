package com.ind.tr.repository.model;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("mutual_funds")
public class MfSolrIndexReadDao {

    @Field("_id")
    ObjectId _id;

    @Field("fund_name")
    String name;

    public MfSolrIndexReadDao(ObjectId _id, String name) {
        this._id = _id;
        this.name = name;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
