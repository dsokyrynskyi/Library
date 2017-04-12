package com.softserveinc.dsoky.resources;

import com.softserveinc.dsoky.dto.BookDTO;
import com.softserveinc.dsoky.dto.RichBookDTO;
import com.softserveinc.dsoky.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
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

    private URI uri;
    private final BookService bookService;

    @Autowired
    public BooksResource(BookService bookService) {
        this.bookService = bookService;
    }

    @PostConstruct
    public void init(){
        uri = UriBuilder.fromPath("http://{host}:{port}/{version}/")
                .resolveTemplate("host", "localhost")
                .resolveTemplate("port", "8080")
                .resolveTemplate("version", "v1")
                .build();
    }

    @GET
    @Path("/books/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response fetchAll() {
        URI publishersUri = uri.resolve("/publishers");
        URI authorsUri = uri.resolve("/authors");
        return Response.ok(bookService.getAllBookDTOs())
                .link(authorsUri, "authors")
                .link(publishersUri, "publishers")
                .build();
    }

    @GET
    @Path("/books/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response fetchBook(@PathParam("id") long id) {
        URI publisherUri = uri.resolve("books/"+id+"/publisher");
        URI authorsUri = uri.resolve("/books/"+id+"/authors");
        return Response.ok(bookService.getBookDTO(id))
                .link(authorsUri, "authors")
                .link(publisherUri, "publisher")
                .build();
    }

    @POST
    @Path("/books/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveBook(RichBookDTO book) {
        bookService.saveDTO(book);
        return Response.ok().build();
    }

    @PUT
    @Path("/books/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBook(RichBookDTO bookDTO, @PathParam("id") long bookId) {
        bookDTO.setId(bookId);
        bookService.update(bookDTO);
        return Response.ok().build();
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
