package com.softserveinc.dsoky.dao;

import com.softserveinc.dsoky.exceptions.CreateResourceException;
import com.softserveinc.dsoky.exceptions.DeleteResourceException;
import com.softserveinc.dsoky.exceptions.NoSuchLibraryResourceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
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
    protected abstract String getTableFieldsWithParams();
    protected abstract String getTableParams();
    protected abstract String getTableFields();
    protected abstract SqlParameterSource parseSqlParams(T entity);

    @Override
    public List<T> getAll() {
        String sql = "SELECT * FROM " + getTableName();
        return jdbcTemplate.query(sql, getRowMapper());
    }

    @Override
    public T get(long id) {
        String sql = "SELECT * FROM " + getTableName() + " WHERE " + getTableName() + ".id = :id";
        SqlParameterSource param = new MapSqlParameterSource("id", id);
        T entity;
        try {
            entity = jdbcTemplate.queryForObject(sql, param, getRowMapper());
        }catch (EmptyResultDataAccessException e){
            throw new NoSuchLibraryResourceException("There is no resource with this id!");
        }
        return entity;
    }

    @Override
    public void save(T entity) {
        String sql = "INSERT INTO "+ getTableName()+" (" + getTableFields() + ") VALUES ("+ getTableParams() +")";
        SqlParameterSource param = parseSqlParams(entity);
        try {
            jdbcTemplate.update(sql, param);
        }catch (DuplicateKeyException e){
            throw new CreateResourceException("It's impossible to create resource with these fields!");
        }
    }

    @Override
    public void remove(long id) {
        String sql = "DELETE FROM " + getTableName() + " WHERE id = :id";
        SqlParameterSource param = new MapSqlParameterSource("id", id);
        try {
            jdbcTemplate.update(sql, param);
        }catch (DataIntegrityViolationException e){
            throw new DeleteResourceException("Foreign key violation");
        }
    }

    @Override
    public void update(T entity) {
        String sql = "UPDATE " + getTableName() + " SET " + getTableFieldsWithParams() + " WHERE " + getTableName() + ".id = :id";
        SqlParameterSource param = parseSqlParams(entity);
        try {
            jdbcTemplate.update(sql, param);
        }catch (DuplicateKeyException e){
            throw new CreateResourceException("It's impossible to update resource with these fields!");
        }
    }
}
