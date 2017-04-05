package com.softserveinc.dsoky.dao;

import com.softserveinc.dsoky.api.Author;
import com.softserveinc.dsoky.api.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class BookRepository implements BookDAO {

    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<Book> getAll() {
        final String sql = "SELECT * FROM \"Book\"";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Book(
                rs.getInt("book_id"),
                rs.getString("name"),
                rs.getString("isbn"),
                rs.getDate("publish_date").toLocalDate(),
                rs.getString("genre")));
    }

    @Override
    public List<Book> getByAuthor(String author) {
        final String sql = "SELECT * FROM \"Book\" \n" +
                "INNER JOIN \"Books_Authors\" ON \"Books_Authors\".book_id = \"Book\".book_id\n" +
                "INNER JOIN \"Author\" ON \"Author\".author_id = \"Books_Authors\".author_id\n" +
                "WHERE \"Author\".name = :author";
        SqlParameterSource param = new MapSqlParameterSource("author", author);
        return jdbcTemplate.query(sql, param, (rs, rowNum) -> new Book(
                rs.getInt("book_id"),
                rs.getString("name"),
                rs.getString("isbn"),
                rs.getDate("publish_date").toLocalDate(),
                rs.getString("genre")));
    }

    @Override
    public Book get(long id) {
        final String sql = "SELECT * FROM \"Book\" WHERE book_id = :id";
        SqlParameterSource param = new MapSqlParameterSource("id", id);
        return jdbcTemplate.queryForObject(sql, param, (rs, rn) -> new Book(
                rs.getInt("book_id"),
                rs.getString("name"),
                rs.getString("isbn"),
                rs.getDate("publish_date").toLocalDate(),
                rs.getString("genre")));
    }

    @Override
    public Book getByName(String name) {
        final String sql = "SELECT * FROM \"Book\" WHERE name = :name";
        SqlParameterSource param = new MapSqlParameterSource("name", name);
        return jdbcTemplate.queryForObject(sql, param, (rs, rn) -> new Book(
                rs.getInt("book_id"),
                rs.getString("name"),
                rs.getString("isbn"),
                rs.getDate("publish_date").toLocalDate(),
                rs.getString("genre")));
    }

    @Override
    @Transactional
    public void save(Book book) {
        final String sqlBook = "INSERT INTO \"Book\" (name, isbn, publish_date) VALUES (:book_name, :book_isbn, :book_date);";
        final String sqlAuth = "INSERT INTO \"Author\" (name, birth_date, country) VALUES (:auth_name, :auth_birth, :auth_country)";
        final String sqlBooksAuthors = "INSERT INTO \"Books_Authors\" VALUES (:book_id, :auth_id)";
        long bookId = insertBookFields(book, sqlBook);
        if(book.getAuthors() != null)
            book.getAuthors()
                    .stream()
                    .map(author -> insertAuthorFields(author, sqlAuth))
                    .forEach(authId -> insertBooksAuthors(sqlBooksAuthors, bookId, authId));
    }

    private void insertBooksAuthors(String sqlBooksAuthors, long bookId, long authId) {
        SqlParameterSource paramsBook = new MapSqlParameterSource("book_id", bookId)
                .addValue("auth_id", authId);
        jdbcTemplate.update(sqlBooksAuthors, paramsBook);
    }

    private long insertBookFields(Book book, String sqlBook) {
        SqlParameterSource paramsBook = new MapSqlParameterSource("book_name", book.getName())
                .addValue("book_isbn", book.getIsbn())
                .addValue("book_date", book.getPublishDate());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sqlBook, paramsBook, keyHolder, new String[]{"book_id"});
        return keyHolder.getKey().longValue();
    }

    private long insertAuthorFields(Author author, String sqlAuthor) {
        if (checkIfExists(author)) return author.getId();
        SqlParameterSource paramsBook = new MapSqlParameterSource("auth_name", author.getName())
                .addValue("auth_birth", author.getDate())
                .addValue("auth_country", author.getCountry());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sqlAuthor, paramsBook, keyHolder, new String[]{"author_id"});
        return keyHolder.getKey().longValue();
    }

    private boolean checkIfExists(Author author) {
        final String sql = "select * from \"Author\" where name = :name";
        Author a = jdbcTemplate.queryForObject(sql, new MapSqlParameterSource("name", author.getName()), (rs, rowNum) -> new Author(rs.getInt("id")));
        return a.getId() == 0;
    }

    @Override
    public void update(Book book) {
        final String sql = "UPDATE \"Book\" set name = :name, isbn = :isbn, publish_date = :date, genre = :genre WHERE book_id = :id";
        SqlParameterSource params = new MapSqlParameterSource("name", book.getName())
                .addValue("id", book.getId())
                .addValue("isbn", book.getIsbn())
                .addValue("genre", book.getGenre())
                .addValue("date", book.getPublishDate());
        jdbcTemplate.update(sql, params);
    }

    @Override
    public void remove(long id) {
        final String sqlBooksAuthors = "DELETE FROM \"Books_Authors\" WHERE book_id = :id";
        final String sqlBook = "DELETE FROM \"Book\" WHERE book_id = :id";
        SqlParameterSource param = new MapSqlParameterSource("id", id);
        jdbcTemplate.update(sqlBooksAuthors, param);
        jdbcTemplate.update(sqlBook, param);
    }
}
