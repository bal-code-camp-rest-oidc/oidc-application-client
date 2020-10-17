package com.example.library.server.business;

import com.example.library.api.User;
import com.example.library.server.api.resource.UserResource;
import com.example.library.server.api.resource.assembler.UserResourceAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.security.RolesAllowed;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService {

    @Value("${library.user-srv}")
    private String userServiceUri;

    private final WebClient webClient;
    private final UserResourceAssembler userResourceAssembler;
    @Autowired
    public UserService(WebClient webClient, UserResourceAssembler userResourceAssembler) {
        this.webClient = webClient;
        this.userResourceAssembler = userResourceAssembler;
    }

    //@PreAuthorize("hasRole('LIBRARY_ADMIN')")
    //@PreAuthorize("hasRole('LIBRARY_USER')")
    @RolesAllowed("LIBRARY_USER")
    public Optional<User> findByIdentifier(String userIdentifier) {
        return webClient
                .get()
                .uri(userServiceUri + "/users/" + userIdentifier)
                .retrieve()
                .bodyToMono(UserResource.class)
                .map(userResourceAssembler::fromModel)
                .log()
                .blockOptional()
                ;
    }

//    @PreAuthorize("hasRole('LIBRARY_ADMIN')")
    @RolesAllowed("LIBRARY_USER")
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
