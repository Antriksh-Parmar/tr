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
import java.sql.PreparedStatement;
import java.util.UUID;
import java.util.function.Function;

public class QueryAllMsMfDataService {

    public static void main(String[] args) {
        new QueryAllMsMfDataService().queryAllMfData();
    }

    public void queryAllMfData() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSourceBuilder
                .create()
                .url("jdbc:postgresql://localhost:5432/tr")
                .username("trapp")
                .password("amdavad123")
                .driverClassName("org.postgresql.Driver")
                .build());

        ObjectNode root = queryFunds();
        ArrayNode funds = (ArrayNode) root.get("rows");
        for (JsonNode fund : funds) {
            if (fund.get("ISIN") == null) continue;
            UUID uuid = UUID.randomUUID();

            jdbcTemplate.update(con -> {
                PreparedStatement preparedStatement = con.prepareStatement(
                        "INSERT INTO fi.mutual_funds(id, amfi_code, fund_of_funds, category_name, amc_name, exchange_traded, purchase_mode, isin)" +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
                preparedStatement.setString(1, uuid.toString());
                preparedStatement.setString(2, getValue(fund.get("AMFICode"), JsonNode::asText));
                preparedStatement.setBoolean(3, getValue(fund.get("FundOfFunds"), JsonNode::asBoolean));
                preparedStatement.setString(4, getValue(fund.get("CategoryName"), JsonNode::asText));
                preparedStatement.setString(5, getValue(fund.get("ProviderCompanyName"), JsonNode::asText));
                preparedStatement.setBoolean(6, getValue(fund.get("ExchangeTradedShare"), JsonNode::asBoolean));
                preparedStatement.setInt(7, (fund.get("PurchaseMode") == null) ? -1 : fund.get("PurchaseMode").asInt());
                preparedStatement.setString(8, getValue(fund.get("ISIN"), JsonNode::asText));
                return preparedStatement;
            });
            String insertMappings = "INSERT INTO fi.key_mappings(mf_id, isin, ms_id) VALUES (" +
                    "'" + uuid.toString() + "', " +
                    "'" + getValue(fund.get("ISIN"), JsonNode::asText) + "', " +
                    "'" + getValue(fund.get("SecId"), JsonNode::asText) + "')";
            jdbcTemplate.update(insertMappings);
        }
    }

    private <T> T getValue(JsonNode node, Function<JsonNode, T> converter) {
        if (node == null) {
            return null;
        } else {
            return converter.apply(node);
        }
    }

    private ObjectNode queryFunds() {

        OkHttpClient client = new OkHttpClient();
        ObjectMapper mapper = new ObjectMapper();
        Request request = new Request.Builder()
                .url("https://lt.morningstar.com/api/rest.svc/dk7pkae7kl/security/screener?page=1&pageSize=15000&sortOrder=LegalName%20asc&outputType=json&version=1&languageId=en-GB&currencyId=INR&universeIds=FOIND%24%24ALL%7CFCIND%24%24ALL&securityDataPoints=SecId%7CName%7CLegalName%7CCategoryName%7CFundName%7CExpenseRatio%7CAverageMarketCapital%7CFundId%7CProviderCompanyName%7CInceptionDate%7CPurchaseMode%7CFundOfFunds%7CExchangeTradedShare%7CISIN%7CAMFICode%7C&filters=&term=&subUniverseId=")
                .method("GET", null)
                .addHeader("authority", "lt.morningstar.com")
                .addHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
                .addHeader("accept-language", "en-US,en;q=0.9,hi-IN;q=0.8,hi;q=0.7,mt;q=0.6")
                .addHeader("cookie", "_gcl_au=1.1.1173648548.1674470488; _cb=DAY9P76gc4DrlKka; _biz_uid=f191f1dffa6f45d4b394459b056fe188; ELOQUA=GUID=8CA39BE734AE47B09E4318F9D3C45608; _biz_flagsA=%7B%22Version%22%3A1%2C%22ViewThrough%22%3A%221%22%2C%22XDomain%22%3A%221%22%2C%22Frm%22%3A%221%22%7D; _biz_nA=7; _biz_pendingA=%5B%5D; _ga=GA1.1.40324560.1674470488; _uetvid=7dd439c09b0a11eda10b93cc68365ab5; _chartbeat2=.1674470488089.1676223325474.0000000000000001.D3OWOxCVxxwYBjf6C5fCAclCzSg1m.2; _ga_G8C0R44VCK=GS1.1.1676225761.5.0.1676225761.60.0.0; _ga_LMTVSGTMT2=GS1.1.1676480331.4.1.1676480357.0.0.0; _ga_SW6G2BFREG=GS1.1.1676480331.4.1.1676480357.0.0.0; _ga_DLZP83KFTR=GS1.1.1676480331.4.1.1676480357.0.0.0; mid=16903496217962704086; ASP.NET_SessionId=pq02v3ho3p2sj1mzbvt4lyrq; __RequestVerificationToken=3DvoJZPya1thv5OwhfJHKNxVEa16Dvwn2YGuAlBLNG57tovYXJ09_fSlDdLZrZMa3iFNsmw8uob5d0U2xbNseODldDg0ET0QyDbVmGskI0o1; __utma=267574099.40324560.1674470488.1676705375.1677606692.2; __utmc=267574099; __utmz=267574099.1677606692.2.2.utmcsr=morningstar.in|utmccn=(referral)|utmcmd=referral|utmcct=/; ASP.NET_SessionId=t3jkadlfi2krv0rnpmjgexg1")
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
            ResponseBody response = client.newCall(request).execute().body();
            return mapper.readValue(response.string(), ObjectNode.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
