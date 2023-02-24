package com.ind.tr.repository;

import com.ind.tr.repository.model.utill.NavQuery;
import com.ind.tr.repository.model.utill.NavStoreDataObject;
import com.mongodb.BasicDBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RefreshMfDataDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    private Query query = new Query();

    public List<NavQuery> getInceptionDates() {
        query.fields().include("id").include("inception_date");
        List<BasicDBObject> objects = mongoTemplate.find(query, BasicDBObject.class, "mutual_funds");
        return objects.stream().map(obj -> new NavQuery(obj.getInt("id"), obj.getString("inception_date"))).collect(Collectors.toList());
    }

    public void saveNavs(NavStoreDataObject responses) {
        mongoTemplate.save(responses, "mf_navs");
    }

}
