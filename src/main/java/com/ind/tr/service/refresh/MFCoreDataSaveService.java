package com.ind.tr.service.refresh;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Transfers scriptbox data from Json to MongoDb with some additional processing.
 */
@Component
public class MFCoreDataSaveService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void refresh() {
        for (int i = 0; i <= 6000; i = i + 100) {
            String filePath = "/Users/antriksh/tr/tr/mf_scriptbox_";
            String file = filePath + i + "_.json";
            List<Document> documents = readJsonFile(file).stream().map(d -> Document.parse(d.toString())).toList();
            for (Document document: documents) {
                mongoTemplate.save(document, "mutual_funds");
            }
            System.out.println("Saved----->>> "+file);
        }
    }


    public List<ObjectNode> readJsonFile(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<ObjectNode> nodes = new ArrayList<>();
        File file = new File(filePath);
        try {
            ObjectNode root = objectMapper.readValue(file, ObjectNode.class);
            ArrayNode sourceArray = (ArrayNode) root.get("results");

            for (JsonNode node : sourceArray) {
                ObjectNode sourceNode = (ObjectNode) node.get("_source");

                String assetAllocationString = sourceNode.get("asset_allocation").asText();
                ObjectNode assetAllocationNode = objectMapper.readValue(assetAllocationString, ObjectNode.class);
                sourceNode.set("asset_allocation", assetAllocationNode);

                String castorFactsheet = sourceNode.get("castor_factsheet").asText();
                ArrayNode castor_factsheetNode = objectMapper.readValue(castorFactsheet, ArrayNode.class);
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
