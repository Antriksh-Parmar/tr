package com.ind.tr.repository;

import com.ind.tr.repository.model.PortfolioEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Component
public class PortfolioDaoImpl implements PortfolioDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PortfolioRowMapper mapper;

    private final QueryBuilder portfolioQueryBuilder = QueryBuilder.builder()
            .setSchema("pf")
            .setTableName("portfolios");

    @Override
    public void savePortfolio(PortfolioEntity portfolioEntity) {
        String query = portfolioQueryBuilder
                .setQueryType(QueryType.INSERT)
                .setParameters("id", "name", "user_id", "created_date", "updated_date")
                .setValues(portfolioEntity.getId().toString(), portfolioEntity.getName(), portfolioEntity.getOwnerId().toString(), portfolioEntity.getCreatedDate(), portfolioEntity.getUpdatedDate())
                .build();
        jdbcTemplate.execute(query);
    }

    @Override
    public PortfolioEntity getPortfolio(UUID portfolioId) {
        String query = portfolioQueryBuilder
                .setQueryType(QueryType.SELECT)
                .setConditions("id ='" + portfolioId.toString() + "'")
                .build();
        return jdbcTemplate.queryForObject(query, mapper);
    }

    @Override
    public List<PortfolioEntity> getAllPortfolios(UUID userId) {
        String query = portfolioQueryBuilder
                .setQueryType(QueryType.SELECT)
                .setConditions("owner_id ='" + userId.toString() + "'")
                .build();
        return jdbcTemplate.query(query, mapper);
    }

    @Override
    public void deletePortfolio(UUID portfolioId) {
        String query = "DELETE FROM pf.portfolios where id='" + portfolioId.toString() + "'";
        jdbcTemplate.execute(query);
    }

    @Override
    public void deletePortfolios(UUID uuid) {
        String query = "DELETE FROM pf.portfolios where owner_id='" + uuid.toString() + "'";
        jdbcTemplate.execute(query);
    }
}

@Component
class PortfolioRowMapper implements RowMapper<PortfolioEntity> {

    @Override
    public PortfolioEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new PortfolioEntity(
                UUID.fromString(rs.getString("id")),
                UUID.fromString(rs.getString("owner_id")),
                rs.getString("name"),
                rs.getDate("created_date"),
                rs.getDate("updated_date")
        );
    }
}