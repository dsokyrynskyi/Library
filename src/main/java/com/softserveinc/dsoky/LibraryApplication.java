package com.softserveinc.dsoky;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;


public class LibraryApplication extends Application<LibraryConfiguration>{

    public static void main(String[] args) throws Exception {
        new LibraryApplication().run(args);
    }

    @Override
    public void run(LibraryConfiguration libraryConfiguration, Environment environment) throws Exception {
        configContext(environment);
        configJersey(environment);
    }

    private void configJersey(Environment environment) {
        environment.jersey().packages("com.softserveinc.dsoky");
    }

    private void configContext(Environment environment) {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.scan("com.softserveinc.dsoky");
       // context.refresh();
        //context.registerShutdownHook();
        environment.servlets().addServletListeners(new ContextLoaderListener(context));
    }

    @Override
    public void initialize(Bootstrap<LibraryConfiguration> bootstrap) {
        // nothing
    }

}
