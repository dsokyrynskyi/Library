package com.softserveinc.dsoky.resources;

import com.softserveinc.dsoky.dto.BookDTO;
import com.softserveinc.dsoky.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * GET     /books/                - get all books
 * POST    /books/                - add new book
 * PUT     /books/{id}            - update existing book
 * DELETE  /books/{id}            - remove existing book
 * <p>
 * GET     /books/{id}            - get book with certain id
 * GET     /books/author?id={id}     - get books of certain author
 * GET     /books/title?name={name}    - get book with certain title
 */

@Component
@Path("v1/")
public class BooksResource {

    private final BookService bookService;

    @Autowired
    public BooksResource(BookService bookService) {
        this.bookService = bookService;
    }

    @GET
    @Path("/books/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response fetchAll() {
        return Response.ok(bookService.getAllBookDTOs())
                .link("http://localhost:8080/authors", "authors")
                .link("http://localhost:8080/publishers", "publishers")
                .build();
    }

    @GET
    @Path("/books/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response fetchBook(@PathParam("id") long id) {
        return Response.ok(bookService.getBookDTO(id))
                .link("http://localhost:8080/books/"+id+"/authors", "authors")
                .link("http://localhost:8080/books/"+id+"/publisher", "publisher")
                .build();
    }

    @POST
    @Path("/books/")
    @Consumes(MediaType.APPLICATION_JSON)
    public void saveBook(BookDTO book) {
        bookService.saveDTO(book);
    }

    @PUT
    @Path("/books/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void updateBook(BookDTO bookDTO) {
        bookService.update(bookDTO);
    }

    @DELETE
    @Path("/books/{id}")
    public void removeBook(@PathParam("id") long id) {
        bookService.remove(id);
    }

    /*@GET
    @Path("/authors/books")
    @Produces(MediaType.APPLICATION_JSON)
    public List<BookDTO> fetchByAuthor(@QueryParam("id") long authorId) {
        return bookService.getDTOByAuthor(authorId);
    }

    @GET
    @Path("title")
    @Produces(MediaType.APPLICATION_JSON)
    public BookDTO fetchByName(@QueryParam("name") String name) throws NoSuchBookException {
        return bookService.getDTOByName(name);
    }*/

    @GET
    @Path("/authors/{id}/books")
    @Produces(MediaType.APPLICATION_JSON)
    public List<BookDTO> getBooksOfAuthor(@PathParam("id") long id){
        return bookService.getDTOByAuthor(id);
    }
}
