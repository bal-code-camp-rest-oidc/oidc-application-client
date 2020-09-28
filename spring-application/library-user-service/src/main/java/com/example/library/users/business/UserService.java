package com.example.library.users.business;

import ch.baloise.keycloak.client.admin.KeycloakAdminFacade;
import ch.baloise.keycloak.client.admin.KeycloakAdminFacadeImpl;
import com.example.library.api.User;
import com.example.library.users.properties.KeycloakAdminProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final KeycloakAdminFacade userRepository;

    @Autowired
    public UserService(KeycloakAdminProperties properties) {
        this.userRepository = new KeycloakAdminFacadeImpl();
        userRepository.connect(properties.getServerUrl(), properties.getRealmId(), properties.getClientId(), properties.getClientSecret());
    }

    public Optional<User> getUserById(String userId) {
        return userRepository.getUserById(userId);
    }

    public List<User> findAll() {
        return userRepository.findUsersByMail("", 0, 100);
    }
}
