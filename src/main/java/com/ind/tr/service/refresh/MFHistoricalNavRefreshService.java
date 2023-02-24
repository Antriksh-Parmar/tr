package com.ind.tr.service.refresh;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ind.tr.repository.RefreshMfDataDao;
import com.ind.tr.repository.model.utill.NavQuery;
import com.ind.tr.repository.model.utill.NavResponse;
import com.ind.tr.repository.model.utill.NavStoreDataObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Queries MF Historical Navs are stores it in the database.
 */
@Component
public class MFHistoricalNavRefreshService {

    private final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    private final String toDate = format.format(new Date());
    private final Map<Integer, List<NavResponse>> responses = new HashMap<>();

    //TODO Make this auto wired
    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private RefreshMfDataDao refreshDao;

    public void refresh() {
        List<NavQuery> navs = refreshDao.getInceptionDates();
        int count = 0;
        for(NavQuery n: navs) {
            String apiUrl = "https://api.scripbox.com/instruments/mutual-fund";
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl)
                    .queryParam("to_date", toDate)
                    .queryParam("fund_id", n.getId())
                    .queryParam("from_date", n.transformedDate());
            ResponseEntity<String> response = restTemplate.getForEntity(builder.toUriString(), String.class);
            count += 1;
            System.out.println("Processed Id-> " + n.getId() + ", Total processed entity: " + count);
            String responseBody = response.getBody();

            refreshDao.saveNavs(new NavStoreDataObject(buildNavObject(responseBody), n.getId()));
        }
        System.out.println(responses);
    }

    private List<NavResponse> buildNavObject(String response) {
        try {
            List<NavResponse> navResponses = new ArrayList<>();
            ObjectNode root = new ObjectMapper().readValue(response, ObjectNode.class);
            ArrayNode sourceArray = (ArrayNode) root.get("historical_nav");
            for (JsonNode objNode : sourceArray) {
                BigDecimal adjustedNav = (objNode.get("unadjustednav") != null && !"null".equals(objNode.get("unadjustednav").toString())) ? new BigDecimal(objNode.get("unadjustednav").toString()) : BigDecimal.valueOf(-1);
                NavResponse navResponse = new NavResponse(
                        new BigDecimal(objNode.get("nav").toString()),
                        adjustedNav,
                        new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(objNode.get("nav_date").toString().replace("\"", "")));
                navResponses.add(navResponse);
            }
            return navResponses;
        } catch (JsonProcessingException | ParseException e) {
            System.out.println("FAILED!!!!");
            throw new RuntimeException(e);
        }
    }

}
