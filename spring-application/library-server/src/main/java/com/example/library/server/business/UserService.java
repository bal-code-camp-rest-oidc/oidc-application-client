package com.example.library.server.business;

import com.example.library.api.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService {

    @Value("${library.user-srv}")
    private String userServiceUri;

    private final WebClient webClient;

    @Autowired
    public UserService(WebClient webClient) {
        this.webClient = webClient;
    }

    @PreAuthorize("hasRole('LIBRARY_ADMIN')")
    public Optional<User> findByIdentifier(String userIdentifier) {
        return webClient
                .get()
                .uri(userServiceUri + "/users/" + userIdentifier)
                .retrieve()
                .bodyToMono(User.class)
                .log()
                .blockOptional()
                ;
    }

    @PreAuthorize("hasRole('LIBRARY_ADMIN')")
    public List<User> findAll() {
        return webClient
                .get()
                .uri(userServiceUri + "/users")
                .retrieve()
                .bodyToMono(User[].class)
                .log()
                .map(Arrays::asList)
                .block()
                ;
    }
}
