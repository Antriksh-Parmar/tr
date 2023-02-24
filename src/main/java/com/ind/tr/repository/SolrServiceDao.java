package com.ind.tr.repository;

import com.ind.tr.repository.model.MfSolrIndexReadDao;

import java.util.List;

public interface SolrServiceDao {
    List<MfSolrIndexReadDao> querySolrMfMetadata();
}
