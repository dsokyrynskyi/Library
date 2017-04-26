package com.softserveinc.dsoky;

import com.softserveinc.dsoky.auth.LibraryAuthenticator;
import com.softserveinc.dsoky.auth.LibraryAuthorizer;
import com.softserveinc.dsoky.auth.User;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;


public class LibraryApplication extends Application<LibraryConfiguration>{

    private static Logger log = LoggerFactory.getLogger(LibraryApplication.class);

    public static void main(String[] args) throws Exception {
        log.info("Running application...  ");
        new LibraryApplication().run(args);
        log.info("Application is started and ready to listen requests");
    }

    @Override
    public void run(LibraryConfiguration libraryConfiguration, Environment environment) throws Exception {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.scan("com.softserveinc.dsoky");     // all spring beans
        log.debug("Spring context was created and base package was scanned");
        environment.servlets().addServletListeners(new ContextLoaderListener(context));
        environment.jersey().packages("com.softserveinc.dsoky"); // resources, exception mappers, etc.

        environment.jersey().register(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<User>()
                .setAuthenticator(new LibraryAuthenticator())
                .setAuthorizer(new LibraryAuthorizer())
                .buildAuthFilter()
        ));
        environment.jersey().register(RolesAllowedDynamicFeature.class);

        log.debug("Dropwizard environment was bind with Spring Context");
    }
}
