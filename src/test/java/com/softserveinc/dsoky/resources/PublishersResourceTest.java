package com.softserveinc.dsoky.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserveinc.dsoky.dto.PublisherDTO;
import com.softserveinc.dsoky.resources.config.PublishersResourceConfig;
import com.softserveinc.dsoky.service.PublisherService;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.test.DeploymentContext;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.ServletDeploymentContext;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.glassfish.jersey.test.spi.TestContainerFactory;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.context.ContextLoaderListener;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PublishersResourceTest extends JerseyTest{

    private ObjectMapper objectMapper = new ObjectMapper();
    private PublisherService publisherService;

    @Override
    protected TestContainerFactory getTestContainerFactory() {
        return new GrizzlyWebTestContainerFactory();
    }

    @Override
    protected DeploymentContext configureDeployment(){
        beforeTesting();
        return ServletDeploymentContext
                .forServlet(new ServletContainer(new PublishersResourceConfig(publisherService)))
                .addListener(ContextLoaderListener.class)
                .contextParam("contextConfigLocation", "classpath:applicationContext.xml")
                .build();
    }

    public void beforeTesting(){
        publisherService = Mockito.mock(PublisherService.class);
        when(publisherService.getAllDTOs()).thenReturn(Collections.singletonList(new PublisherDTO(100_000L, "TEST", "Russia")));
        when(publisherService.getDTO(1)).thenReturn(new PublisherDTO(100_000L, "TEST", "Russia"));
        when(publisherService.getDTOByBook(1)).thenReturn(new PublisherDTO(100_000L, "TEST", "Russia"));
    }

    @Test
    public void getPublisherByIdReturnsExpectedPublisher(){
        Response output = target("/v1/publishers/1").request().get();
        PublisherDTO dto = output.readEntity(PublisherDTO.class);

        assertThat(output.getStatus(), is(200));
        assertThat(dto.getId(), is(100_000L));
        assertThat(dto.getName(), is("TEST"));
    }

    @Test
    public void retrievingAllThePublishers(){
        Response output = target("/v1/publishers/").request().get();
        PublisherDTO dto = objectMapper.convertValue(output.readEntity(List.class).get(0), PublisherDTO.class);

        assertThat(output.getStatus(), is(200));
        assertThat(dto.getId(), is(100_000L));
        assertThat(dto.getCountry(), is("Russia"));
    }

    @Test
    public void fetchPublisherForCertainBookReturnsExpectedResult(){
        Response output = target("/v1/books/1/publisher").request().get();
        PublisherDTO dto = output.readEntity(PublisherDTO.class);

        assertThat(dto.getName(), equalTo("TEST"));
        assertThat(dto.getCountry(), equalTo("Russia"));
    }

    @Test
    public void savePublisherDTOReturnsNoContent(){
        PublisherDTO dto = new PublisherDTO();
        dto.setName("TEST");
        Response output = target("/v1/publishers").request().post(Entity.entity(dto, MediaType.APPLICATION_JSON));

        assertThat(output.getStatus(), is(204));
    }

    @Test
    public void deletePublisherDTOReturnsNoContent(){
        Response output = target("/v1/publishers/1").request().delete();

        assertThat(output.getStatus(), is(204));
    }

    @Test
    public void impossibleDeletePublisherWithoutId(){
        Response output = target("/v1/publishers/").request().delete();

        assertThat(output.getStatus(), is(405));
    }
}
