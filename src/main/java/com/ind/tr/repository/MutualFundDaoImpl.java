package com.ind.tr.repository;

import com.ind.tr.repository.model.InvestmentType;
import com.ind.tr.repository.model.MutualFundInvestmentEntity;
import com.ind.tr.repository.model.SipInterval;
import com.ind.tr.repository.model.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Component
public class MutualFundDaoImpl implements MutualFundDao {

    private final QueryBuilder mfQueryBuilder = QueryBuilder.builder().setSchema("pf").setTableName("mutual_funds_investments");

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MutualFundRowMapper mutualFundRowMapper;

    @Override
    public MutualFundInvestmentEntity getMutualFund(UUID mfId) {
        String query = mfQueryBuilder.setConditions("id = '" + mfId.toString() + "'").build();
        List<MutualFundInvestmentEntity> mutualFundEntities = jdbcTemplate.query(query, mutualFundRowMapper);
        if (mutualFundEntities.size() > 0) {
            return mutualFundEntities.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<MutualFundInvestmentEntity> getMutualFunds(UUID portfolioId) {
        String query = mfQueryBuilder
                .setQueryType(QueryType.SELECT)
                .setConditions("portfolioId = '" + portfolioId.toString() + "'")
                .build();
        return jdbcTemplate.query(query, mutualFundRowMapper);
    }

    @Override
    public void deleteMutualFund(UUID mfId) {
        String query = "DELETE FROM pf.mutual_funds_investments WHERE id='" + mfId.toString() + "'";
        jdbcTemplate.execute(query);
    }

    @Override
    public void deleteAllMutualFunds(UUID portfolioId) {
        String query = "DELETE FROM pf.mutual_funds_investments WHERE portfolio_id='" + portfolioId.toString() + "'";
        jdbcTemplate.execute(query);
    }

    @Override
    public void addMutualFund(MutualFundInvestmentEntity mf) {
        String query = mfQueryBuilder
                .setQueryType(QueryType.INSERT)
                .setParameters(
                        "id", "portfolio_id", "source", "investment_type", "sip_interval",
                        "sip_start_date", "sip_amount", "lump_sum_investment_date", "lump_sum_amount",
                        "created_date", "updated_date")
                .setValues(
                        mf.getId().toString(), mf.getPortfolioId().toString(),
                        mf.getSource().toString(), mf.getInvestmentType().toString(), mf.getSipInterval().toString(),
                        mf.getSipStartDate(), mf.getSipAmount(), mf.getLumpSumInvestmentDate(),
                        mf.getLumpSumInvestmentAmount(), mf.getCreatedDate(), mf.getUpdatedDate())
                .build();
        jdbcTemplate.execute(query);
    }
}

@Component
class MutualFundRowMapper implements RowMapper<MutualFundInvestmentEntity> {
    @Override
    public MutualFundInvestmentEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new MutualFundInvestmentEntity(
                UUID.fromString(rs.getString("id")),
                UUID.fromString(rs.getString("mutual_fund_id")),
                UUID.fromString(rs.getString("portfolio_id")),
                Source.valueOf(rs.getString("source")),
                InvestmentType.valueOf(rs.getString("investment_type")),
                SipInterval.valueOf(rs.getString("sip_interval")),
                rs.getDate("sip_start_date"),
                rs.getBigDecimal("sip_amount"),
                rs.getDate("lump_sum_investment_date"),
                rs.getBigDecimal("lump_sum_amount"),
                rs.getDate("created_date"),
                rs.getDate("updated_date")
        );
    }
}
