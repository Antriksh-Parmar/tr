package com.ind.tr.service;

import com.ind.tr.controller.model.MutualFund;
import com.ind.tr.controller.model.SearchRecommendationResponse;
import com.ind.tr.controller.model.Status;
import com.ind.tr.persistance.MfQueryService;
import com.ind.tr.persistance.model.MfSolrIndexRead;
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

    @Autowired
    private SolrClient solrClient;

    @Autowired
    private MfQueryService mfQueryService;

    public void refreshSolrIndexes() {
        List<MfSolrIndexRead> mfSolrIndexReads = mfQueryService.querySolrMfMetadata();
        List<SolrInputDocument> documents = new ArrayList<>();

        for (MfSolrIndexRead solrIndexRead : mfSolrIndexReads) {
            SolrInputDocument document = new SolrInputDocument();
            document.addField("id", solrIndexRead.getId());
            document.addField("name", solrIndexRead.getName());
            documents.add(document);
        }
        try {
            solrClient.add(documents);
            solrClient.commit();
        } catch (SolrServerException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public SearchRecommendationResponse getRecommendations(String key) {
        SolrQuery query = new SolrQuery();
        query.add(key);
        query.set("defType", "edismax");
        query.setRows(10);
        try {
            QueryResponse response = solrClient.query(query);
            SolrDocumentList solrDocuments = response.getResults();
            List<MfSolrIndexRead> mfSolrIndexReads = new ArrayList<>();
            for (SolrDocument document : solrDocuments) {
                MfSolrIndexRead mfSolrIndexRead = new MfSolrIndexRead(Integer.parseInt(document.getFieldValue("id").toString()), document.getFieldValue("name").toString());
                mfSolrIndexReads.add(mfSolrIndexRead);
            }
            return buildResponse(mfSolrIndexReads);
        } catch (SolrServerException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private SearchRecommendationResponse buildResponse(List<MfSolrIndexRead> mfSolrIndexReads) {
        return new SearchRecommendationResponse(Status.SUCCESS, mfSolrIndexReads.stream().map(mf -> new MutualFund(mf.getId(), mf.getName())).collect(Collectors.toList()));
    }
}
