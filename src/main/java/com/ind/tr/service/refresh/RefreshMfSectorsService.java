package com.ind.tr.service.refresh;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;

public class RefreshMfSectorsService {

    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSourceBuilder
            .create()
            .url("jdbc:postgresql://localhost:5432/tr")
            .username("trapp")
            .password("amdavad123")
            .driverClassName("org.postgresql.Driver")
            .build());
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private final List<String> missedIDs = new ArrayList<>();
    private final List<String> portfolioTypeKeys = new ArrayList<>();
    private final Map<String, String> portKeysToName = new HashMap<>();

    {
        portKeysToName.put("fundPortfolio", "fundName");
        portKeysToName.put("categoryPortfolio", "categoryName");
        portKeysToName.put("indexPortfolio", "indexName");
        portfolioTypeKeys.add("fundPortfolio");
        portfolioTypeKeys.add("categoryPortfolio");
        portfolioTypeKeys.add("indexPortfolio");

    }

    public static void main(String[] args) {
        new RefreshMfSectorsService().refreshMfSectors();
    }

    private void refreshMfSectors() {
        List<MsKeys> keys = queryEligibleMsIds();
        int count = 1;
        for (MsKeys key : keys) {
            System.out.println("Processing - " + count);
            ObjectNode root = querySectors(key.ms_id);
            if (root == null) continue;
            JsonNode fixedIncome = root.get("FIXEDINCOME");
            JsonNode equity = root.get("EQUITY");
            List<Portfolio> portfolios = getPortfolios(equity, key, "EQUITY");
            portfolios.addAll(getPortfolios(fixedIncome, key, "FIXED_INCOME"));
            saveSectorBasedAllocation(portfolios);
            count++;
        }
        System.out.println(missedIDs);
    }

    private void saveSectorBasedAllocation(List<Portfolio> portfolios) {
        String query = "INSERT INTO fi.mutual_fund_sector_allocation (mf_id, portfolio_investment_category, portfolio_type, portfolio_name, sector_name, allocation_value) " +
                "VALUES (?, ?, ?,  ?, ?, ?)";

        jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Portfolio portfolio = portfolios.get(i);
                ps.setString(1, portfolio.getMfId());
                ps.setString(2, portfolio.getPortfolioCategory());
                ps.setString(3, portfolio.getPortfolioType());
                ps.setString(4, portfolio.getPortfolioName());
                ps.setString(5, portfolio.getSectorName());
                ps.setBigDecimal(6, portfolio.getAllocationValue());
            }

            @Override
            public int getBatchSize() {
                return portfolios.size();
            }
        });
    }


    List<Portfolio> getPortfolios(JsonNode node, MsKeys keys, String portfolioCategory) {
        List<Portfolio> result = new ArrayList<>();
        for (String portfolioKey : portfolioTypeKeys) {
            Iterator<String> fields = node.get(portfolioKey).fieldNames();
            while (fields.hasNext()) {
                String field = fields.next();
                if (field.equals("portfolioDate")) continue;
                result.add(new Portfolio(
                        keys.mf_id,
                        portfolioCategory,
                        portfolioKey,
                        getStringValue(node.get(portKeysToName.get(portfolioKey))),
                        field,
                        getBigDecimal(node.get(portfolioKey).get(field))
                ));
            }
        }

        return result;
    }

    private ObjectNode querySectors(String key) {
        try {
            Thread.sleep(70);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String url = String.format("https://api-global.morningstar.com/sal-service/v1/fund/portfolio/v2/sector/%s/data?languageId=en&locale=en&clientId=RSIN_SAL&benchmarkId=mstarorcat&component=sal-components-mip-sector-exposure&version=3.74.0&access_token=mCtvXHKRuYMA6DexTmG9ZrtLFzY3", key);
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
                .addHeader("x-api-realtime-e", "ew0KICAiYWxnIjogIlJTQS1PQUVQIiwNCiAgImVuYyI6ICJBMTI4R0NNIg0KfQ.feHGSS0kVS89-vh4YieM4Bd3s49PJ0JGoRzIqfcxxe_IP2atl0SSdOyO7rntQu7JEF4w8Hqvy7PiAIfZ-RXgVa-Ei7T7PFRCFxeVi2NlaeXbGCltOXQCHGrN9qt2h_-FYD8ZCWElm2mMobQz7dL5WUITXL9EZafRDZowpQxpfDM.XGf5-RliqoBnEHzp.vFX6Ei93qo_zv7lFYF_0HaJ-p7ItS5VQxlNY6zR7wKcnPCI2xW0vYwqNYt-xIJQaoOv3nxG6fEph6OldmdYvSnuAKQ9zM0VeFt6d21jxnRGE9EPh6p0yevoX2JsidbP4uW7fHnbRgzSCnavB2Sxu7vpoC7fc9M6BuoX_qXG8HM3yhQ6Ppfik4EhsVaDbWO0_EmszVB5jGFcxuHp1iNZk4KmWtKD0ERSflHosD5toHuQVk8ItTVzpsIBD7N2gi9ZrWOP6gJpV.vY8RLLmRi4hz5lB5L826mQ")
                .addHeader("x-api-requestid", "f69965de-bde7-30dc-c539-9ee97f8dff91")
                .addHeader("x-sal-contenttype", "nNsGdN3REOnPMlKDShOYjlk6VYiEVLSdpfpXAm7o2Tk=")
                .build();
        try {
            return mapper.readValue(client.newCall(request).execute().body().string(), ObjectNode.class);
        } catch (IOException e) {
            missedIDs.add(key);
            return null;
        }
    }

    private List<MsKeys> queryEligibleMsIds() {
        String query = "SELECT ms_id, mf_id, isin from fi.key_mappings WHERE kv_id IS NOT NULL";
        return jdbcTemplate.query(query, (rs, rowNum) -> new MsKeys(rs.getString("isin"), rs.getString("mf_id"), rs.getString("ms_id")));
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

    class Portfolio {
        private final String mfId;
        private final String portfolioCategory;
        private final String portfolioType;
        private final String portfolioName;
        private final String sectorName;
        private final BigDecimal allocationValue;

        public Portfolio(String mfId, String portfolioCategory, String portfolioType, String portfolioName, String sectorName, BigDecimal allocationValue) {
            this.mfId = mfId;
            this.portfolioCategory = portfolioCategory; // FIXED_INCOME/EQUITY
            this.portfolioType = portfolioType; //fund, index, category
            this.portfolioName = portfolioName; // categoryName, indexName, fundName
            this.sectorName = sectorName; //
            this.allocationValue = allocationValue;
        }

        public String getMfId() {
            return mfId;
        }

        public String getPortfolioCategory() {
            return portfolioCategory;
        }

        public String getPortfolioType() {
            return portfolioType;
        }

        public String getPortfolioName() {
            return portfolioName;
        }

        public String getSectorName() {
            return sectorName;
        }

        public BigDecimal getAllocationValue() {
            return allocationValue;
        }
    }

    class MsKeys {
        private final String isin;
        private final String mf_id;
        private final String ms_id;

        public MsKeys(String isin, String mf_id, String ms_id) {
            this.isin = isin;
            this.mf_id = mf_id;
            this.ms_id = ms_id;
        }

        public String getIsin() {
            return isin;
        }

        public String getMf_id() {
            return mf_id;
        }

        public String getMs_id() {
            return ms_id;
        }
    }
}
