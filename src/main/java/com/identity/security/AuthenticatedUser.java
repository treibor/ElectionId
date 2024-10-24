package com.identity.security;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.identity.entity.Users;
import com.identity.repository.UsersRepository;
import com.vaadin.flow.spring.security.AuthenticationContext;


@Component
public class AuthenticatedUser {

    private final UsersRepository userRepository;
    private final AuthenticationContext authenticationContext;

    public AuthenticatedUser(AuthenticationContext authenticationContext, UsersRepository userRepository) {
        this.userRepository = userRepository;
        this.authenticationContext = authenticationContext;
    }

    @Transactional
    public Optional<Users> get() {
        return authenticationContext.getAuthenticatedUser(UserDetails.class)
                .map(userDetails -> userRepository.findByUserNameAndEnabled(userDetails.getUsername(),true));
    }

    public void logout() {
        authenticationContext.logout();
    }

}