package com.softserveinc.dsoky.auth;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

public class LibraryAuthenticator implements Authenticator<BasicCredentials, User>{

    private static Logger log = LoggerFactory.getLogger(LibraryAuthenticator.class);

    private static final Map<String, String> VALID_USERS = ImmutableMap.of(
            "user1", "pass",
            "user2", "pass",
            "user3", "pass",
            "admin", "admin"
    );

    @Override
    public Optional<User> authenticate(BasicCredentials credentials) throws AuthenticationException {

        log.info("Authentication... ");

        String username = credentials.getUsername();
        String password = credentials.getPassword();

        if("admin".equals(username) && VALID_USERS.get(username).equals(password))
            return Optional.of(new User(username, ImmutableSet.of("ADMIN", "USER")));
        else if(VALID_USERS.containsKey(username) && VALID_USERS.get(username).equals(password))
            return Optional.of(new User(username, ImmutableSet.of("USER")));

        return Optional.empty();
    }
}
