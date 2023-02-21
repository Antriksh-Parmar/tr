package com.ind.tr.persistance;

import com.ind.tr.persistance.model.MfSolrIndexRead;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class MfQueryServiceImpl implements MfQueryService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<MfSolrIndexRead> querySolrMfMetadata() {
        Query query = new Query();
        query.fields().include("id").include("fund_name");
        return mongoTemplate.find(query, MfSolrIndexRead.class, "mutual_funds");
    }
}
