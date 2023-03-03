package com.ind.tr.service.refresh;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class RefreshMfHoldingsService {

    // Unprocessed entities [F00001EZSX, F00001EZSY, F00001EZSZ, F00001F4BM, F00001F0EE, F00001F0EF, F00001EZNR, F00001EZNS, F00001EZNT, F00001F0RH, F00001F0KI, F00001F0KJ, F00001F0KK, F00001F015, F00001F016, F00001F017, F00001F0K0, F00001F0K2, F00001EXZT, F00001EXZO, F00001EZF4, F00001EYS8, F00001F0K1]
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSourceBuilder
            .create()
            .url("jdbc:postgresql://localhost:5432/tr")
            .username("trapp")
            .password("amdavad123")
            .driverClassName("org.postgresql.Driver")
            .build());

    private List<String> missedIDs = new ArrayList<>();

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

    private final OkHttpClient client = new OkHttpClient();

    public static void main(String[] args) {
        new RefreshMfHoldingsService().refreshHoldings();
    }

    private void refreshHoldings() {
        List<MsKeys> keys = queryEligibleMsIds();

        int count = 1;
        for (MsKeys key : keys) {
            System.out.println("Processing element# " + count);
            ObjectNode node = getHoldings(key.getMs_id());
            if(node == null) continue;

            ArrayNode equityHoldings = (ArrayNode) node.get("equityHoldingPage").get("holdingList");
            saveHoldings(key, equityHoldings);
            System.out.println("Processed equity holdings");

            ArrayNode bondHoldings = (ArrayNode) node.get("boldHoldingPage").get("holdingList");
            saveHoldings(key, bondHoldings);
            System.out.println("Processed bond holdings");

            ArrayNode otherHoldings = (ArrayNode) node.get("otherHoldingPage").get("holdingList");
            saveHoldings(key, otherHoldings);
            System.out.println("Processed other holdings");

            count++;
        }
        System.out.println(missedIDs);
    }

    private void saveHoldings(MsKeys key, ArrayNode holdings) {
        for(JsonNode holding: holdings) {
            String updateQuery = "INSERT INTO fi.mutual_fund_holdings(mf_id, mf_isin, name, holding_type, weight, number_of_shares, market_value, country, sector_code, asset_isin, cusip, super_sector, primary_sector, secondary_sector, first_bought_date, coupon) " +
                    "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(updateQuery);
                ps.setString(1, key.mf_id);
                ps.setString(2, key.isin);
                ps.setString(3, getStringValue(holding.get("securityName")));
                ps.setString(4, getStringValue(holding.get("holdingType")));
                ps.setBigDecimal(5, getBigDecimal(holding.get("weighting")));
                ps.setBigDecimal(6, getBigDecimal(holding.get("numberOfShare")));
                ps.setBigDecimal(7, getBigDecimal(holding.get("marketValue")));
                ps.setString(8, getStringValue(holding.get("country")));
                ps.setString(9, getStringValue(holding.get("sector")));
                ps.setString(10, getStringValue(holding.get("isin")));
                ps.setString(11, getStringValue(holding.get("cusip")));
                ps.setString(12, getStringValue(holding.get("superSectorName")));
                ps.setString(13, getStringValue(holding.get("primarySectorName")));
                ps.setString(14, getStringValue(holding.get("secondarySectorName")));
                ps.setDate(15, getDate(holding.get("firstBoughtDate")));
                ps.setBigDecimal(16, getBigDecimal(holding.get("coupon")));
                return ps;
            });
        }
    }

    private Date getDate(JsonNode node) {
        if(node == null) return null;
        try {
            return new Date(dateFormat.parse(node.asText()).getTime());
        } catch (ParseException e) {
            System.out.println("Error parsing date - " + e.getMessage());
            return null;
        }
    }

    private ObjectNode getHoldings(String key) {
        String url = String.format("https://api-global.morningstar.com/sal-service/v1/fund/portfolio/holding/v2/%s/data?premiumNum=1000&freeNum=5000&languageId=en&locale=en&clientId=RSIN_SAL&benchmarkId=mstarorcat&component=sal-components-mip-holdings&version=3.74.0&access_token=SGr6zkJ31b6c5e3B75bn5AIyosS9", key);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .addHeader("authority", "api-global.morningstar.com")
                .addHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
                .addHeader("accept-language", "en-US,en;q=0.9,hi-IN;q=0.8,hi;q=0.7,mt;q=0.6")
                .addHeader("cache-control", "max-age=0")
                .addHeader("cookie", "_gcl_au=1.1.1173648548.1674470488; _cb=DAY9P76gc4DrlKka; _biz_uid=f191f1dffa6f45d4b394459b056fe188; ELOQUA=GUID=8CA39BE734AE47B09E4318F9D3C45608; _biz_flagsA=%7B%22Version%22%3A1%2C%22ViewThrough%22%3A%221%22%2C%22XDomain%22%3A%221%22%2C%22Frm%22%3A%221%22%7D; _biz_nA=7; _biz_pendingA=%5B%5D; _ga=GA1.1.40324560.1674470488; _uetvid=7dd439c09b0a11eda10b93cc68365ab5; _chartbeat2=.1674470488089.1676223325474.0000000000000001.D3OWOxCVxxwYBjf6C5fCAclCzSg1m.2; _ga_G8C0R44VCK=GS1.1.1676225761.5.0.1676225761.60.0.0; _ga_LMTVSGTMT2=GS1.1.1676480331.4.1.1676480357.0.0.0; _ga_SW6G2BFREG=GS1.1.1676480331.4.1.1676480357.0.0.0; _ga_DLZP83KFTR=GS1.1.1676480331.4.1.1676480357.0.0.0; mid=16903496217962704086; JSESSIONID=C19C3F910B9DF1AB5E8CD70E0E26C672")
                .addHeader("sec-ch-ua", "\"Chromium\";v=\"110\", \"Not A(Brand\";v=\"24\", \"Google Chrome\";v=\"110\"")
                .addHeader("sec-ch-ua-mobile", "?0")
                .addHeader("sec-ch-ua-platform", "\"macOS\"")
                .addHeader("sec-fetch-dest", "document")
                .addHeader("sec-fetch-mode", "navigate")
                .addHeader("sec-fetch-site", "none")
                .addHeader("sec-fetch-user", "?1")
                .addHeader("upgrade-insecure-requests", "1")
                .addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36")
                .build();
        try {
            return new ObjectMapper().readValue(client.newCall(request).execute().body().string(), ObjectNode.class);
        } catch (IOException e) {
            missedIDs.add(key);
            System.out.println("Could not process - " + key);
            System.out.println(e.getMessage());
            return null;
        }
    }

    private List<MsKeys> queryEligibleMsIds() {
        String query = "SELECT ms_id, mf_id, isin from fi.key_mappings WHERE kv_id IS NOT NULL";
        return jdbcTemplate.query(query, (rs, rowNum) -> new MsKeys(rs.getString("isin"), rs.getString("mf_id"), rs.getString("ms_id")));
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
}
