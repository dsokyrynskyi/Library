package com.softserveinc.dsoky.dao;

import com.softserveinc.dsoky.api.Author;
import com.softserveinc.dsoky.api.Book;
import com.softserveinc.dsoky.api.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Repository
public class BookJDBCTemplate implements BookDAO{

    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public Collection<Book> getAll() {
        final String sql = "select * from \"Book\"";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Book(
                                            rs.getInt("book_id"),
                                            rs.getString("name"),
                                            rs.getString("isbn"),
                                            rs.getDate("publish_date").toLocalDate(),
                                            rs.getString("genre")));
    }

    @Override
    public Collection<Book> getByAuthor(String author) {
        final String sql = "select \"Book\".book_id, \"Book\".name, \"Book\".isbn, \"Book\".publish_date, \"Genre\".name AS \"genre\", " +
                            "\"Publisher\".name AS \"publisher\", \"Publisher\".country AS \"country\", \"Publisher\".publisher_id AS \"p_id\" from \"Book\"\n" +
                            "inner join \"Books_Authors\" on \"Books_Authors\".book_id = \"Book\".book_id "+
                            "inner join \"Author\" on \"Books_Authors\".author_id = \"Author\".author_id "+
                            "inner join \"Genre\" on \"Genre\".genre_id = \"Book\".genre\n" +
                            "inner join \"Publisher\" on \"Publisher\".publisher_id = \"Book\".publisher\n" +
                            "where \"Author\".name = :author";
        SqlParameterSource param = new MapSqlParameterSource("author", author);
        return jdbcTemplate.query(sql, param, (rs, rn) -> new Book(
                                                            rs.getInt("book_id"),
                                                            rs.getString("name"),
                                                            rs.getString("isbn"),
                                                            rs.getDate("publish_date").toLocalDate(),
                                                            rs.getString("genre"),
                                                            new Publisher(rs.getInt("p_id"), rs.getString("publisher"), rs.getString("country"))));
    }

    @Override
    public Book get(long id) {
        final String sql_book = "select \"Book\".book_id, \"Book\".name, \"Book\".isbn, " +
                                "\"Book\".publish_date, \"Genre\".name AS \"genre\", " +
                                "\"Publisher\".name AS \"publisher\", \"Publisher\".country AS \"country\", \"Publisher\".publisher_id AS \"p_id\" from \"Book\"\n" +
                                "inner join \"Genre\" on \"Genre\".genre_id = \"Book\".genre\n" +
                                "inner join \"Publisher\" on \"Publisher\".publisher_id = \"Book\".publisher\n" +
                                "where \"Book\".book_id = :id";
        final String sql_auth = "select * from \"Author\"\n inner join \"Books_Authors\"\n " +
                                "on \"Books_Authors\".author_id = \"Author\".author_id\n " +
                                "where \"Books_Authors\".book_id = :id";
        SqlParameterSource param = new MapSqlParameterSource("id", id);
        List<Author> authors = jdbcTemplate.query(sql_auth, param, (rs, rowNum) -> new Author(
                                                                rs.getInt("author_id"),
                                                                rs.getString("name"),
                                                                rs.getString("country"),
                                                                rs.getDate("birth_date").toLocalDate()));
        Book book = jdbcTemplate.queryForObject(sql_book, param, (rs, rn) -> new Book(
                                                                rs.getInt("book_id"),
                                                                rs.getString("name"),
                                                                rs.getString("isbn"),
                                                                rs.getDate("publish_date").toLocalDate(),
                                                                rs.getString("genre"),
                                                                new Publisher(rs.getInt("p_id"), rs.getString("publisher"), rs.getString("country"))));
        book.setAuthors(authors);
        return book;
    }

    @Override
    public Book getByName(String name) {
        final String sql_book = "select \"Book\".book_id, \"Book\".name, \"Book\".isbn, " +
                                "\"Book\".publish_date, \"Genre\".name AS \"genre\", " +
                                "\"Publisher\".name AS \"publisher\", \"Publisher\".country AS \"country\", \"Publisher\".publisher_id AS \"p_id\" from \"Book\"\n" +
                                "inner join \"Genre\" on \"Genre\".genre_id = \"Book\".genre\n" +
                                "inner join \"Publisher\" on \"Publisher\".publisher_id = \"Book\".publisher\n" +
                                "where \"Book\".name = :name";
        final String sql_auth = "select * from \"Author\"\n" +
                                "inner join \"Books_Authors\" on \"Books_Authors\".author_id = \"Author\".author_id\n" +
                                "inner join \"Book\" on \"Books_Authors\".book_id = \"Book\".book_id\n" +
                                "where \"Book\".name = :name";
        SqlParameterSource param = new MapSqlParameterSource("name", name);
        List<Author> authors = jdbcTemplate.query(sql_auth, param, (rs, rowNum) -> new Author(
                rs.getInt("author_id"),
                rs.getString("name"),
                rs.getString("country"),
                rs.getDate("birth_date").toLocalDate()));
        Book book = jdbcTemplate.queryForObject(sql_book, param, (rs, rn) -> new Book(
                rs.getInt("book_id"),
                rs.getString("name"),
                rs.getString("isbn"),
                rs.getDate("publish_date").toLocalDate(),
                rs.getString("genre"),
                new Publisher(rs.getInt("p_id"), rs.getString("publisher"), rs.getString("country"))));
        book.setAuthors(authors);
        return book;
    }

    @Override
    public void save(Book book) {
        final String sql_book = "insert into \"Book\" (name, isbn, publish_date) \n" +
                                "values (:book_name, :book_isbn, :book_date);";
        final String sql_auth = "insert into \"Author\" (name, birth_date, country) \n" +
                                "values (:auth_name, :auth_birth, :auth_country)";
        final String sql_books_authors = "insert into \"Books_Authors\" values (:book_id, :auth_id)";
        long bookId = insertBookFields(book, sql_book);
        long authId = insertAuthorFields(book, sql_auth);
        insertBooksAuthors(sql_books_authors, bookId, authId);
    }

    private void insertBooksAuthors(String sql_books_authors, long bookId, long authId) {
        SqlParameterSource paramsBook = new MapSqlParameterSource("book_id", bookId)
                                                                .addValue("auth_id", authId);
        jdbcTemplate.update(sql_books_authors, paramsBook);
    }

    private long insertBookFields(Book book, String sql_book) {
        SqlParameterSource paramsBook = new MapSqlParameterSource("book_name", book.getName())
                                                                .addValue("book_isbn", book.getIsbn())
                                                                .addValue("book_date", book.getPublishDate());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql_book, paramsBook, keyHolder, new String[]{"book_id"});
        return keyHolder.getKey().longValue();
    }
    private long insertAuthorFields(Book book, String sql_auth) {
        SqlParameterSource paramsBook = new MapSqlParameterSource("auth_name", "Billy Wong")
                .addValue("auth_birth", LocalDate.now())
                .addValue("auth_country", "Island");
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql_auth, paramsBook, keyHolder, new String[]{"author_id"});
        return keyHolder.getKey().longValue();
    }

    @Override
    public void update(Book book) {
        System.out.printf("update");
    }

    @Override
    public void remove(long id) {
        System.out.printf("remove");
    }
}
