package com.ind.tr.service;

import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Component
public class ISTClock {

    private final ZoneId indiaZone = ZoneId.of("Asia/Kolkata");

    public Clock getClock() {
        return Clock.system(indiaZone);
    }

    public Date getTodayDate() {
        return new Date(getClock().millis());
    }

    public LocalDate getTodayLocalDate() {
        return LocalDate.now(indiaZone);
    }
}
