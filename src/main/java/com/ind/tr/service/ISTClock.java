package com.ind.tr.service;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;

@Component
public class ISTClock {

    private final ZoneId indiaZone = ZoneId.of("Asia/Kolkata");

    public LocalDate getTodayDate() {
        return LocalDate.now(indiaZone);
    }
}
