package com.softserveinc.dsoky.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserveinc.dsoky.dto.BookDTO;
import com.softserveinc.dsoky.dto.RichBookDTO;
import com.softserveinc.dsoky.exceptions.NoSuchLibraryResourceException;
import com.softserveinc.dsoky.resources.config.BooksResourceConfig;
import com.softserveinc.dsoky.service.BookService;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.test.DeploymentContext;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.ServletDeploymentContext;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.glassfish.jersey.test.spi.TestContainerFactory;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.context.ContextLoaderListener;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BooksResourceTest extends JerseyTest{

    private ObjectMapper objectMapper;
    private BookService bookService;

    @Override
    protected TestContainerFactory getTestContainerFactory() {
        return new GrizzlyWebTestContainerFactory();
    }

    @Override
    protected DeploymentContext configureDeployment(){
        beforeTesting();
        return ServletDeploymentContext
                .forServlet(new ServletContainer(new BooksResourceConfig(bookService)))
                .addListener(ContextLoaderListener.class)
                .contextParam("contextConfigLocation", "classpath:applicationContext.xml")
                .build();
    }

    @SuppressWarnings("unchecked")
    public void beforeTesting(){
        objectMapper = new ObjectMapper();
        bookService = Mockito.mock(BookService.class);
        when(bookService.getAllBookDTOs()).thenReturn(Collections.singletonList(new BookDTO(100_000L, "TEST-NAME", "TEST-ISBN", "2000-02-02", "TEST-GENRE")));
        when(bookService.getBookDTO(23)).thenReturn(new BookDTO(230_000L, "TEST-NAME-23", "TEST-ISBN-23", "2023-02-02", "TEST-GENRE-23"));
        when(bookService.getBookDTO(666)).thenThrow(NoSuchLibraryResourceException.class);
        when(bookService.getDTOByAuthor(1)).thenReturn(Collections.singletonList(new BookDTO(100_000L, "TEST-NAME", "TEST-ISBN", "2000-02-02", "TEST-GENRE")) );
        when(bookService.getDTOByPublisher(1)).thenReturn(Collections.singletonList(new BookDTO(100_000L, "TEST-NAME", "TEST-ISBN", "2000-02-02", "TEST-GENRE")) );
        when(bookService.getBookDTOByName("test")).thenReturn(new BookDTO(100_000L, "TEST-NAME", "TEST-ISBN", "2000-02-02", "TEST-GENRE"));
    }

    @Test
    public void testGetAllBookDTOs(){
        Response output = target("/v1/books").request().get();
        BookDTO dto = objectMapper.convertValue(output.readEntity(List.class).get(0), BookDTO.class);

        assertThat(dto.getId(), is(100_000L));
        assertThat(dto.getName(), CoreMatchers.equalTo("TEST-NAME"));
        assertThat(dto.getIsbn(), CoreMatchers.equalTo("TEST-ISBN"));
        assertThat(dto.getPublishDate(), CoreMatchers.equalTo("2000-02-02"));
        assertThat(dto.getGenre(), CoreMatchers.equalTo("TEST-GENRE"));
    }

    @Test
    public void linkHeaderReturnsLinksForAuthorsAndPublisher(){
        Response output = target("/v1/books").request().get();
        assertThat(output.getHeaderString("link"), containsString("http://localhost:8080/authors"));
        assertThat(output.getHeaderString("link"), containsString("http://localhost:8080/publishers"));
    }

    @Test
    public void fetchExpectedBookById(){
        Response output = target("/v1/books/23").request().get();
        BookDTO dto = output.readEntity(BookDTO.class);

        assertThat(dto.getId(), is(230_000L));
        assertThat(dto.getName(), CoreMatchers.equalTo("TEST-NAME-23"));
        assertThat(dto.getIsbn(), CoreMatchers.equalTo("TEST-ISBN-23"));
        assertThat(dto.getPublishDate(), CoreMatchers.equalTo("2023-02-02"));
        assertThat(dto.getGenre(), CoreMatchers.equalTo("TEST-GENRE-23"));
    }

    @Test
    public void fetchExpectedBookByName(){
        Response output = target("/v1/books/byName").queryParam("title", "test").request().get();
        BookDTO dto = output.readEntity(BookDTO.class);

        assertThat(dto.getId(), is(100_000L));
        assertThat(dto.getName(), CoreMatchers.equalTo("TEST-NAME"));
        assertThat(dto.getIsbn(), CoreMatchers.equalTo("TEST-ISBN"));
        assertThat(dto.getPublishDate(), CoreMatchers.equalTo("2000-02-02"));
        assertThat(dto.getGenre(), CoreMatchers.equalTo("TEST-GENRE"));
    }

    @Test
    public void ifNoElementWithCurrentIdThenShowNotFoundStatus(){
        Response output = target("/v1/books/666").request().get();

        assertThat(output.getStatus(), is(404));
    }

    @Test
    public void fetchExpectedBookByAuthor(){
        Response output = target("/v1/authors/1/books").request().get();
        BookDTO dto = objectMapper.convertValue(output.readEntity(List.class).get(0), BookDTO.class);

        assertThat(dto.getId(), is(100_000L));
        assertThat(dto.getName(), CoreMatchers.equalTo("TEST-NAME"));
        assertThat(dto.getIsbn(), CoreMatchers.equalTo("TEST-ISBN"));
        assertThat(dto.getPublishDate(), CoreMatchers.equalTo("2000-02-02"));
        assertThat(dto.getGenre(), CoreMatchers.equalTo("TEST-GENRE"));
    }

    @Test
    public void fetchExpectedBookByPublisher(){
        Response output = target("/v1/publishers/1/books").request().get();
        BookDTO dto = objectMapper.convertValue(output.readEntity(List.class).get(0), BookDTO.class);

        assertThat(dto.getId(), is(100_000L));
        assertThat(dto.getName(), CoreMatchers.equalTo("TEST-NAME"));
        assertThat(dto.getIsbn(), CoreMatchers.equalTo("TEST-ISBN"));
        assertThat(dto.getPublishDate(), CoreMatchers.equalTo("2000-02-02"));
        assertThat(dto.getGenre(), CoreMatchers.equalTo("TEST-GENRE"));
    }

    @Test
    public void saveBookDTO(){
        RichBookDTO dto = new RichBookDTO();
        Response output = target("/v1/books").request().post(Entity.entity(dto, MediaType.APPLICATION_JSON));

        assertThat(output.getStatus(), is(200));
    }

    @Test
    public void updateExistingBook() {
        RichBookDTO dto = new RichBookDTO();
        Response output = target("/v1/books/1").request().put(Entity.entity(dto, MediaType.APPLICATION_JSON));

        assertThat(output.getStatus(), is(200));
    }

    @Test
    public void deleteBook() {
        Response output = target("/v1/books/1").request().delete();

        assertThat(output.getStatus(), is(204));
    }

    @Test
    public void notAllowedMethodIfRemovingBookWithoutId(){
        Response output = target("/v1/books/").request().delete();

        assertThat(output.getStatus(), is(405));
    }
}
