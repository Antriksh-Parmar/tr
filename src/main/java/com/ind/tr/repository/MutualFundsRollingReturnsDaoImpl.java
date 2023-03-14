package com.ind.tr.repository;

import com.ind.tr.repository.model.RollingReturnEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Component
public class MutualFundsRollingReturnsDaoImpl implements MutualFundsRollingReturnsDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void saveRollingReturns(List<RollingReturnEntity> rollingReturnEntities) {
        String query = "INSERT INTO fi.mutual_funds_rolling_returns (mf_id, rr_date, one_month, three_month, six_month, one_year, three_years, five_years, seven_years, ten_years) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                RollingReturnEntity rollingReturn = rollingReturnEntities.get(i);
                ps.setString(1, rollingReturn.getMfId());
                ps.setDate(2, rollingReturn.getRrDate());
                ps.setBigDecimal(3, rollingReturn.getOneMonth());
                ps.setBigDecimal(4, rollingReturn.getThreeMonths());
                ps.setBigDecimal(5, rollingReturn.getSixMonths());
                ps.setBigDecimal(6, rollingReturn.getOneYear());
                ps.setBigDecimal(7, rollingReturn.getThreeYears());
                ps.setBigDecimal(8, rollingReturn.getFiveYears());
                ps.setBigDecimal(9, rollingReturn.getSevenYears());
                ps.setBigDecimal(10, rollingReturn.getTenYears());
            }

            @Override
            public int getBatchSize() {
                return rollingReturnEntities.size();
            }
        });
    }
}
