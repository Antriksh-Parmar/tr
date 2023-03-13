package com.ind.tr.service.refresh;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RefreshPerformanceIndicatorService {

    private final QueryFundKeysService keysService = new QueryFundKeysService();
    private final List<String> missedIds = new ArrayList<>();

    public static void main(String[] args) {
        new RefreshPerformanceIndicatorService().refreshPerfIndicators();
    }

    private void refreshPerfIndicators() {
        List<MsKeys> keys = keysService.queryEligibleMsIds();
        int i = 1;
        for (MsKeys key : keys) {
            System.out.println("Processing " + i);
            ObjectNode root = getPerfIndicators(key.getMsId());
            if (root == null) continue;
            savePerfIndicators(buildObject(root, key));
            i++;
        }
        System.out.println(missedIds);
    }

    private List<PerfIndicators> buildObject(ObjectNode root, MsKeys keys) {
        List<PerfIndicators> result = new ArrayList<>();
        JsonNode node = root.get("measureMap");
        if(node == null) return result;
        result.add(buildObject(node, keys, "fund"));
        result.add(buildObject(node, keys, "index"));
        result.add(buildObject(node, keys, "category"));
        result.removeIf(Objects::isNull);
        return result;
    }

    private PerfIndicators buildObject(JsonNode root, MsKeys keys, String assetType) {
        JsonNode node = root.get(assetType);
        if (node == null) return null;
        return new PerfIndicators(
                keys.getUuid(),
                assetType,
                keysService.getBigDecimal(node.get("upside")),
                keysService.getBigDecimal(node.get("downside")),
                keysService.getBigDecimal(node.get("maxDrawDown")),
                keysService.getDate(node.get("peakDate")),
                keysService.getDate(node.get("valleyDate")),
                keysService.getBigDecimal(node.get("duration"))
        );
    }


    private void savePerfIndicators(List<PerfIndicators> indicators) {
        String query = "INSERT INTO fi.mutual_fund_performance_indicators(mf_id, asset_category, upside, downside, maxDrawDown, peakDate, valleyDate) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?)";
        keysService.jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, indicators.get(i).mf_id);
                ps.setString(2, indicators.get(i).perfAssetType);
                ps.setBigDecimal(3, indicators.get(i).upside);
                ps.setBigDecimal(4, indicators.get(i).downside);
                ps.setBigDecimal(5, indicators.get(i).maxDrawDown);
                ps.setDate(6, indicators.get(i).peak);
                ps.setDate(7, indicators.get(i).valley);
            }

            @Override
            public int getBatchSize() {
                return indicators.size();
            }
        });
    }

    private ObjectNode getPerfIndicators(String key) {
        String url = String.format("https://api-global.morningstar.com/sal-service/v1/fund/performance/marketVolatilityMeasure/%s/data?year=3&currency=&longestTenure=false&languageId=en&locale=en&clientId=RSIN_SAL&benchmarkId=mstarorcat&component=sal-components-mip-market-volatility-measures&version=3.74.0&access_token=TeV7cawAplez16e377u5EFn2l1Iy", key);
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
                .addHeader("x-api-requestid", "1edc09e6-beba-8f48-b8e1-038eaa183e7f")
                .addHeader("x-sal-contenttype", "nNsGdN3REOnPMlKDShOYjlk6VYiEVLSdpfpXAm7o2Tk=")
                .build();
        try {
            return keysService.mapper.readValue(client.newCall(request).execute().body().string(), ObjectNode.class);
        } catch (IOException e) {
            missedIds.add(key);
            return null;
        }
    }

    class PerfIndicators {
        private final String mf_id;
        private final String perfAssetType;
        private final BigDecimal upside;
        private final BigDecimal downside;
        private final BigDecimal maxDrawDown;
        private final Date peak;
        private final Date valley;
        private final BigDecimal duration;

        public PerfIndicators(String mf_id, String perfAssetType, BigDecimal upside, BigDecimal downside, BigDecimal maxDrawDown, Date peak, Date valley, BigDecimal duration) {
            this.mf_id = mf_id;
            this.perfAssetType = perfAssetType;
            this.upside = upside;
            this.downside = downside;
            this.maxDrawDown = maxDrawDown;
            this.peak = peak;
            this.valley = valley;
            this.duration = duration;
        }

        public String getMf_id() {
            return mf_id;
        }

        public String getPerfAssetType() {
            return perfAssetType;
        }

        public BigDecimal getUpside() {
            return upside;
        }

        public BigDecimal getDownside() {
            return downside;
        }

        public BigDecimal getMaxDrawDown() {
            return maxDrawDown;
        }

        public Date getPeak() {
            return peak;
        }

        public Date getValley() {
            return valley;
        }

        public BigDecimal getDuration() {
            return duration;
        }
    }
}
