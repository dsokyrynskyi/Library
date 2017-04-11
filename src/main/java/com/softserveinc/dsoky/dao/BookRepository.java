package com.softserveinc.dsoky.dao;

import com.softserveinc.dsoky.api.Author;
import com.softserveinc.dsoky.api.Book;
import com.softserveinc.dsoky.api.Publisher;
import com.softserveinc.dsoky.exceptions.NoSuchBookException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BookRepository implements BookDAO {

    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<Book> getAll() {
        final String sql = "select * from \"Book\" \n" +
                "left join \"Publisher\" on \"Book\".publisher = \"Publisher\".publisher_id\n" +
                "left join \"Books_Authors\" on \"Books_Authors\".book_id = \"Book\".book_id";
        return getWithoutCartesianProduct(sql, new MapSqlParameterSource());
    }

    @Override
    public Book get(long id) throws NoSuchBookException {
        final String sql = "select * from \"Book\" \n" +
                "left join \"Publisher\" on \"Book\".publisher = \"Publisher\".publisher_id\n" +
                "left join \"Books_Authors\" on \"Books_Authors\".book_id = \"Book\".book_id\n" +
                "where \"Book\".book_id = :id";
        SqlParameterSource param = new MapSqlParameterSource("id", id);
        List<Book> books = getWithoutCartesianProduct(sql, param);
        if (books.isEmpty())
            throw new NoSuchBookException("There are not any books with ID = " + id);
        return books.get(0);
    }

    @Override
    public List<Book> getByAuthor(long id) {
        final String sql = "select * from \"Book\"\n" +
                "left join \"Publisher\" on \"Book\".publisher = \"Publisher\".publisher_id\n" +
                "left join \"Books_Authors\" on \"Books_Authors\".book_id = \"Book\".book_id\n" +
                "where \"Book\".book_id IN\n" +
                "(select \"Books_Authors\".book_id from \"Books_Authors\"\n" +
                "where author_id = :authorId)";
        SqlParameterSource param = new MapSqlParameterSource("authorId", id);
        List<Book> books = getWithoutCartesianProduct(sql, param);
        if (books.isEmpty())
            throw new NoSuchBookException("There are not any books with AUTHOR ID = " + id);
        return books;
    }

    @Override
    public List<Book> getByPublisher(long id) {
        final String sql = "select * from \"Book\" \n" +
                "where publisher = :publisherId";
        SqlParameterSource param = new MapSqlParameterSource("publisherId", id);
        List<Book> books =  jdbcTemplate.query(sql, param, (rs, rowNum) -> new Book(
                rs.getLong("book_id"),
                rs.getString("name"),
                rs.getString("isbn"),
                rs.getDate("publish_date").toLocalDate(),
                rs.getString("genre")));
        if (books.isEmpty())
            throw new NoSuchBookException("There are not any books with PUBLISHER ID = " + id);
        return books;
    }

    private List<Book> getWithoutCartesianProduct(String sql, SqlParameterSource param) {
        return jdbcTemplate.query(sql, param, rs -> {
            Map<Long, Book> bookMap = new HashMap<>();
            Book book;
            while (rs.next()) {
                long bookId = rs.getLong("book_id");
                book = bookMap.get(bookId);
                if (book == null) {
                    book = new Book(
                            rs.getLong("book_id"),
                            rs.getString("name"),
                            rs.getString("isbn"),
                            rs.getDate("publish_date").toLocalDate(),
                            rs.getString("genre"),
                            new Publisher(rs.getLong("publisher"))
                    );
                    bookMap.put(bookId, book);
                }
                long authorId = rs.getLong("author_id");
                if (authorId > 0)
                    book.getAuthors().add(new Author(authorId));
            }
            return new ArrayList<>(bookMap.values());
        });
    }

    @Override
    public void remove(long id) {
        final String sqlBooksAuthors = "DELETE FROM \"Books_Authors\" WHERE book_id = :id";
        final String sqlBook = "DELETE FROM \"Book\" WHERE book_id = :id";
        SqlParameterSource param = new MapSqlParameterSource("id", id);
        jdbcTemplate.update(sqlBooksAuthors, param);
        jdbcTemplate.update(sqlBook, param);
    }

    @Override
    public void save(Book book) {
        final String sqlBook = "INSERT INTO \"Book\" (name, isbn, publish_date, genre, publisher) VALUES (:bookName, :bookIsbn, :bookDate, :bookGenre, :publisherId)";
        final String sqlBookAuth = "INSERT INTO \"Books_Authors\" VALUES (:bookId, :authId)";
        long insertedId = insertBookFields(book, sqlBook);
        book.getAuthors().stream()
                .map(Author::getId)
                .forEach(authId -> insertBooksAuthors(sqlBookAuth, insertedId, authId));
    }

    private long insertBookFields(Book book, String sqlBook) {
        SqlParameterSource paramsBook = new MapSqlParameterSource("bookName", book.getName())
                .addValue("bookIsbn", book.getIsbn())
                .addValue("bookDate", book.getPublishDate())
                .addValue("bookGenre", book.getGenre())
                .addValue("publisherId", book.getPublisher().getId());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sqlBook, paramsBook, keyHolder, new String[]{"book_id"});
        return keyHolder.getKey().longValue();
    }

    private void insertBooksAuthors(String sqlBooksAuthors, long bookId, long authId) {
        SqlParameterSource paramsBook = new MapSqlParameterSource("bookId", bookId)
                .addValue("authId", authId);
        jdbcTemplate.update(sqlBooksAuthors, paramsBook);
    }

    @Override
    public void update(Book book) {
        updateBookFields(book);
        updateBooksAuthorsFields(book);
    }

    private void updateBookFields(Book book) {
        final String sql = "UPDATE \"Book\" set name = :name, isbn = :isbn, publish_date = :date, genre = :genre, publisher = :publisher WHERE book_id = :id";
        SqlParameterSource params = new MapSqlParameterSource("name", book.getName())
                .addValue("id", book.getId())
                .addValue("isbn", book.getIsbn())
                .addValue("genre", book.getGenre())
                .addValue("publisher", book.getPublisher().getId())
                .addValue("date", book.getPublishDate());
        jdbcTemplate.update(sql, params);
    }

    private void updateBooksAuthorsFields(Book book) {
        final String delSql = "DELETE FROM \"Books_Authors\" where book_id = :bookId";
        SqlParameterSource delParam = new MapSqlParameterSource("bookId", book.getId());
        jdbcTemplate.update(delSql, delParam);
        final String insSql = "INSERT INTO \"Books_Authors\" VALUES(:bookId, :authorId)";
        book.getAuthors().stream()
                .map(Author::getId)
                .map(aLong -> new MapSqlParameterSource("bookId", book.getId()).addValue("authorId", aLong))
                .forEach(params -> jdbcTemplate.update(insSql, params));
    }

    /*@Override
    public Book getByName(String name){
        final String sql = "select * from \"Book\"\n" +
                "inner join \"Books_Authors\" on \"Books_Authors\".book_id=\"Book\".book_id\n" +
                "where \"Book\".name = :name";
        SqlParameterSource param = new MapSqlParameterSource("name", name);
        List<Book> books = getWithoutCartesianProduct(sql, param);
        if (books.isEmpty())
            throw new NoSuchBookException("There are not any books with title = " + name);
        return books.get(0);
    }*/
}
