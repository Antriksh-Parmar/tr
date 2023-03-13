package com.ind.tr.service.refresh;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.function.Function;

public class QueryFundKeysService {

    protected final ObjectMapper mapper = new ObjectMapper();
    protected final JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSourceBuilder
            .create()
            .url("jdbc:postgresql://localhost:5432/tr")
            .username("trapp")
            .password("amdavad123")
            .driverClassName("org.postgresql.Driver")
            .build());

    public List<MsKeys> queryEligibleMsIds() {
        String query = "SELECT ms_id, mf_id from fi.key_mappings WHERE kv_id IS NOT NULL limit 1521";
        return jdbcTemplate.query(query, (rs, rowNum) -> new MsKeys(rs.getString("ms_id"), rs.getString("mf_id")));
    }

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

    public BigDecimal getBigDecimal(JsonNode node) {
        if (node == null || "null".equals(node.asText())) return BigDecimal.valueOf(-1);
        return new BigDecimal(getStringValue(node));
    }

    public String getStringValue(JsonNode node) {
        return getValue(node, JsonNode::asText);
    }

    private <T> T getValue(JsonNode node, Function<JsonNode, T> converter) {
        if (node == null) {
            return null;
        } else {
            return converter.apply(node);
        }
    }

    public Date getDate(JsonNode node) {
        if(node == null) return null;
        try {
            return new Date(dateFormat.parse(node.asText()).getTime());
        } catch (ParseException e) {
            System.out.println("Error parsing date - " + e.getMessage());
            return null;
        }
    }
}

class MsKeys {
    private final String msId;
    private final String uuid;

    public MsKeys(String msId, String uuid) {
        this.msId = msId;
        this.uuid = uuid;
    }

    public String getMsId() {
        return msId;
    }

    public String getUuid() {
        return uuid;
    }
}
