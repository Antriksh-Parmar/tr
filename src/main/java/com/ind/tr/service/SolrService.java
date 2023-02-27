package com.ind.tr.service;

import com.ind.tr.controller.model.search.MutualFund;
import com.ind.tr.controller.model.search.SearchResponse;
import com.ind.tr.controller.model.search.Status;
import com.ind.tr.repository.SolrServiceDao;
import com.ind.tr.repository.model.MfSolrIndexReadEntity;
import com.ind.tr.service.model.MfSolrSearchIndexDocument;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SolrService {

    //TODO - need to update query analyzers and improve the search result
    //TODO - consider using extended disMex parser

    private final String collectionName = "mf_search";
    @Autowired
    private SolrClient solrClient;
    @Autowired
    private SolrServiceDao solrServiceDao;

    public void refreshSolrIndexes() {
        List<MfSolrSearchIndexDocument> mfSolrIndexReadDaos = solrServiceDao.querySolrMfMetadata().stream().map(this::buildSolrDocument).toList();
        List<SolrInputDocument> documents = new ArrayList<>();

        for (MfSolrSearchIndexDocument solrIndexRead : mfSolrIndexReadDaos) {
            SolrInputDocument document = new SolrInputDocument();
            document.addField("mfid", solrIndexRead.getMfid());
            document.addField("name", solrIndexRead.getName());
            documents.add(document);
        }
        try {
            solrClient.add(collectionName, documents);
            solrClient.commit(collectionName);
        } catch (SolrServerException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public SearchResponse getRecommendations(String key) {
        SolrQuery query = new SolrQuery();
        query.setFields("name", "mfid");
        query.setQuery("name: \"" + key + "\"~5");
        query.setRows(10);

        try {
            QueryResponse response = solrClient.query(collectionName, query);
            SolrDocumentList solrDocuments = response.getResults();
            List<MfSolrSearchIndexDocument> mfSolrIndexReadDaos = new ArrayList<>();
            for (SolrDocument document : solrDocuments) {
                MfSolrSearchIndexDocument mfSolrIndexReadDao = new MfSolrSearchIndexDocument(document.getFieldValue("mfid").toString(), document.getFieldValue("name").toString());
                mfSolrIndexReadDaos.add(mfSolrIndexReadDao);
            }
            return buildResponse(mfSolrIndexReadDaos);
        } catch (SolrServerException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private MfSolrSearchIndexDocument buildSolrDocument(MfSolrIndexReadEntity mfSolrIndexReadEntity) {
        return new MfSolrSearchIndexDocument(mfSolrIndexReadEntity.get_id().toString(), mfSolrIndexReadEntity.getName());
    }

    private SearchResponse buildResponse(List<MfSolrSearchIndexDocument> mfSolrIndexReadDaos) {
        return new SearchResponse(Status.SUCCESS, mfSolrIndexReadDaos.stream().map(mf -> new MutualFund(mf.getMfid(), mf.getName())).collect(Collectors.toList()));
    }
}
