package com.softserveinc.dsoky.auth;

import io.dropwizard.auth.Authorizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LibraryAuthorizer implements Authorizer<User>{

    private static Logger log = LoggerFactory.getLogger(LibraryAuthorizer.class);

    @Override
    public boolean authorize(User user, String role) {
        log.info("Authorization...");
        return user.getRoles() != null && user.getRoles().contains(role);
    }
}
