package com.softserveinc.dsoky.dao;

import com.softserveinc.dsoky.api.Publisher;
import com.softserveinc.dsoky.exceptions.NoSuchLibraryResourceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class PublisherRepository extends AbstractLibraryResourceRepository<Publisher> implements PublisherDAO {
    @Override
    public String getTableName() {
        return "\"publisher\"";
    }

    @Override
    public RowMapper<Publisher> getRowMapper() {
        return (rs, rowNum) -> new Publisher(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("country")
        );
    }

    @Override
    protected String getTableFields() {
        return "name, country";
    }

    @Override
    protected String getTableParams() {
        return ":name, :country";
    }

    @Override
    protected SqlParameterSource parseSqlParams(Publisher publisher) {
        return new MapSqlParameterSource("name", publisher.getName())
                .addValue("country", publisher.getCountry()
                );
    }

    @Override
    public Publisher getByBook(long id) {
        return null;
    }

    @Override
    public void update(Publisher entity) {

    }

    /*private NamedParameterJdbcTemplate jdbcTemplate;
    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<Publisher> getAll() {
        final String sql = "SELECT * FROM \"publisher\"";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Publisher(
                rs.getInt("publisher_id"),
                rs.getString("name"),
                rs.getString("country")
        ));
    }

    @Override
    public Publisher get(long id) {
        final String sql = "SELECT * FROM \"publisher\" where publisher_id = :id";
        SqlParameterSource param = new MapSqlParameterSource("id", id);
        Publisher publisher;
        try {
            publisher = jdbcTemplate.queryForObject(sql, param, (rs, rowNum) ->
                    new Publisher(
                            rs.getInt("publisher_id"),
                            rs.getString("name"),
                            rs.getString("country")));
        }catch (EmptyResultDataAccessException e){
            throw new NoSuchLibraryResourceException("There isn't a publisher in the Library with ID = "+id);
        }
        return publisher;
    }

    @Override
    public Publisher getByBook(long id) {
        final String sql = "select * from \"publisher\"\n" +
                "inner join \"book\" on \"book\".publisher = \"publisher\".publisher_id  \n" +
                "where \"book\".book_id = :id";
        SqlParameterSource param = new MapSqlParameterSource("id", id);
        Publisher publisher;
        try {
            publisher = jdbcTemplate.queryForObject(sql, param, (rs, rowNum) ->
                    new Publisher(
                            rs.getInt("publisher_id"),
                            rs.getString("name"),
                            rs.getString("country")));
        }catch (EmptyResultDataAccessException e){
            throw new NoSuchLibraryResourceException("There isn't a publisher in the Library with BOOK_ID = "+id);
        }
        return publisher;
    }

    @Override
    public void save(Publisher publisher) {
        final String sql = "INSERT INTO \"publisher\" (name, country) VALUES (:p_name, :p_country)";
        SqlParameterSource params = new MapSqlParameterSource("p_name", publisher.getName())
                .addValue("p_country", publisher.getCountry());
        jdbcTemplate.update(sql, params);
    }

    @Override
    public void remove(long id) {
        final String sql = "DELETE FROM \"publisher\" WHERE publisher_id = :id";
        SqlParameterSource param = new MapSqlParameterSource("id", id);
        jdbcTemplate.update(sql, param);
    }

    @Override
    public void update(Publisher publisher) {
        final String sql = "UPDATE \"publisher\" set name = :name, country = :country WHERE publisher_id = :id";
        SqlParameterSource params = new MapSqlParameterSource("name", publisher.getName())
                .addValue("id", publisher.getId())
                .addValue("country", publisher.getCountry());
        jdbcTemplate.update(sql, params);
    }*/
}
