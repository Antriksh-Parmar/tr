package com.ind.tr.service.refresh;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class RefreshIndexes {

    protected final ObjectMapper mapper = new ObjectMapper();
    protected final JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSourceBuilder
            .create()
            .url("jdbc:postgresql://localhost:5432/tr")
            .username("trapp")
            .password("amdavad123")
            .driverClassName("org.postgresql.Driver")
            .build());
    private final OkHttpClient client = new OkHttpClient();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private List<String> missedId = new ArrayList<>();

    public static void main(String[] args) {
        new RefreshIndexes().refreshIndexes();
    }

    private void refreshIndexes() {
        ArrayNode indexes = queryIndexNames();
        //saveIndexes(indexes);
        for (JsonNode node : indexes) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            ArrayNode rrs = indexRollingReturns(node.get("indexcode").asText());
            if(rrs == null) continue;
            saveRollingReturns(rrs);
        }
        System.out.println("Missed Ids -> " + missedId);
    }

    private void saveRollingReturns(ArrayNode rrs) {
        String query = "INSERT INTO fi.index_rolling_returns (index_id, rr_date, one_month, three_month, six_month, one_year, three_years, five_years, seven_years, ten_years) VALUES (? ,?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                JsonNode node = rrs.get(i);
                ps.setInt(1, node.get("index_code").asInt());
                ps.setDate(2, getDate(node.get("date")));
                ps.setBigDecimal(3, getBigDecimal(node.get("return_1_months")));
                ps.setBigDecimal(4, getBigDecimal(node.get("return_3_months")));
                ps.setBigDecimal(5, getBigDecimal(node.get("return_6_months")));
                ps.setBigDecimal(6, getBigDecimal(node.get("return_1_years")));
                ps.setBigDecimal(7, getBigDecimal(node.get("return_3_years")));
                ps.setBigDecimal(8, getBigDecimal(node.get("return_5_years")));
                ps.setBigDecimal(9, getBigDecimal(node.get("return_7_years")));
                ps.setBigDecimal(10, getBigDecimal(node.get("return_10_years")));
            }

            @Override
            public int getBatchSize() {
                return rrs.size();
            }
        });
    }

    private BigDecimal getBigDecimal(JsonNode node) {
        if (node == null || "null".equals(node.asText())) return null;
        return new BigDecimal(node.asText());
    }

    private Date getDate(JsonNode node) {
        if (node == null) return null;
        try {
            return new Date(dateFormat.parse(node.asText()).getTime());
        } catch (ParseException e) {
            System.out.println("Error parsing date - " + e.getMessage());
            return null;
        }
    }

    private void saveIndexes(ArrayNode indexes) {
        String query = "INSERT INTO fi.indexes (index_key, name) VALUES (? ,?)";
        jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, indexes.get(i).get("indexcode").asInt());
                ps.setString(2, indexes.get(i).get("index_name").asText());
            }

            @Override
            public int getBatchSize() {
                return indexes.size();
            }
        });
    }

    private ArrayNode indexRollingReturns(String key) {
        String url = "https://www.rupeevest.com/home/return_rolling?benchmark=1&schemecode%5B%5D=3461&schemecode%5B%5D=" + key + "&return_year=1_mon&from_date=2010-01-01&end_date=2023-03-09";
        //String url = "https://www.rupeevest.com/home/return_rolling?benchmark=1&schemecode%5B%5D=3461&schemecode%5B%5D=24&return_year=5_yr&from_date=2000-01-01&end_date=2023-03-09";
        Request request = new Request.Builder()
                .url(url)
                .method("GET", null).build();

        try {
            return (ArrayNode) mapper.readValue(client.newCall(request).execute().body().string(), ObjectNode.class).get("sql6");
        } catch (IOException e) {
            missedId.add(key);
            e.printStackTrace();
            return null;
        }
    }


    private ArrayNode queryIndexNames() {
        String url = "https://www.rupeevest.com/home/get_landing_index";
        Request request = new Request.Builder()
                .url(url)
                .method("GET", null).build();

        try {
            return (ArrayNode) mapper.readValue(client.newCall(request).execute().body().string(), ObjectNode.class).get("index");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}
