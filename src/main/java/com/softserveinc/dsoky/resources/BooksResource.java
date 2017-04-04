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
 * book/all
 * book/{id}
 * book/byName?name={name}
 * book/byAuthor?author={author}
 *
 * PUT
 * book
 * */

@Component
@Path("/book/")
@Produces(MediaType.APPLICATION_JSON)
public class BooksResource {

    private final BookDAO bookDAO;

    @Autowired
    public BooksResource(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    @GET
    @Path("{id}")
    public Book fetchBook(@PathParam("id") long id){
        return bookDAO.get(id);
    }

    @GET
    @Path("all")
    public List<Book> fetchAll(){
        return (List<Book>) bookDAO.getAll();
    }

    @GET
    @Path("byAuthor")
    public List<Book> fetchByAuthor(@QueryParam("author") String author){
        return (List<Book>) bookDAO.getByAuthor(author);
    }

    @GET
    @Path("byName")
    public Book fetchByName(@QueryParam("name") String name){
        return bookDAO.getByName(name);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void saveBook(Book book){
        bookDAO.save(book);
    }
}
