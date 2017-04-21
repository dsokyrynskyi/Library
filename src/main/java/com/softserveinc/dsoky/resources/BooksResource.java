package com.softserveinc.dsoky.resources;

import com.softserveinc.dsoky.dto.BookDTO;
import com.softserveinc.dsoky.dto.RichBookDTO;
import com.softserveinc.dsoky.service.BookService;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
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

 GET     /v1/books/byName?title='...'
 DELETE  /v1/books/{bId}/authors/{aId}
 GET     /v1/authors/{id}/books
 GET     /v1/publishers/{id}/books
 * */

@Component
@Path("v1/")
public class BooksResource {

    private static Logger log = LoggerFactory.getLogger(BooksResource.class);
    private URI uri;
    private final BookService bookService;

    public void setUri(URI uri) {
        this.uri = uri;
    }

    @Autowired
    public BooksResource(BookService bookService) {
        this.bookService = bookService;
    }

    @PostConstruct
    public void init(){
        log.debug("Building URI template for headers from requests to BooksResource... ");
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
        log.info("Fetching all the books... ");
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
        log.info("Fetching the book by id... ");
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
    public Response saveBook(@Valid RichBookDTO book) {
        log.info("Saving the book... ");
        bookService.saveDTO(book);
        return Response.ok().build();
    }

    @PUT
    @Path("/books/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBook(@Valid RichBookDTO bookDTO, @PathParam("id") long bookId) {
        log.info("Updating the book... ");
        bookDTO.setId(bookId);
        bookService.update(bookDTO);
        return Response.ok().build();
    }

    @DELETE
    @Path("/books/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void removeBook(@PathParam("id") long id) {
        log.info("Removing the book... ");
        bookService.remove(id);
    }

    @DELETE
    @Path("/books/{bId}/authors/{aId}")
    @Produces(MediaType.APPLICATION_JSON)
    public void removeRelation(@PathParam("bId") long bId, @PathParam("aId") long aId) {
        log.info("Removing the author from the book's list of authors... ");
        bookService.removeRelation(bId, aId);
    }

    @GET
    @Path("/authors/{id}/books")
    @Produces(MediaType.APPLICATION_JSON)
    public List<BookDTO> getBooksOfAuthor(@PathParam("id") long id){
        log.info("Fetching all the books for certain author... ");
        return bookService.getDTOByAuthor(id);
    }

    @GET
    @Path("/publishers/{id}/books")
    @Produces(MediaType.APPLICATION_JSON)
    public List<BookDTO> getBooksOfPublisher(@PathParam("id") long id){
        log.info("Fetching all the books for certain publisher...");
        return bookService.getDTOByPublisher(id);
    }

    @GET
    @Path("/books/byName")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBookByName(@QueryParam("title") @NotEmpty String title){
        log.info("Fetching the book by title...");
        return Response.ok(bookService.getBookDTOByName(title), MediaType.APPLICATION_JSON).build();
    }
}
