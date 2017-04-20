package com.softserveinc.dsoky.dao;

import com.softserveinc.dsoky.api.Author;
import com.softserveinc.dsoky.exceptions.NoSuchLibraryResourceException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AuthorRepository extends AbstractLibraryResourceRepository<Author> implements AuthorDAO {
    @Override
    public String getTableName() {
        return "\"author\"";
    }

    @Override
    public RowMapper<Author> getRowMapper() {
        return (rs, rowNum) -> new Author(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("country"),
                rs.getDate("birth_date").toLocalDate()
        );
    }

    @Override
    protected String getTableParams() {
        return ":name, :birth_date, :country";
    }

    @Override
    protected String getTableFields() {
        return "name, birth_date, country";
    }

    @Override
    protected SqlParameterSource parseSqlParams(Author entity) {
        return new MapSqlParameterSource("name", entity.getName())
                .addValue("id", entity.getId())
                .addValue("birth_date", entity.getDate())
                .addValue("country", entity.getCountry());
    }

    @Override
    protected String getTableFieldsWithParams() {
        return "name = :name, birth_date = :birth_date, country = :country";
    }

    /** from interface**/

    @Override
    public List<Author> getByBook(long bookId) {
        String sql = "select * from author \n" +
                "left join books_authors on author.id = books_authors.author_id\n" +
                "where book_id = :bookId";
        SqlParameterSource param = new MapSqlParameterSource("bookId", bookId);
        List<Author> authors = jdbcTemplate.query(sql, param, getRowMapper());
        if(authors.isEmpty())
            throw new NoSuchLibraryResourceException("There are no authors for this book!");
        return authors;
    }
}
