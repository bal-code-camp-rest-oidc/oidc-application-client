package com.example.library.users.business;

import ch.baloise.keycloak.client.admin.KeycloakAdminFacade;
import ch.baloise.keycloak.client.admin.KeycloakAdminFacadeImpl;
import ch.baloise.keycloak.client.admin.api.User;
import com.example.library.users.KeycloakProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final KeycloakAdminFacade userRepository;

    @Autowired
    public UserService(KeycloakProperties properties) {
        this.userRepository = new KeycloakAdminFacadeImpl();
        userRepository.connect(properties.getServerUrl(), properties.getRealmId(), properties.getClientId(), properties.getClientSecret());
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
