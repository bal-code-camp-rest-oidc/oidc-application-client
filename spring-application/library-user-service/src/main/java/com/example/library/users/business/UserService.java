package com.example.library.users.business;

import ch.baloise.keycloak.client.admin.KeycloakAdminFacade;
import ch.baloise.keycloak.client.admin.KeycloakAdminFacadeImpl;
import ch.baloise.keycloak.client.admin.api.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final KeycloakAdminFacade userRepository;

    public UserService() {
        this.userRepository = new KeycloakAdminFacadeImpl();
        userRepository.connect("http://localhost:8080/auth", "workshop", "keycloak-admin", "ab14c208-a2ee-47a9-9fd5-04c93cc61a2a");
    }

    public Optional<User> findUserByEmail(String email) {
        List<User> result = userRepository.findUsersByMail(email, 0, 10);
        return Optional.of(result.get(0));
    }

    public Optional<User> getUserById(String userId) {
        return userRepository.getUserById(userId);
    }

    public List<User> findAll() {
        return userRepository.findUsersByMail("", 0, 100);
    }
}
