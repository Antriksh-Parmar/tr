package com.ind.tr.persistance.model.utill;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class NavQuery {
    private int id;
    private String fromDate;
    private final SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy", Locale.US);

    public NavQuery(int id, String fromDate) {
        this.id = id;
        this.fromDate = fromDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String transformedDate() {
        try {
            return new SimpleDateFormat("dd/MM/yyyy").format(format.parse(this.fromDate));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}