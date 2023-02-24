package com.ind.tr.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

enum QueryType {
    INSERT, SELECT
}

public class QueryBuilder {
    private String schema;
    private String tableName;
    private QueryType queryType;
    private String[] parameters;
    private Object[] values;
    private String conditions;

    private QueryBuilder() {
    }

    public static QueryBuilder builder() {
        return new QueryBuilder();
    }

    public QueryBuilder setSchema(String schema) {
        this.schema = schema;
        return this;
    }

    public QueryBuilder setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public QueryBuilder setQueryType(QueryType queryType) {
        this.queryType = queryType;
        return this;
    }

    public QueryBuilder setParameters(String... parameters) {
        this.parameters = parameters;
        return this;
    }

    public QueryBuilder setValues(Object... values) {
        this.values = values;
        return this;
    }

    public QueryBuilder setConditions(String conditions) {
        this.conditions = conditions;
        return this;
    }

    public String build() {
        StringBuilder query = new StringBuilder();
        if (queryType == QueryType.SELECT) {
            query = new StringBuilder("SELECT ");
            for (int i = 0; i < parameters.length; i++) {
                query.append(parameters[i]);
                if (i != parameters.length - 1) {
                    query.append(", ");
                }
            }
            query.append(" FROM ").append(schema).append(".").append(tableName);
            if (conditions != null && !conditions.isEmpty()) {
                query.append(" WHERE ").append(conditions);
            }
        } else if (queryType == QueryType.INSERT) {
            query = new StringBuilder("INSERT INTO " + schema + "." + tableName + " (");
            for (int i = 0; i < parameters.length; i++) {
                query.append(parameters[i]);
                if (i != parameters.length - 1) {
                    query.append(", ");
                }
            }
            query.append(") VALUES (");
            for (int i = 0; i < values.length; i++) {
                if (values[i] instanceof String) {
                    query.append("'").append(values[i]).append("'");
                } else if (values[i] instanceof BigDecimal) {
                    query.append(((BigDecimal) values[i]).toPlainString());
                } else if (values[i] instanceof Integer) {
                    query.append(((Integer) values[i]).toString());
                } else if (values[i] instanceof UUID) {
                    query.append("'").append(values[i].toString()).append("'");
                } else if (values[i] instanceof LocalDate) {
                    query.append("'").append(((LocalDate) values[i]).format(DateTimeFormatter.ISO_LOCAL_DATE)).append("'");
                }
                if (i != values.length - 1) {
                    query.append(", ");
                }
            }
            query.append(")");
        }
        return query.toString();
    }
}