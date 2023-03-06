package com.ind.tr.service.refresh;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.ResponseBody;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class RefreshNavService {

    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSourceBuilder
            .create()
            .url("jdbc:postgresql://localhost:5432/tr")
            .username("trapp")
            .password("amdavad123")
            .driverClassName("org.postgresql.Driver")
            .build());
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    List<String> missedId = new ArrayList<>();

    public static void main(String[] args) {
        new RefreshNavService().refreshNavs();
    }

    private void refreshNavs() {
        List<KvKeys> keys = queryEligibleMsIds();
        int count = 1;
        for (KvKeys key : keys) {
            System.out.println("Processing - " + count);
            ArrayNode navs = getNavs(key.kv_id);
            if(navs == null) continue;
            saveNavs(navs, key);
            count++;
        }
        System.out.println(missedId);
    }

    private void saveNavs(ArrayNode navs, KvKeys key) {
        String query = "INSERT INTO fi.mutual_fund_navs (mf_id, isin, nav, nav_date) " +
                "VALUES(?,?,?,?)";
        jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, key.mf_id);
                ps.setString(2, key.isin);
                ps.setBigDecimal(3, getBigDecimal(navs.get(i).get(1)));
                ps.setDate(4, getDate(navs.get(i).get(0).asLong()));
            }

            @Override
            public int getBatchSize() {
                return navs.size();
            }
        });
    }

    private Date getDate(long unixTs) {
        java.util.Date date = new java.util.Date(unixTs * 1000L);
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        return sqlDate;
    }

    private ArrayNode getNavs(String key) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String url = String.format("https://api.kuvera.in/mf/api/v6/fund_navs/%s.json?v=1.211.0", key);
        try {
            ResponseBody response = client.newCall(new Request.Builder().url(url).method("GET", null).build()).execute().body();
            return mapper.readValue(response.string(), ArrayNode.class);
        } catch (IOException e) {
            missedId.add(key);
            return null;
        }

    }


    private List<KvKeys> queryEligibleMsIds() {
        String query = "SELECT kv_id, mf_id, isin from fi.key_mappings WHERE kv_id IS NOT NULL";
        return jdbcTemplate.query(query, (rs, rowNum) -> new KvKeys(rs.getString("isin"), rs.getString("mf_id"), rs.getString("kv_id")));
    }

    private BigDecimal getBigDecimal(JsonNode node) {
        if (node == null || "null".equals(node.asText())) return BigDecimal.valueOf(-1);
        return new BigDecimal(getStringValue(node));
    }

    private String getStringValue(JsonNode node) {
        return getValue(node, JsonNode::asText);
    }

    private <T> T getValue(JsonNode node, Function<JsonNode, T> converter) {
        if (node == null) {
            return null;
        } else {
            return converter.apply(node);
        }
    }

    class KvKeys {
        private final String isin;
        private final String mf_id;
        private final String kv_id;

        public KvKeys(String isin, String mf_id, String kv_id) {
            this.isin = isin;
            this.mf_id = mf_id;
            this.kv_id = kv_id;
        }

        public String getIsin() {
            return isin;
        }

        public String getMf_id() {
            return mf_id;
        }

        public String getKv_id() {
            return kv_id;
        }
    }
}
