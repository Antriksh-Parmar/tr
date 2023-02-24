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


    @Value("${liquibase.changelog}")
    private String liquibaseChangeLog;

    @Value("${postgres.url}")
    private String dbUrl;

    @Value("${postgres.username}")
    private String username;

    @Value("${postgres.password}")
    private String password;

    @Value("${postgres.driver-class-name}")
    private String driverClass;

    @Value("${mongo.url}")
    private String mongoUrl;

    @Value("${mongo.db}")
    private String mongoDb;

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create(mongoUrl);
    }

    @Bean
    public MongoTemplate mongoTemplate(@Autowired MongoClient mongoClient) {
        return new MongoTemplate(mongoClient, mongoDb);
    }

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
        springLiquibase.setChangeLog(liquibaseChangeLog);
        return springLiquibase;
    }

}
