package com.softserveinc.dsoky;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;


public class LibraryApplication extends Application<LibraryConfiguration>{

    public static void main(String[] args) throws Exception {
        new LibraryApplication().run(args);
    }

    @Override
    public void run(LibraryConfiguration libraryConfiguration, Environment environment) throws Exception {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.scan("com.softserveinc.dsoky");     // all spring beans
        environment.servlets().addServletListeners(new ContextLoaderListener(context));
        environment.jersey().packages("com.softserveinc.dsoky"); // resources, exception mappers, etc.
    }
}
