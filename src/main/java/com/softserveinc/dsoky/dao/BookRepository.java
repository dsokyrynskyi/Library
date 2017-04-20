package com.softserveinc.dsoky.dao;

import com.softserveinc.dsoky.api.Book;
import com.softserveinc.dsoky.exceptions.NoSuchLibraryResourceException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookRepository extends AbstractLibraryResourceRepository<Book> implements BookDAO {

    @Override
    public String getTableName() {
        return "\"book\"";
    }

    @Override
    public RowMapper<Book> getRowMapper() {
        return (rs, rowNum) -> new Book(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("isbn"),
                rs.getDate("publish_date").toLocalDate(),
                rs.getString("genre")
        );
    }

    @Override
    protected String getTableFields() {
        return "name, isbn, publish_date, genre";
    }

    @Override
    protected String getTableParams() {
        return ":name, :isbn, :publish_date, :genre";
    }

    @Override
    protected SqlParameterSource parseSqlParams(Book book) {
        return new MapSqlParameterSource(
                "name", book.getName())
                .addValue("id", book.getId())
                .addValue("isbn", book.getIsbn())
                .addValue("publish_date", book.getPublishDate())
                .addValue("genre", book.getGenre()
                );
    }

    @Override
    protected String getTableFieldsWithParams() {
        return "name = :name, isbn = :isbn, publish_date = :publish_date, genre = :genre";
    }


    @Override
    public List<Book> getByAuthor(long authorId) {
        final String sql = "select * from book \n" +
                "left join books_authors on book.id = books_authors.book_id\n" +
                "where author_id = :authorId";
        SqlParameterSource param = new MapSqlParameterSource("authorId", authorId);
        List<Book> books = jdbcTemplate.query(sql, param, getRowMapper());
        if (books.isEmpty())
            throw new NoSuchLibraryResourceException("There are no books for this author!");
        return books;
    }

    @Override
    public List<Book> getByPublisher(long id) {
        final String sql = "select * from book \n" +
                "where publisher = :publisherId";
        SqlParameterSource param = new MapSqlParameterSource("publisherId", id);
        List<Book> books = jdbcTemplate.query(sql, param, getRowMapper());
        if (books.isEmpty())
            throw new NoSuchLibraryResourceException("There are no books for this publisher!");
        return books;
    }

    @Override
    public Book getByName(String name) {
        final String sql = "select * from \"book\"\n" +
                "where name = :name";
        SqlParameterSource param = new MapSqlParameterSource("name", name);
        Book book;
        try{
            book = jdbcTemplate.queryForObject(sql, param, getRowMapper());
        }catch (EmptyResultDataAccessException e){
            throw new NoSuchLibraryResourceException("There is no book with this title!");
        }
        return book;
    }

    @Override
    public void removeFromBooksAuthors(long bookId, long authorId){
        final String sql = "delete from books_authors\n" +
                "where book_id = :bookId AND author_id = :authorId";
        SqlParameterSource param = new MapSqlParameterSource("bookId", bookId)
                .addValue("authorId", authorId);
        jdbcTemplate.update(sql, param);
    }
}
