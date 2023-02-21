package com.ind.tr.controller;

import com.ind.tr.controller.model.MfEquityExposureResponse;
import com.ind.tr.controller.model.Status;
import com.ind.tr.service.analytics.MfTotalEquityExposureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
public class MfAnalyticsController {

    @Autowired
    private MfTotalEquityExposureService mfTotalEquityExposureService;

    @GetMapping("/equity-exposure")
    public ResponseEntity<MfEquityExposureResponse> getEquityExposure(@RequestParam("keys") List<String> mfKeys) {
        if (mfKeys == null || mfKeys.size() == 0) {
            return ResponseEntity.ok(new MfEquityExposureResponse(Collections.emptyMap(), Status.FAILURE));
        }
        return null;
    }

}
