package com.ind.tr.repository;

import com.ind.tr.repository.model.InvestmentType;
import com.ind.tr.repository.model.MutualFundInvestmentEntity;
import com.ind.tr.repository.model.SipInterval;
import com.ind.tr.repository.model.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class MutualFundDaoImpl implements MutualFundDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MutualFundRowMapper mutualFundRowMapper;

    @Override
    public Optional<MutualFundInvestmentEntity> getMutualFund(UUID mfId) {
        String query = String.format("SELECT * FROM pf.mutual_funds_investments where id='%s'", mfId.toString());
        List<MutualFundInvestmentEntity> mutualFundEntities = jdbcTemplate.query(query, mutualFundRowMapper);
        if (mutualFundEntities.size() > 0) {
            return Optional.of(mutualFundEntities.get(0));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<MutualFundInvestmentEntity> getMutualFunds(UUID portfolioId) {
        String query = String.format("SELECT * FROM pf.mutual_funds_investments where portfolio_id='%s'", portfolioId.toString());
        return jdbcTemplate.query(query, mutualFundRowMapper);
    }

    @Override
    public void deleteMutualFund(UUID id) {
        String query = "DELETE FROM pf.mutual_funds_investments WHERE id='" + id.toString() + "'";
        jdbcTemplate.execute(query);
    }

    @Override
    public void deleteAllMutualFunds(UUID portfolioId) {
        String query = "DELETE FROM pf.mutual_funds_investments WHERE portfolio_id='" + portfolioId.toString() + "'";
        jdbcTemplate.execute(query);
    }

    @Override
    public void addMutualFund(MutualFundInvestmentEntity mf) {
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO pf.mutual_funds_investments (id, mutual_fund_id, portfolio_id, source, investment_type, sip_interval, sip_start_date, sip_amount, lump_sum_investment_date, lump_sum_amount, created_date, updated_date) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");
            preparedStatement.setString(1, mf.getId().toString());
            preparedStatement.setString(2, mf.getMutualFundId().toString());
            preparedStatement.setString(3, mf.getPortfolioId().toString());
            preparedStatement.setString(4, mf.getSource().toString());
            preparedStatement.setString(5, mf.getInvestmentType().toString());
            preparedStatement.setString(6, mf.getSipInterval().toString());
            preparedStatement.setDate(7, mf.getSipStartDate());
            preparedStatement.setBigDecimal(8, mf.getSipAmount());
            preparedStatement.setDate(9, mf.getLumpSumInvestmentDate());
            preparedStatement.setBigDecimal(10, mf.getLumpSumInvestmentAmount());
            preparedStatement.setDate(11, mf.getCreatedDate());
            preparedStatement.setDate(12, mf.getUpdatedDate());
            return preparedStatement;
        });
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
