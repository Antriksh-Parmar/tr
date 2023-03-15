package com.ind.tr.repository;

import com.ind.tr.repository.model.MutualFundNavEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class MutualFundsNavDaoImpl implements MutualFundsNavDao {

    private final Logger logger = LoggerFactory.getLogger(MutualFundsNavDaoImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MutualFundNavRowMapper rowMapper;


    @Override
    public List<MutualFundNavEntity> queryMutualFundsNavs() {
        String query = "SELECT * FROM fi.mutual_fund_navs";
        long start = System.currentTimeMillis();
        List<MutualFundNavEntity> result = jdbcTemplate.query(query, rowMapper);
        long end = System.currentTimeMillis();
        logger.debug("Meter - queryAllMutualFundsNavs - ms: " + (end - start));
        return result;
    }

    @Override
    public List<MutualFundNavEntity> queryMutualFundsNavs(List<String> fundIds) {
        String query = "SELECT * FROM fi.mutual_fund_navs WHERE mf_id IN (";
        List<String> placeholders = new ArrayList<>();
        for (int i = 0; i < fundIds.size(); i++) {
            placeholders.add("?");
        }
        query += String.join(",", placeholders) + ")";
        return jdbcTemplate.query(query, rowMapper, fundIds.toArray());
    }
}

@Component
class MutualFundNavRowMapper implements RowMapper<MutualFundNavEntity> {

    @Override
    public MutualFundNavEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new MutualFundNavEntity(
                UUID.fromString(rs.getString("mf_id")),
                rs.getString("isin"),
                rs.getBigDecimal("nav"),
                rs.getDate("nav_date")
        );
    }
}