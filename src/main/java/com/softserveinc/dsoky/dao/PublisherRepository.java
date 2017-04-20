package com.softserveinc.dsoky.dao;

import com.softserveinc.dsoky.api.Publisher;
import com.softserveinc.dsoky.exceptions.NoSuchLibraryResourceException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

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
                .addValue("id", publisher.getId())
                .addValue("country", publisher.getCountry()
                );
    }

    @Override
    protected String getTableFieldsWithParams() {
        return "name = :name, country = :country";
    }

    /*** from interface */

    @Override
    public Publisher getByBook(long id) {
        String sql = "select * from publisher \n" +
                "left join book on publisher.id = book.publisher\n" +
                "where book.id = :bookId";
        SqlParameterSource param = new MapSqlParameterSource("bookId", id);
        Publisher publisher;
        try {
            publisher = jdbcTemplate.queryForObject(sql, param, getRowMapper());
        }catch (EmptyResultDataAccessException e){
            throw new NoSuchLibraryResourceException("There is no publisher for this book!");
        }
        return publisher;
    }
}
