package com.softserveinc.dsoky;

import com.softserveinc.dsoky.resources.BooksResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class LibraryApplication extends Application<LibraryConfiguration>{
    public static void main(String[] args) throws Exception {
        new LibraryApplication().run(args);
    }
    @Override
    public void run(LibraryConfiguration libraryConfiguration, Environment environment) throws Exception {
        environment.jersey().register(new BooksResource());
    }

    @Override
    public String getName() {
        return "library";
    }
    @Override
    public void initialize(Bootstrap<LibraryConfiguration> bootstrap) {
        // nothing
    }
}
