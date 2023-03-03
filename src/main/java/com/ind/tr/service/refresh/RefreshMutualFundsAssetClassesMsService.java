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
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class RefreshMutualFundsAssetClassesMsService {

    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSourceBuilder
            .create()
            .url("jdbc:postgresql://localhost:5432/tr")
            .username("trapp")
            .password("amdavad123")
            .driverClassName("org.postgresql.Driver")
            .build());
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private final List<String> unprocessedEntities = new ArrayList<>();
    private final List<String> objKeys = new ArrayList<>();

    public static void main(String[] args) {
        new RefreshMutualFundsAssetClassesMsService().refreshAssetClasses();
    }

    private void refreshAssetClasses() {
        String[] allocationCategories = {"INDAssetAllocStock", "INDAssetAllocConvertible", "INDAssetAllocBond", "INDAssetAllocCash", "INDAssetAllocOther", "INDAssetAllocPreferred", "assetAllocPreferred", "assetAllocOther", "assetAllocEquity", "assetAllocCash", "assetAllocConvertible", "assetAllocFixedIncome"};
        List<MsKeys> keys = queryEligibleMsIds();
        int count = 1;
        for (MsKeys key : keys) {
            System.out.println("Processing - " + count);
            ObjectNode assetClassDetails = getAssetClasses(key.ms_id);
            if (assetClassDetails == null) continue;
            for (String ac : allocationCategories) {
                Allocation allocation = buildAllocation(assetClassDetails, ac, ac.contains("IND"), key);
                saveAssetData(allocation);
            }
            count++;
        }
        System.out.println(unprocessedEntities);
    }

    private Allocation buildAllocation(ObjectNode assetClassDetails, String key, boolean isLocal, MsKeys msKeys) {
        String allocationMap = (isLocal) ? "allocationMap" : "globalAllocationMap";

        return new Allocation(
                msKeys.mf_id,
                msKeys.isin,
                getAlllocKey(key).name(),
                getBigDecimal(assetClassDetails.get(allocationMap).get(key).get("netAllocation")),
                getBigDecimal(assetClassDetails.get(allocationMap).get(key).get("shortAllocation")),
                getBigDecimal(assetClassDetails.get(allocationMap).get(key).get("longAllocation")),
                isLocal
        );

    }

    private AllocationKey getAlllocKey(String key) {
        if (key.contains("Stock") || key.contains(" Equity")) return AllocationKey.EQUITY;
        if (key.contains("Convertible")) return AllocationKey.CONVERTIBLE;
        if (key.contains("Bond") || key.contains("FixedIncome")) return AllocationKey.FIXED_INCOME;
        if (key.contains("Cash")) return AllocationKey.CASH;
        if (key.contains("Preferred")) return AllocationKey.PREFERRED;
        return AllocationKey.OTHER;
    }

    private void saveAssetData(Allocation allocation) {
        String query = "INSERT INTO fi.mutual_fund_asset_classes (mf_id, isin, allocation_category, net_allocation, short_allocation, long_allocation, local_allocation) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, allocation.mfId);
            ps.setString(2, allocation.isin);
            ps.setString(3, allocation.allocationCategory);
            ps.setBigDecimal(4, allocation.netAllocation);
            ps.setBigDecimal(5, allocation.shortAllocation);
            ps.setBigDecimal(6, allocation.longAllocation);
            ps.setBoolean(7, allocation.isLocal);
            return ps;
        });
    }


    private ObjectNode getAssetClasses(String key) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String url = String.format("https://api-global.morningstar.com/sal-service/v1/fund/process/asset/v2/%s/data?languageId=en&locale=en&clientId=RSIN_SAL&benchmarkId=mstarorcat&component=sal-components-mip-asset-allocation&version=3.74.0&access_token=SGr6zkJ31b6c5e3B75bn5AIyosS9", key);
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
                .addHeader("x-api-realtime-e", "ew0KICAiYWxnIjogIlJTQS1PQUVQIiwNCiAgImVuYyI6ICJBMTI4R0NNIg0KfQ.l7-9HpYd-17aXzGsdPOBFpL60dk8Jcuw2ioi0oQIodm6EzdtSPlmI16CthEBa4A8pEWCyqvLbzN99ZRJvnQ3F2DscN9B4Ginx6oj5Z-P50LVWCfIaFBa6O3S_DB9lhVv-ebJCQg8tAX54EVHnk2ESJTU0Jj55moihljhyrsnzjc.bTqvdKxTUSb5cf7n.zHCr4HB6S17NEcd71rFts1DSu_uPJFhomnK30HNa6522-mcQ_kx-YFjtL8NQ-fu3qOOsJvKbZC-k_ONds6qe4-K9NUYI05tgTFpsywHLQIEx8N1m69j_LEsnG3zGzD6t1fwilmovGOxigxilvyoHpZwI-UgewtlJo2-YhXU8_bQB3dIxzuk7eYSwso1-HwwMHyGbqI_X1e8A_UTkMxJcquqzWtCdKwfyD4we93e3LJcXMxUybWx8Kn9NMLt7zgdvr8mRsL6t.g01Vb7aFl6q1DI6HPMl_bA")
                .addHeader("x-api-requestid", "74bc48c0-000c-ed07-03cc-64155ea6b212")
                .addHeader("x-sal-contenttype", "nNsGdN3REOnPMlKDShOYjlk6VYiEVLSdpfpXAm7o2Tk=")
                .build();
        try {
            return mapper.readValue(client.newCall(request).execute().body().string(), ObjectNode.class);
        } catch (IOException e) {
            unprocessedEntities.add(key);
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

    enum AllocationKey {
        EQUITY, CONVERTIBLE, FIXED_INCOME, CASH, OTHER, PREFERRED
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

    class Allocation {
        private final String mfId;
        private final String isin;
        private final String allocationCategory;
        private final BigDecimal netAllocation;
        private final BigDecimal shortAllocation;
        private final BigDecimal longAllocation;
        private final Boolean isLocal;

        public Allocation(String mfId, String isin, String allocationCategory, BigDecimal netAllocation, BigDecimal shortAllocation, BigDecimal longAllocation, Boolean isLocal) {
            this.mfId = mfId;
            this.isin = isin;
            this.allocationCategory = allocationCategory;
            this.netAllocation = netAllocation;
            this.shortAllocation = shortAllocation;
            this.longAllocation = longAllocation;
            this.isLocal = isLocal;
        }

        public String getMfId() {
            return mfId;
        }

        public String getIsin() {
            return isin;
        }

        public String getAllocationCategory() {
            return allocationCategory;
        }

        public BigDecimal getNetAllocation() {
            return netAllocation;
        }

        public BigDecimal getShortAllocation() {
            return shortAllocation;
        }

        public BigDecimal getLongAllocation() {
            return longAllocation;
        }

        public Boolean getLocal() {
            return isLocal;
        }
    }
}
