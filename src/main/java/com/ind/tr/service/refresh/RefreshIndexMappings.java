package com.ind.tr.service.refresh;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RefreshIndexMappings {

    protected final ObjectMapper mapper = new ObjectMapper();
    protected final JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSourceBuilder
            .create()
            .url("jdbc:postgresql://localhost:5432/tr")
            .username("trapp")
            .password("amdavad123")
            .driverClassName("org.postgresql.Driver")
            .build());

    public static void main(String[] args) {
        new RefreshIndexMappings().refreshMapping();
    }

    private void refreshMapping() {
        File file = new File("/Users/antriksh/tr/tr/src/main/resources/indexmap.json");
        try {
            ArrayNode array = (ArrayNode) mapper.readTree(file);
            String query = "INSERT INTO fi.index_map (ms_name, rv_code, rv_name) VALUES (?, ?, ?)";
            jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    JsonNode node = array.get(i);
                    ps.setString(1, node.get("Morningstar").asText());
                    ps.setInt(2, node.get("RupeevestId").asInt());
                    ps.setString(3, node.get("RupeeVest Name").asText());
                }

                @Override
                public int getBatchSize() {
                    return array.size();
                }
            });
            System.out.println(array);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
