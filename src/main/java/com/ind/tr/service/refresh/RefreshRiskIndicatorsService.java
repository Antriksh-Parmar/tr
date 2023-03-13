package com.ind.tr.service.refresh;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RefreshRiskIndicatorsService {

    private final QueryFundKeysService keysService = new QueryFundKeysService();
    private final List<String> missedIds = new ArrayList<>();
    private final Map<String, String> categories = Map.of("indexRiskVolatility", "index", "fundRiskVolatility", "fund", "categoryRiskVolatility", "category");
    private final Map<String, Integer> durations = Map.of(
            "for1Year", 1,
            "for3Year", 3,
            "for5Year", 5,
            "for10Year", 10,
            "for15Year", 15);

    public static void main(String[] args) {
        new RefreshRiskIndicatorsService().refreshRiskIndicators();
    }

    private void refreshRiskIndicators() {
        List<MsKeys> keys = keysService.queryEligibleMsIds();
        int count = 0;
        for (MsKeys key : keys) {
            System.out.println("Processing " + count);
            ObjectNode root = getRiskIndicators(key.getMsId());
            if (root == null) continue;
            List<RiskIndicators> indicators = buildObjects(root, key);
            saveIndicators(indicators);
            count++;
        }
        System.out.println(missedIds);
    }

    private void saveIndicators(List<RiskIndicators> indicators) {
        String query = "INSERT INTO fi.mutual_fund_risk_parameters (mf_id, asset_category, duration, alpha, beta, rSquared, standardDeviation, sharpeRatio) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        keysService.jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                RiskIndicators ind = indicators.get(i);
                ps.setString(1, ind.mfId);
                ps.setString(2, ind.assetClassType);
                ps.setInt(3, ind.duration);
                ps.setBigDecimal(4, ind.alpha);
                ps.setBigDecimal(5, ind.beta);
                ps.setBigDecimal(6, ind.rSquared);
                ps.setBigDecimal(7, ind.standardDeviation);
                ps.setBigDecimal(8, ind.sharpeRatio);
            }

            @Override
            public int getBatchSize() {
                return indicators.size();
            }
        });
    }

    private List<RiskIndicators> buildObjects(ObjectNode root, MsKeys keys) {
        List<RiskIndicators> indicators = new ArrayList<>();
        for (String category : categories.keySet()) {
            JsonNode node = root.get(category);
            if (node == null) continue;
            indicators.addAll(buildIndicators(node, category, keys));
        }
        return indicators;
    }


    private List<RiskIndicators> buildIndicators(JsonNode node, String category, MsKeys msKeys) {
        List<RiskIndicators> indicators = new ArrayList<>();
        for (String duration : durations.keySet()) {
            JsonNode node1 = node.get(duration);
            if (node1 == null) continue;
            indicators.add(buildIndicator(node1, category, durations.get(duration), msKeys));
        }
        return indicators;
    }

    private RiskIndicators buildIndicator(JsonNode node, String category, int duration, MsKeys msKeys) {

        return new RiskIndicators(
                msKeys.getUuid(),
                category,
                duration,
                keysService.getBigDecimal(node.get("alpha")),
                keysService.getBigDecimal(node.get("beta")),
                keysService.getBigDecimal(node.get("rSquared")),
                keysService.getBigDecimal(node.get("standardDeviation")),
                keysService.getBigDecimal(node.get("sharpeRatio")));
    }

    private ObjectNode getRiskIndicators(String key) {
        String url = String.format("https://api-global.morningstar.com/sal-service/v1/fund/performance/riskVolatility/%s/data?currency=&longestTenure=false&languageId=en&locale=en&clientId=RSIN_SAL&benchmarkId=mstarorcat&component=sal-components-mip-risk-volatility-measures&version=3.74.0&access_token=TeV7cawAplez16e377u5EFn2l1Iy", key);
        OkHttpClient client = new OkHttpClient();
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
                .addHeader("x-api-realtime-e", "ew0KICAiYWxnIjogIlJTQS1PQUVQIiwNCiAgImVuYyI6ICJBMTI4R0NNIg0KfQ.Imp5dQ3C-jr1-wqpjbo370wxFYXgkx2Iu1nCe85UeS163-Pc_CGxSlK6kjF4tP_v8ywHNvW370V13NQptKypr3UUB-0cTj98C7uhFcTTdWaiUv1-NqBUhIB39c1lhHZ8-a_dJvz3trSiotJAjPwxFK-MOYlXY9OVxIEoOsUMLv4.SfLIJ_zWpdmsS6dd.4iTgr8g3LXqCO4J5rw0Ueqvsedn33eirl2VVilpZNAKCZnzz2V0t9pJLllUiLzxUw8UQnLOtNJ0eOMDz0E4cPmxL73uiwCjdGITqIieIaFkyk0i45fmeZvSFf10QYHsA9KzC-TPiVkEFU7NZYWG7nzsSTYJUxC_tuJRKrOBrGDzW5Cb5Qd5_zP9odk4KxsEXXPeJz0__BuPYHUBOYOdIAi68CFdFUcMPdDXemySHRHdfUIA3uVbdtcszkowUGjb9fUBbCpPu.X5eLBQOnthZ8WQxHulHKkw")
                .addHeader("x-api-requestid", "e8727d28-620e-2751-8a3d-4bf78398a07b")
                .addHeader("x-sal-contenttype", "nNsGdN3REOnPMlKDShOYjlk6VYiEVLSdpfpXAm7o2Tk=")
                .build();
        try {
            return keysService.mapper.readValue(client.newCall(request).execute().body().string(), ObjectNode.class);
        } catch (IOException e) {
            missedIds.add(key);
            return null;
        }

    }

    class RiskIndicators {
        private final String mfId;
        private final String assetClassType;
        private final int duration;
        private final BigDecimal alpha;
        private final BigDecimal beta;
        private final BigDecimal rSquared;
        private final BigDecimal standardDeviation;
        private final BigDecimal sharpeRatio;

        public RiskIndicators(String mfId, String assetClassType, int duration, BigDecimal alpha, BigDecimal beta, BigDecimal rSquared, BigDecimal standardDeviation, BigDecimal sharpeRatio) {
            this.mfId = mfId;
            this.assetClassType = assetClassType;
            this.duration = duration;
            this.alpha = alpha;
            this.beta = beta;
            this.rSquared = rSquared;
            this.standardDeviation = standardDeviation;
            this.sharpeRatio = sharpeRatio;
        }
    }

}
