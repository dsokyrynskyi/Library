package com.softserveinc.dsoky;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
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
        //environment.servlets().addFilter("MDC Filter", new MDCFilter()).addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
        environment.jersey().packages("com.softserveinc.dsoky"); // resources, exception mappers, etc.
        log.debug("Dropwizard environment was bind with Spring Context");
    }
}
