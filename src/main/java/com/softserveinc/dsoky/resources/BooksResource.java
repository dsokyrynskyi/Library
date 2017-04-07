package com.softserveinc.dsoky.resources;

import com.softserveinc.dsoky.dto.BookDTO;
import com.softserveinc.dsoky.exceptions.NoSuchBookException;
import com.softserveinc.dsoky.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 GET     /books/
 POST    /books/
 PUT     /books/
 GET     /books/author/{id}
 GET     /books/title/{name}
 DELETE  /books/{id}
 GET     /books/{id}
 */

@Component
@Path("/books/")
public class BooksResource {

    private final BookService bookService;

    @Autowired
    public BooksResource(BookService bookService) {
        this.bookService = bookService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<BookDTO> fetchAll() {
        return bookService.getAllBookDTOs();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public BookDTO fetchBook(@PathParam("id") long id) throws NoSuchBookException {
        return bookService.getBookDTO(id);
    }

    @GET
    @Path("author/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<BookDTO> fetchByAuthor(@PathParam("id") long authorId) {
        return bookService.getDTOByAuthor(authorId);
    }

    @GET
    @Path("title/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public BookDTO fetchByName(@PathParam("name") String name) throws NoSuchBookException {
        return bookService.getDTOByName(name);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void saveBook(BookDTO book) {
        bookService.saveDTO(book);
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void updateBook(BookDTO bookDTO) {
        bookService.update(bookDTO);
    }

    @DELETE
    @Path("{id}")
    public void removeBook(@PathParam("id") long id) {
        bookService.remove(id);
    }
}
