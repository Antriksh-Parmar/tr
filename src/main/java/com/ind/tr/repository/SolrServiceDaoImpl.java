package com.ind.tr.repository;

import com.ind.tr.repository.model.MfSolrIndexReadDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SolrServiceDaoImpl implements SolrServiceDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<MfSolrIndexReadDao> querySolrMfMetadata() {
        Query query = new Query();
        query.fields().include("fund_name").include("_id");
        return mongoTemplate.find(query, MfSolrIndexReadDao.class, "mutual_funds");
    }
}
