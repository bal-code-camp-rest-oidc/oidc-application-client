package com.example.library.server.business;

import ch.baloise.keycloak.client.admin.api.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class UserService {

  @Value("library.user-srv")
  private String userServiceUri;

  @PreAuthorize("hasRole('LIBRARY_ADMIN')")
  public Optional<User> findByIdentifier(String userIdentifier) {
    RestTemplate restTemplate = new RestTemplateBuilder()
                                    // todo auth
                                    .rootUri(userServiceUri)
                                    .build();

    ResponseEntity<User> response = restTemplate.exchange(
        userIdentifier, HttpMethod.GET, null,
        new ParameterizedTypeReference<User>() {
        });

    return response.getBody() == null ? Optional.empty() : Optional.of(response.getBody());
  }

  @PreAuthorize("hasRole('LIBRARY_ADMIN')")
  public List<User> findAll() {
    RestTemplate restTemplate = new RestTemplateBuilder()
                                    .rootUri(userServiceUri)
                                    // todo auth
                                    .build();

    ResponseEntity<List<User>> response = restTemplate.exchange(
        "", HttpMethod.GET, null,
        new ParameterizedTypeReference<List<User>>() {
        });

    return response.getBody();
  }
}
