package com.example.library.users.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.IdGenerator;

import ch.baloise.keycloak.client.admin.KeycloakAdminFacadeImpl;
import ch.baloise.keycloak.client.admin.api.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final KeycloakAdminFacadeImpl userRepository;
    private final IdGenerator idGenerator;

    @Autowired
    public UserService(KeycloakAdminFacadeImpl userRepository, IdGenerator idGenerator) {
        this.userRepository = userRepository;
        this.idGenerator = idGenerator;
    }

    public Optional<User> findUserByEmail(String email) {
        List<User> result = userRepository.findUsersByMail(email, 0, 10);

        return Optional.of(result.get(0));
    }

    public Optional<User> getUserById(String userId) {
        return userRepository.getUserById(userId);
    }


    @PreAuthorize("hasRole('LIBRARY_ADMIN')")
    public List<User> findAll() {
        return userRepository.findUsersByMail("", 0, 100);
    }
}
