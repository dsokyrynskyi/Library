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
 GET     /books/                - get all books
 POST    /books/                - add new book
 PUT     /books/{id}            - update existing book
 DELETE  /books/{id}            - remove existing book

 GET     /books/{id}            - get book with certain id
 GET     /books/author?id={id}     - get books of certain author
 GET     /books/title?name={name}    - get book with certain title
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
    public BookDTO fetchBook(@PathParam("id") long id){
        return bookService.getBookDTO(id);
    }

    @GET
    @Path("author")
    @Produces(MediaType.APPLICATION_JSON)
    public List<BookDTO> fetchByAuthor(@QueryParam("id") long authorId) {
        return bookService.getDTOByAuthor(authorId);
    }

    @GET
    @Path("title")
    @Produces(MediaType.APPLICATION_JSON)
    public BookDTO fetchByName(@QueryParam("name") String name) throws NoSuchBookException {
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
