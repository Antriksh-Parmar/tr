package com.ind.tr.service.refresh;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Stores scriptbox data into Json
 */
@Component
public class SbDataScanService {

    private final Map<String, Object> requestBody = new HashMap<>();
    private final HttpHeaders headers = new HttpHeaders();

    // TODO Inject this using spring
    private RestTemplate restTemplate = new RestTemplate();

    public void exploreAPI() {
        int count = 0;
        requestBody.put("q", "");
        requestBody.put("skip_filter", true);
        requestBody.put("sort_by", "tag_priority");
        int batchSize = 100;
        requestBody.put("size", batchSize);
        headers.setContentType(MediaType.APPLICATION_JSON);

        int maxEntries = 6100;
        while (count < maxEntries) {
            requestBody.put("from", count);
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
            String outputFile = "mf_scriptbox_";
            String outputFileName = outputFile + count + "_.json";
            FileWriter writer = null;

            String apiUrl = "https://api.scripbox.com/instruments/search";
            String response = restTemplate.postForObject(apiUrl, requestEntity, String.class);
            try {
                writer = new FileWriter(outputFileName);
                writer.write(response);
                writer.flush();
                Thread.sleep(100);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            count += batchSize;
        }
    }
}
