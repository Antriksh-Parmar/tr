package com.ind.tr.repository;

import com.ind.tr.repository.model.MfSolrIndexReadEntity;

import java.util.List;

public interface SolrServiceDao {
    List<MfSolrIndexReadEntity> querySolrMfMetadata();
}
