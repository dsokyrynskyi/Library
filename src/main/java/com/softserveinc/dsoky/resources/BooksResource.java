package com.softserveinc.dsoky.resources;

import com.softserveinc.dsoky.api.Book;
import com.softserveinc.dsoky.dao.BookDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * GET
 * books
 * books/{id}
 * books/title/{name}
 * books/author/{name}
 *
 * PUT
 * books
 */

@Component
@Path("/books/")
public class BooksResource {

    private final BookDAO bookDAO;

    @Autowired
    public BooksResource(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Book> fetchAll() {
        return (List<Book>) bookDAO.getAll();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Book fetchBook(@PathParam("id") long id) {
        return bookDAO.get(id);
    }

    @GET
    @Path("author/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Book> fetchByAuthor(@PathParam("name") String author) {
        return (List<Book>) bookDAO.getByAuthor(author);
    }

    @GET
    @Path("title/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Book fetchByName(@PathParam("name") String name) {
        return bookDAO.getByName(name);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void saveBook(Book book) {
        bookDAO.save(book);
    }
}
