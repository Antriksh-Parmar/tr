package com.ind.tr.service.refresh;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.function.Function;


// TODO NEED TO ALTER MATURITY_TYPE IN MUTUAL_FUNDS TABLE, MAKE IT VARCHAR  FROM BOOL
public class QueryIndexMsDataService {

    private final ObjectMapper mapper = new ObjectMapper();
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSourceBuilder
            .create()
            .url("jdbc:postgresql://localhost:5432/tr")
            .username("trapp")
            .password("amdavad123")
            .driverClassName("org.postgresql.Driver")
            .build());


    public static void main(String[] args) {
        new QueryIndexMsDataService().populateIndexData();
    }

    private void populateIndexData() {
        List<MsKeys> keys = queryEligibleMsIds();
        String updateQuery = "UPDATE fi.mutual_funds SET index = ?, index_new = ?, benchmark = ?, portfolio_turnover = ? WHERE id = ?";
        for (MsKeys key : keys) {
            ObjectNode node = queryIndexes(key.getMsId());

            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(updateQuery);
                ps.setString(1, getStringValue(node.get("index")));
                ps.setString(2, getStringValue(node.get("primaryIndexNameNew")));
                ps.setString(3, getStringValue(node.get("prospectusBenchmarkName")));
                ps.setBigDecimal(4, getBigDecimal(node.get("lastTurnoverRatio")));
                ps.setString(5, key.getUuid());
                return ps;
            });

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private List<MsKeys> queryEligibleMsIds() {
        String query = "SELECT ms_id, mf_id from fi.key_mappings WHERE kv_id IS NOT NULL";
        return jdbcTemplate.query(query, (rs, rowNum) -> new MsKeys(rs.getString("ms_id"), rs.getString("mf_id")));
    }

    private ObjectNode queryIndexes(String msId) {
        OkHttpClient client = new OkHttpClient();
        String url = String.format("https://api-global.morningstar.com/sal-service/v1/fund/quote/v3/%s/data?fundServCode=&showAnalystRatingChinaFund=false&showAnalystRating=false&languageId=en&locale=en&clientId=RSIN_SAL&benchmarkId=mstarorcat&component=sal-components-mip-quote&version=3.74.0&access_token=KjTY3bEPuusw3zrTnPuld4DNIMHB", msId);
        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .addHeader("authority", "api-global.morningstar.com")
                .addHeader("accept", "*/*")
                .addHeader("accept-language", "en-US,en;q=0.9,hi-IN;q=0.8,hi;q=0.7,mt;q=0.6")
                .addHeader("origin", "https://www.morningstar.in")
                .addHeader("referer", "https://www.morningstar.in/")
                .addHeader("sec-ch-ua", "\"Chromium\";v=\"110\", \"Not A(Brand\";v=\"24\", \"Google Chrome\";v=\"110\"")
                .addHeader("sec-ch-ua-mobile", "?0")
                .addHeader("sec-ch-ua-platform", "\"macOS\"")
                .addHeader("sec-fetch-dest", "empty")
                .addHeader("sec-fetch-mode", "cors")
                .addHeader("sec-fetch-site", "cross-site")
                .addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36")
                .addHeader("x-api-realtime-e", "ew0KICAiYWxnIjogIlJTQS1PQUVQIiwNCiAgImVuYyI6ICJBMTI4R0NNIg0KfQ.S-INBwd7_8XqrGgtqAxMrugdpA9oBlTzaXVcOc2ApsUxCsdFmi5TN_PBJWxQ3WHeRgIeouhlGd68iO1EBUHeMrpDwHBBde677dWnAq3aZIhkwf-CatSb4HHAy0rxY1T2HWDallK_NHTbNaKujBUU_t0HfxO0jtA2vpIrM9qNCbU.n6IGbml8fn4RrT38.En8nKjBIrT1ZaYRv0MWhb-_FKl03XDuRauxyEBA_BmtMqZxr_oLwsBriJH0QTQEp5Vq_6IfrUopni90lMLnC77EpKFAGSYjbFQV7tgSNkqpyuhhG9fb6u4AT9RlyZVyuYwfO8YPkzBjsaThmawRLoSuMysURnMfnrS25e8GBMCKF7jEvFRsJ9tgbj3_6_DERNmEGyuJTf92lEmnoTBXQeXCZPnYbR3sSOgvaYfAh2XzfuOBQhgju4Gq4nFTxfBR7JszJYMm8.IBT8En9lAff4-Ie_7tq2Ag")
                .addHeader("x-api-requestid", "6333e1b0-23eb-e53a-2f73-b46e154e50da")
                .addHeader("x-sal-contenttype", "nNsGdN3REOnPMlKDShOYjlk6VYiEVLSdpfpXAm7o2Tk=")
                .build();
        try {
            return mapper.readValue(client.newCall(request).execute().body().string(), ObjectNode.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
}