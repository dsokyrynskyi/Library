package com.softserveinc.dsoky.dao;

import com.softserveinc.dsoky.api.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class AuthorRepository implements AuthorDAO {

    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<Author> getAll() {
        final String sql = "SELECT * FROM \"Author\"";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Author(
                rs.getInt("author_id"),
                rs.getString("name"),
                rs.getString("country"),
                rs.getDate("birth_date").toLocalDate())
        );
    }

    @Override
    public Author get(long id) {
        final String sql = "SELECT * FROM \"Author\" where author_id = :id";
        SqlParameterSource param = new MapSqlParameterSource("id", id);
        return jdbcTemplate.queryForObject(sql, param, (rs, rowNum) ->
                new Author(
                        rs.getInt("author_id"),
                        rs.getString("name"),
                        rs.getString("country"),
                        rs.getDate("birth_date").toLocalDate())
        );
    }

    @Override
    public List<Author> getByBook(String book) {
        final String sql = "select * from \"Author\" \n" +
                "inner join \"Books_Authors\" on \"Books_Authors\".author_id = \"Author\".author_id\n" +
                "inner join \"Book\" on \"Book\".book_id = \"Books_Authors\".book_id\n" +
                "where \"Book\".name = :bookName";
        SqlParameterSource param = new MapSqlParameterSource("bookName", book);
        return jdbcTemplate.query(sql, param, (rs, rowNum) ->
                new Author(
                        rs.getInt("author_id"),
                        rs.getString("name"),
                        rs.getString("country"),
                        rs.getDate("birth_date").toLocalDate())
        );
    }

    @Override
    public void save(Author author) {
        final String sql = "INSERT INTO \"Author\" (name, birth_date, country) VALUES (:auth_name, :auth_birth, :auth_country)";
        SqlParameterSource params = new MapSqlParameterSource("auth_name", author.getName())
                .addValue("auth_birth", author.getDate())
                .addValue("auth_country", author.getCountry());
        jdbcTemplate.update(sql, params);
    }

    @Override
    public void remove(long id) {
        final String sqlBooksAuthors = "DELETE FROM \"Books_Authors\" WHERE author_id = :id";
        final String sqlAuthor = "DELETE FROM \"Author\" WHERE author_id = :id";
        SqlParameterSource param = new MapSqlParameterSource("id", id);
        jdbcTemplate.update(sqlBooksAuthors, param);
        jdbcTemplate.update(sqlAuthor, param);
    }

    @Override
    public void update(Author author) {
        final String sql = "UPDATE \"Author\" set name = :name, country = :country, birth_date = :date WHERE author_id = :id";
        SqlParameterSource params = new MapSqlParameterSource("name", author.getName())
                .addValue("id", author.getId())
                .addValue("country", author.getCountry())
                .addValue("date", author.getDate());
        jdbcTemplate.update(sql, params);
    }
}