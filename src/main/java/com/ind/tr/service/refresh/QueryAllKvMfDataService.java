package com.ind.tr.service.refresh;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.ResponseBody;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.function.Function;

public class QueryAllKvMfDataService {

    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSourceBuilder
            .create()
            .url("jdbc:postgresql://localhost:5432/tr")
            .username("trapp")
            .password("amdavad123")
            .driverClassName("org.postgresql.Driver")
            .build());
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        new QueryAllKvMfDataService().queryAllMfData();
    }

    private void queryAllMfData() {
        String growthFundsURL = "https://api.kuvera.in/insight/api/v1/mutual_fund_search.json?limit=10000&scheme_plan=GROWTH";
        String divFundURL = "https://api.kuvera.in/insight/api/v1/mutual_fund_search.json?limit=10000&scheme_plan=DIVIDEND";
        ArrayNode growthFunds = (ArrayNode) queryFunds(growthFundsURL).get("data").get("funds");
        ArrayNode divFunds = (ArrayNode) queryFunds(divFundURL).get("data").get("funds");
        populateDatabase(growthFunds);
        populateDatabase(divFunds);
    }

    private void populateDatabase(ArrayNode funds) {
        for(JsonNode fund: funds) {
            System.out.println("Processing ---> " + fund.get("unique_fund_code").asText());
            JsonNode fundDetails = queryFundDetails(fund.get("unique_fund_code").asText());
            jdbcTemplate.update(con -> {
                String query =
                        "UPDATE fi.mutual_funds SET fund_active = ?, fund_name = ?, short_name = ?, fund_manager = ?, inception_date = ?," +
                        " category = ?, sub_category = ?, plan = ?, fund_type = ?, lock_in = ?, detail_info = ?, objective = ?, risk_level = ?, " +
                        "aum = ?, expense_ratio = ?, lump_sum_order_allowed = ?, redemption_allowed = ?, sip_order_allowed = ?, switch_order_allowed = ?, " +
                        "swp_order_allowed = ?, stp_order_allowed = ?, direct = ?, maturity_type = ? WHERE isin = ?";
                PreparedStatement ps = con.prepareStatement(query);
                ps.setBoolean(1, getBooleanValue(fund.get("fund_active")));
                ps.setString(2, getStringValue(fundDetails.get("name")));
                ps.setString(3, getStringValue(fundDetails.get("short_name")));
                ps.setString(4, getStringValue(fundDetails.get("fund_manager")));
                ps.setDate(5, Date.valueOf(getStringValue(fundDetails.get("start_date"))));
                ps.setString(6, getStringValue(fundDetails.get("category")));
                ps.setString(7, getStringValue(fundDetails.get("fund_category")));
                ps.setString(8, getStringValue(fundDetails.get("plan")));
                ps.setString(9, getStringValue(fundDetails.get("fund_type")));
                ps.setInt(10, getIntValue(fundDetails.get("lock_in_period")));
                ps.setString(11, getStringValue(fundDetails.get("detail_info")));
                ps.setString(12, getStringValue(fundDetails.get("investment_objective")));
                ps.setString(13, getStringValue(fundDetails.get("crisil_rating")));
                ps.setBigDecimal(14, getBigDecimal(fundDetails.get("aum")));
                ps.setBigDecimal(15, getBigDecimal(fundDetails.get("expense_ratio")));
                ps.setBoolean(16, "Y".equals(getStringValue(fundDetails.get("lump_available"))));
                ps.setBoolean(17, "Y".equals(getStringValue(fundDetails.get("redemption_allowed"))));
                ps.setBoolean(18, "Y".equals(getStringValue(fundDetails.get("sip_available"))));
                ps.setBoolean(19, "Y".equals(getStringValue(fundDetails.get("switch_allowed"))));
                ps.setBoolean(20, "Y".equals(getStringValue(fundDetails.get("swp_flag"))));
                ps.setBoolean(21, "Y".equals(getStringValue(fundDetails.get("stp_flag"))));
                ps.setBoolean(22, "Y".equals(getStringValue(fundDetails.get("direct"))));
                ps.setString(23, getStringValue(fundDetails.get("maturity_type")));
                ps.setString(24, getStringValue(fundDetails.get("ISIN")));
                return ps;
            });

            String query = "UPDATE fi.key_mappings SET kv_id = '" + getStringValue(fundDetails.get("code")) + "' WHERE isin = '" + getStringValue(fundDetails.get("ISIN")) + "'";
            jdbcTemplate.update(query);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private JsonNode queryFundDetails(String fundId) {
        String url = String.format("https://api.kuvera.in/mf/api/v5/fund_schemes/%s.json?v=1.212.6", fundId);
        try {
            ResponseBody response = client.newCall(new Request.Builder().url(url).method("GET", null).build()).execute().body();
            return mapper.readValue(response.string(), ArrayNode.class).get(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ObjectNode queryFunds(String url) {
        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .build();
        try {
            ResponseBody response = client.newCall(request).execute().body();
            return mapper.readValue(response.string(), ObjectNode.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private BigDecimal getBigDecimal(JsonNode node) {
        if(node == null  || "null".equals(node.asText())) return BigDecimal.valueOf(-1);
        return new BigDecimal(getStringValue(node));
    }

    private boolean getBooleanValue(JsonNode node) {
        return getValue(node, JsonNode::asBoolean);
    }

    private String getStringValue(JsonNode node) {
        return getValue(node, JsonNode::asText);
    }

    private int getIntValue(JsonNode node) {
        if(node == null) return -1;
        return getValue(node, JsonNode::asInt);
    }

    private <T> T getValue(JsonNode node, Function<JsonNode, T> converter) {
        if (node == null) {
            return null;
        } else {
            return converter.apply(node);
        }
    }

}
