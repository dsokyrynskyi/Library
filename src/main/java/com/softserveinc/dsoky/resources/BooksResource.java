package com.softserveinc.dsoky.resources;

import com.softserveinc.dsoky.api.Book;
import com.softserveinc.dsoky.dao.BookDAO;
import com.softserveinc.dsoky.dao.BookDAOImpl;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/book/{id}")
@Produces(MediaType.APPLICATION_JSON)
public class BooksResource {

    private final BookDAO bookDAO = new BookDAOImpl();

    @GET
    public Book fetchBook(@PathParam("id") long id){
        return bookDAO.get(id);
    }

}
