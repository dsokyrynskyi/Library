package com.softserveinc.dsoky;

import com.softserveinc.dsoky.resources.BooksResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class LibraryApplication extends Application<LibraryConfiguration>{

    public static void main(String[] args) throws Exception {
        new LibraryApplication().run(args);
    }

    @Override
    public void run(LibraryConfiguration libraryConfiguration, Environment environment) throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(LibrarySpringConfiguration.class);
        environment.jersey().register(context.getBean(BooksResource.class));
    }

    @Override
    public void initialize(Bootstrap<LibraryConfiguration> bootstrap) {
        // nothing
    }

}
