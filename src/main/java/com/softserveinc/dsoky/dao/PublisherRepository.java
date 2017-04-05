package com.softserveinc.dsoky.dao;

import com.softserveinc.dsoky.api.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class PublisherRepository implements PublisherDAO {

    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<Publisher> getAll() {
        final String sql = "SELECT * FROM \"Publisher\"";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Publisher(
                rs.getInt("publisher_id"),
                rs.getString("name"),
                rs.getString("country")
        ));
    }

    @Override
    public Publisher get(long id) {
        final String sql = "SELECT * FROM \"Publisher\" where publisher_id = :id";
        SqlParameterSource param = new MapSqlParameterSource("id", id);
        return jdbcTemplate.queryForObject(sql, param, (rs, rowNum) ->
                new Publisher(
                        rs.getInt("publisher_id"),
                        rs.getString("name"),
                        rs.getString("country")
        ));
    }

    @Override
    public Publisher getByBook(String name) {
        final String sql = "select * from \"Publisher\"\n" +
                "inner join \"Book\" on \"Book\".publisher = publisher_id \n" +
                "where \"Book\".name = :bookName";
        SqlParameterSource param = new MapSqlParameterSource("bookName", name);
        return jdbcTemplate.queryForObject(sql, param, (rs, rowNum) ->
                new Publisher(
                        rs.getInt("publisher_id"),
                        rs.getString("name"),
                        rs.getString("country")
        ));
    }

    @Override
    public void save(Publisher publisher) {
        final String sql = "INSERT INTO \"Publisher\" (name, country) VALUES (:p_name, :p_country)";
        SqlParameterSource params = new MapSqlParameterSource("p_name", publisher.getName())
                .addValue("p_country", publisher.getCountry());
        jdbcTemplate.update(sql, params);
    }

    @Override
    public void remove(long id) {
        final String sql = "DELETE FROM \"Publisher\" WHERE publisher_id = :id";
        SqlParameterSource param = new MapSqlParameterSource("id", id);
        jdbcTemplate.update(sql, param);
    }

    @Override
    public void update(Publisher publisher) {
        final String sql = "UPDATE \"Publisher\" set name = :name, country = :country WHERE publisher_id = :id";
        SqlParameterSource params = new MapSqlParameterSource("name", publisher.getName())
                .addValue("id", publisher.getId())
                .addValue("country", publisher.getCountry());
        jdbcTemplate.update(sql, params);
    }
}
