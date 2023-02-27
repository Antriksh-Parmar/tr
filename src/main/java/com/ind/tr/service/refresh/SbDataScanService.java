package com.ind.tr.service.refresh;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Stores scriptbox data into Json
 */
@Component
public class SbDataScanService {

    private final Map<String, Object> requestBody = new HashMap<>();
    private final HttpHeaders headers = new HttpHeaders();

    // TODO Inject this using spring
    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private MongoTemplate mongoTemplate;

    public void exploreAPI() {
        int count = 0;
        requestBody.put("q", "");
        requestBody.put("skip_filter", true);
        requestBody.put("sort_by", "tag_priority");
        int batchSize = 100;
        requestBody.put("size", batchSize);
        headers.setContentType(MediaType.APPLICATION_JSON);

        int maxEntries = 6100;
        while (count < maxEntries) {
            requestBody.put("from", count);
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

            String apiUrl = "https://api.scripbox.com/instruments/search";
            ObjectNode response = restTemplate.postForObject(apiUrl, requestEntity, ObjectNode.class);
            List<Document> documents = readJsonFile(response).stream().map(d -> Document.parse(d.toString())).toList();
            for (Document document: documents) {
                mongoTemplate.save(document, "mutual_funds_v2");
            }
            count += batchSize;
        }
    }

    private List<ObjectNode> readJsonFile(ObjectNode root) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<ObjectNode> nodes = new ArrayList<>();
        try {
            ArrayNode sourceArray = (ArrayNode) root.get("results");

            for (JsonNode node : sourceArray) {
                ObjectNode sourceNode = (ObjectNode) node.get("_source");

                String assetAllocationString = sourceNode.get("asset_allocation").asText();
                ObjectNode assetAllocationNode = objectMapper.readValue(assetAllocationString, ObjectNode.class);
                sourceNode.set("asset_allocation", assetAllocationNode);

                String castorFactsheet = sourceNode.get("castor_factsheet").asText();
                ArrayNode castor_factsheetNode = objectMapper.readValue(castorFactsheet, ArrayNode.class);
                JsonNode cfnode = castor_factsheetNode.get(11);
                String hString = cfnode.get("holdings").asText();
                String sString = cfnode.get("sectors").asText();
                if(hString.isBlank() || hString.isEmpty()) hString = "[]";
                if(sString.isBlank() || sString.isEmpty()) sString = "[]";
                ArrayNode holdingArray = objectMapper.readValue(hString, ArrayNode.class);
                ArrayNode sectorsArray = objectMapper.readValue(sString, ArrayNode.class);
                sourceNode.set("holdings_v4", holdingArray);
                sourceNode.set("sectors_v4", sectorsArray);
                sourceNode.set("castor_factsheet", castor_factsheetNode);

                String sectors = sourceNode.get("sectors").asText();
                if (sectors.isEmpty()) {
                    sourceNode.set("sectors", objectMapper.createArrayNode());
                } else {
                    ArrayNode sectorsNode = objectMapper.readValue(sectors, ArrayNode.class);
                    sourceNode.set("sectors", sectorsNode);
                }

                String holdingString = sourceNode.get("holdings").asText();
                if (holdingString.isEmpty() || holdingString.equals("{}") || holdingString.equals("\"unavailable\"") || holdingString.equals("\"\"")) {
                    sourceNode.set("holdings", objectMapper.createObjectNode());
                } else {
                    ObjectNode holdingNode = objectMapper.readValue(holdingString, ObjectNode.class);
                    sourceNode.set("holdings", holdingNode);
                }


                String sections_v2 = sourceNode.get("sections_v2").asText();
                if (sections_v2.isEmpty()) {
                    sourceNode.set("sections_v2", objectMapper.createArrayNode());
                } else {
                    ArrayNode sections_v2Node = objectMapper.readValue(sections_v2, ArrayNode.class);
                    sourceNode.set("sections_v2", sections_v2Node);
                }


                String sections_v1 = sourceNode.get("sections_v1").asText();
                if (sections_v1.isEmpty()) {
                    sourceNode.set("sections_v1", objectMapper.createArrayNode());
                } else {
                    ArrayNode sections_v1Node = objectMapper.readValue(sections_v1, ArrayNode.class);
                    sourceNode.set("sections_v1", sections_v1Node);
                }

                String sections = sourceNode.get("sections").asText();
                if (sections.isEmpty() || sections.equals("{}") || sections.equals("\"unavailable\"") || sections.equals("\"\"")) {
                    sourceNode.set("sections", objectMapper.createArrayNode());
                } else {
                    ArrayNode sectionsNode = objectMapper.readValue(sections, ArrayNode.class);
                    sourceNode.set("sections", sectionsNode);
                }



                nodes.add(sourceNode);
            }
            return nodes;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
