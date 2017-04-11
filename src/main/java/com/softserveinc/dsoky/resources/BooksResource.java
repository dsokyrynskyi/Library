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
 GET     /v1/books/
 POST    /v1/books/
 DELETE  /v1/books/{id}
 GET     /v1/books/{id}
 PUT     /v1/books/{id}
 GET     /v1/authors/{id}/books
 GET     /v1/publishers/{id}/books
 * */

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
                .link("http://localhost:8080/v1/authors", "authors")
                .link("http://localhost:8080/v1/publishers", "publishers")
                .build();
    }

    @GET
    @Path("/books/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response fetchBook(@PathParam("id") long id) {
        return Response.ok(bookService.getBookDTO(id))
                .link("http://localhost:8080/v1/books/"+id+"/authors", "authors")
                .link("http://localhost:8080/v1/books/"+id+"/publisher", "publisher")
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

    @GET
    @Path("/authors/{id}/books")
    @Produces(MediaType.APPLICATION_JSON)
    public List<BookDTO> getBooksOfAuthor(@PathParam("id") long id){
        return bookService.getDTOByAuthor(id);
    }

    @GET
    @Path("/publishers/{id}/books")
    @Produces(MediaType.APPLICATION_JSON)
    public List<BookDTO> getBooksOfPublisher(@PathParam("id") long id){
        return bookService.getDTOByPublisher(id);
    }

    @GET
    @Path("/books/byName")
    @Produces(MediaType.APPLICATION_JSON)
    public BookDTO getBookByName(@QueryParam("title") String title){
        return bookService.getBookDTOByName(title);
    }
}
