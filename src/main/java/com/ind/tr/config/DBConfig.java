package com.ind.tr.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DBConfig {

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create("mongodb://localhost:27017");
    }

    @Bean
    public MongoTemplate mongoTemplate(@Autowired MongoClient mongoClient) {
        return new MongoTemplate(mongoClient, "mf_analytics");
    }

    @Value("${datasource.url}")
    private String dbUrl;
    @Value("${datasource.username}")
    private String username;
    @Value("${datasource.password}")
    private String password;
    @Value("${datasource.driver-class-name}")
    private String driverClass;

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder
                .create()
                .url(dbUrl)
                .username(username)
                .password(password)
                .driverClassName(driverClass)
                .build();
    }

    @Bean
    public JdbcTemplate jdbcTemplate(@Autowired DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public SpringLiquibase springLiquibase(@Autowired DataSource dataSource) {
        SpringLiquibase springLiquibase = new SpringLiquibase();
        springLiquibase.setDataSource(dataSource);
        springLiquibase.setChangeLog("database/db-changelog-master.yaml");
        return springLiquibase;
    }

}
