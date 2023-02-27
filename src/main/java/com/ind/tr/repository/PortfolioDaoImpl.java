package com.ind.tr.repository;

import com.ind.tr.repository.model.PortfolioEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class PortfolioDaoImpl implements PortfolioDao {

    private final QueryBuilder portfolioQueryBuilder = QueryBuilder.builder()
            .setSchema("pf")
            .setTableName("portfolios");
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private PortfolioRowMapper mapper;

    @Override
    public void savePortfolio(PortfolioEntity portfolioEntity) {

        String query = String.format("INSERT INTO pf.portfolios " +
                        "(id, name, user_id, created_date, updated_date) VALUES " +
                        "('%s', '%s', '%s', '%s', '%s')",
                portfolioEntity.getId().toString(), portfolioEntity.getName(), portfolioEntity.getOwnerId().toString(), portfolioEntity.getCreatedDate(), portfolioEntity.getUpdatedDate());
        jdbcTemplate.execute(query);
    }

    @Override
    public Optional<PortfolioEntity> getPortfolio(UUID portfolioId) {
        String query = String.format("SELECT * FROM pf.portfolios WHERE id='%s'", portfolioId.toString());
        List<PortfolioEntity> portfolioEntities = jdbcTemplate.query(query, mapper);
        if (portfolioEntities.size() > 0) return Optional.of(portfolioEntities.get(0));
        else return Optional.empty();
    }

    @Override
    public List<PortfolioEntity> getAllPortfolios(UUID userId) {
        String query = String.format("SELECT * FROM pf.portfolios WHERE user_id='%s'", userId.toString());
        return jdbcTemplate.query(query, mapper);
    }

    @Override
    public void deletePortfolio(UUID portfolioId) {
        String query = "DELETE FROM pf.portfolios where id='" + portfolioId.toString() + "'";
        jdbcTemplate.execute(query);
    }

    @Override
    public void deletePortfolios(UUID uuid) {
        String query = "DELETE FROM pf.portfolios where user_id='" + uuid.toString() + "'";
        jdbcTemplate.execute(query);
    }
}

@Component
class PortfolioRowMapper implements RowMapper<PortfolioEntity> {

    @Override
    public PortfolioEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new PortfolioEntity(
                UUID.fromString(rs.getString("id")),
                UUID.fromString(rs.getString("user_id")),
                rs.getString("name"),
                rs.getDate("created_date"),
                rs.getDate("updated_date")
        );
    }
}