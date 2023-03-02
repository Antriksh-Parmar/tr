package com.ind.tr.service.refresh;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class KuveraQueryAllMFKeys {

    @Autowired
    private RestTemplate restTemplate;
}
