package com.softserveinc.dsoky.resources;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserveinc.dsoky.dto.AuthorDTO;
import com.softserveinc.dsoky.service.AuthorService;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.test.DeploymentContext;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.ServletDeploymentContext;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.glassfish.jersey.test.spi.TestContainerFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.context.ContextLoaderListener;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthorsResourceTest extends JerseyTest{

    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private AuthorService authorService;

    @Override
    protected TestContainerFactory getTestContainerFactory() {
        return new GrizzlyWebTestContainerFactory();
    }


    @Override
    protected DeploymentContext configureDeployment(){
        beforeTesting();
        return ServletDeploymentContext
                .forServlet(new ServletContainer(new AuthorsResourceConfig(authorService)))
                .addListener(ContextLoaderListener.class)
                .contextParam("contextConfigLocation", "classpath:applicationContext.xml")
                .build();
    }

    @Before
    public void beforeTesting(){
        authorService = Mockito.mock(AuthorService.class);
        when(authorService.getDTO(1)).thenReturn(new AuthorDTO(100_000L, "Adolf", "Finland", "1990-08-08"));
        when(authorService.getDTOByBook(1)).thenReturn(Collections.singletonList(new AuthorDTO(100_000L, "Adolf", "Finland", "1990-08-08")));
    }

    @Test
    public void linkHeaderReturnsLinksForBooksAndPublishers(){
        Response output = target("/v1/authors").request().get();
        assertThat(output.getHeaderString("link"), containsString("http://localhost:8080/books"));
        assertThat(output.getHeaderString("link"), containsString("http://localhost:8080/publishers"));
    }

    @Test
    public void responseReturnsJson(){
        Response output = target("/v1/authors").request().get();
        assertThat(output.getMediaType().toString(), equalTo(MediaType.APPLICATION_JSON));
    }

    @Test
    public void fetchGetByAuthorIdIsOk(){
        Response output = target("/v1/authors/1").request().get();
        AuthorDTO dto = output.readEntity(AuthorDTO.class);

        assertThat(output.getStatus(), is(200));
        assertThat(dto.getId(), is(100_000L));
        assertThat(dto.getName(), equalTo("Adolf"));
    }

    @Test
    public void fetchGetByAuthorIdReturnsValidLink(){
        Response output = target("/v1/authors/1").request().get();

        assertThat(output.getHeaderString("link"), containsString("http://localhost:8080/v1/authors/1/books"));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void fetchListOfAuthorsForCertainBookNameReturnsExpectedList(){
        Response output = target("/v1/books/1/authors").request().get();
        AuthorDTO dto = objectMapper.convertValue(output.readEntity(List.class).get(0), AuthorDTO.class);

        assertThat(dto.getName(), equalTo("Adolf"));
        assertThat(dto.getCountry(), equalTo("Finland"));
        assertThat(dto.getDate(), equalTo("1990-08-08"));
    }

    @Test
    public void saveAuthorDTOReturnsNoContent(){
        AuthorDTO dto = new AuthorDTO();
        Response output = target("/v1/authors").request().post(Entity.entity(dto, MediaType.APPLICATION_JSON));

        assertThat(output.getStatus(), is(204));
    }
}
