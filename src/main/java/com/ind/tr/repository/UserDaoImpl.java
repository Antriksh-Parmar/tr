package com.ind.tr.repository;

import com.ind.tr.repository.model.UserReadEntity;
import com.ind.tr.repository.model.UserWriteEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserDaoImpl implements UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void saveGuestUser(UserWriteEntity userWriteEntity) {
        String query = QueryBuilder.builder()
                .setQueryType(QueryType.INSERT)
                .setSchema("pf")
                .setTableName("users")
                .setParameters("id")
                .setValues(userWriteEntity.getId().toString())
                .build();
        jdbcTemplate.execute(query);
    }

    @Override
    public UserReadEntity getUser(UUID uuid) {
        String query = QueryBuilder.builder().setQueryType(QueryType.SELECT).setSchema("pf").setTableName("users").setParameters("*").build();
        return jdbcTemplate.queryForObject(query, new RowMapper<UserReadEntity>() {
            @Override
            public UserReadEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new UserReadEntity(
                        UUID.fromString(rs.getString("id")),
                        Optional.ofNullable(rs.getString("first_name")),
                        Optional.ofNullable(rs.getString("last_name")),
                        Optional.ofNullable(rs.getString("email")),
                        Optional.ofNullable(rs.getString("pswd_hash"))
                );
            }
        });
    }
}


