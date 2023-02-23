package com.ind.tr.persistance;

import com.ind.tr.persistance.model.MfSolrIndexReadDao;

import java.util.List;

public interface SolrServiceDao {
    List<MfSolrIndexReadDao> querySolrMfMetadata();
}
