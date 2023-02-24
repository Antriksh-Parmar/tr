package com.ind.tr.repository;

import com.ind.tr.repository.model.PortfolioEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class PortfolioDaoImpl implements PortfolioDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void savePortfolio(PortfolioEntity portfolioEntity) {
        String query = QueryBuilder.builder()
                .setQueryType(QueryType.INSERT)
                .setSchema("pf")
                .setTableName("portfolios")
                .setParameters("id", "name", "user_id", "created_date", "updated_date")
                .setValues(portfolioEntity.getId().toString(),portfolioEntity.getName(), portfolioEntity.getOwnerId().toString(), portfolioEntity.getCreatedDate(), portfolioEntity.getUpdatedDate())
                .build();
        jdbcTemplate.execute(query);
    }
}
