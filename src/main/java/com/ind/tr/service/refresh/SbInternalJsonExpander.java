package com.ind.tr.service.refresh;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class SbInternalJsonExpander {

    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private MongoTemplate mongoTemplate;

    public void expandFields() {

        Query query = new Query();
        query.fields().include("id").include("castor_factsheet").include("sections_v2");
        List<String> docs = mongoTemplate.find(query, String.class, "mutual_funds");

        docs.forEach(doc -> {
            try {
                ObjectNode root = mapper.readValue(doc, ObjectNode.class);
                JsonNode node = root.get("castor_factsheet").get(11);
                ArrayNode sectionsV2 = (ArrayNode) root.get("sections_v2");

                String hString = node.get("holdings").asText();
                String sString = node.get("sectors").asText();
                if(hString.isBlank() || hString.isEmpty()) hString = "[]";
                if(sString.isBlank() || sString.isEmpty()) sString = "[]";
                ArrayNode holdingArray = mapper.readValue(hString, ArrayNode.class);
                ArrayNode sectorsArray = mapper.readValue(sString, ArrayNode.class);

                Criteria criteria = Criteria.where("id").is(root.get("id").intValue());
                Update update = new Update();
                update.set("holdings_v3", buildHoldingsArray(holdingArray));
                update.set("sectors_v3", buildSectorsArray(sectorsArray));
                //update.set("exit_loads", exitLoads);
                mongoTemplate.updateFirst(Query.query(criteria), update, "mutual_funds");
            } catch (JsonProcessingException e) {
                System.out.println("Failed for --> " + doc);
                throw new RuntimeException(e);
            }
        });
        System.out.println(docs);
    }

    private List<Holding> buildHoldingsArray(ArrayNode holdingArray) {
        List<Holding> holdings = new ArrayList<>();
        if(holdingArray.size() < 1) return holdings;
        for (JsonNode node : holdingArray) {
            holdings.add(new Holding((node.get("name") != null)? node.get("name").asText(): null, getBdValue(node.get("weight")), (node.get("holdingtype") != null)? node.get("holdingtype").asText(): null));
        }
        return holdings;
    }

    private BigDecimal getBdValue(JsonNode node) {
        if(node == null) return null;
        else if(node.asText().isBlank() || node.asText().isEmpty()) return null;
        else return new BigDecimal(node.asText());
    }

    private List<Sector> buildSectorsArray(ArrayNode holdingArray) {
        List<Sector> sectors = new ArrayList<>();
        if(holdingArray.size() < 1) return sectors;
        for (JsonNode node : holdingArray) {
            sectors.add(new Sector((node.get("name") != null)? node.get("name").asText(): null, getBdValue(node.get("weight")), getBdValue(node.get("value"))));
        }
        return sectors;
    }

    class Sector {
        String name;
        BigDecimal weight;
        BigDecimal value;

        public Sector(String name, BigDecimal weight, BigDecimal value) {
            this.name = name;
            this.weight = weight;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public BigDecimal getWeight() {
            return weight;
        }

        public void setWeight(BigDecimal weight) {
            this.weight = weight;
        }

        public BigDecimal getValue() {
            return value;
        }

        public void setValue(BigDecimal value) {
            this.value = value;
        }
    }


    class Holding {
        String name;
        BigDecimal weight;
        String holdingType;

        public Holding(String name, BigDecimal weight, String holdingType) {
            this.name = name;
            this.weight = weight;
            this.holdingType = holdingType;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public BigDecimal getWeight() {
            return weight;
        }

        public void setWeight(BigDecimal weight) {
            this.weight = weight;
        }

        public String getHoldingType() {
            return holdingType;
        }

        public void setHoldingType(String holdingType) {
            this.holdingType = holdingType;
        }
    }

}
