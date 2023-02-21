package com.ind.tr.persistance;

import com.ind.tr.persistance.model.MfSolrIndexRead;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface MfQueryService {

    List<MfSolrIndexRead> querySolrMfMetadata();
}
