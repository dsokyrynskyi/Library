package com.softserveinc.dsoky.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public abstract class AbstractLibraryResourceRepository<T> implements LibraryResourceDAO<T>{

    protected NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public abstract RowMapper<T> getRowMapper();
    public abstract String getTableName();

    @Override
    public List<T> getAll() {
        String sql = "SELECT * FROM " + getTableName();
        return jdbcTemplate.query(sql, getRowMapper());
    }

    @Override
    public T get(long id) {
        String sql = "SELECT * FROM " + getTableName() + " WHERE " + getTableName() + ".id = :id";
        SqlParameterSource param = new MapSqlParameterSource("id", id);
        return jdbcTemplate.queryForObject(sql, param, getRowMapper());
    }

    @Override
    public void save(T entity) {
        String sql = "INSERT INTO "+ getTableName()+" (" + getTableFields() + ") VALUES ("+ getTableParams() +")";
        SqlParameterSource param = parseSqlParams(entity);
        jdbcTemplate.update(sql, param);
    }

    @Override
    public void remove(long id) {
        String sql = "DELETE FROM " + getTableName() + " WHERE id = :id";
        SqlParameterSource param = new MapSqlParameterSource("id", id);
        jdbcTemplate.update(sql, param);
    }

    protected abstract String getTableParams();
    protected abstract String getTableFields();
    protected abstract SqlParameterSource parseSqlParams(T entity);

    public abstract void update(T entity);
}
