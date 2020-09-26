package com.example.library.server.api;

import com.example.library.server.api.resource.UserResource;
import com.example.library.server.api.resource.assembler.UserResourceAssembler;
import com.example.library.server.business.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@Validated
public class UserRestController {

  private final UserService userService;
  private final UserResourceAssembler userResourceAssembler;

  @Autowired
  public UserRestController(UserService userService, UserResourceAssembler userResourceAssembler) {
    this.userService = userService;
    this.userResourceAssembler = userResourceAssembler;
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping
  public List<UserResource> getAllUsers() {
    return userService.findAll().stream()
               .map(userResourceAssembler::toModel)
               .collect(Collectors.toList());
  }

  @GetMapping("/{userId}")
  public ResponseEntity<UserResource> getUser(@PathVariable("userId") UUID userId) {
    return userService
               .findByIdentifier(userId)
               .map(u -> ResponseEntity.ok(userResourceAssembler.toModel(u)))
               .orElse(ResponseEntity.notFound().build());
  }
}
