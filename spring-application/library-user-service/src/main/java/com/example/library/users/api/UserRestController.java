package com.example.library.users.api;

import com.example.library.users.api.resource.UserResource;
import com.example.library.users.api.resource.assembler.UserResourceAssembler;
import com.example.library.users.business.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @ApiOperation(value = "Provides a list of all users currently known in the realm working with libraries and borrowing books.", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "the list of all registered users"),
            @ApiResponse(code = 401, message = "You are not authorized to view the list"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<UserResource> getAllUsers() {
        return userService.findAll().stream()
                .map(userResourceAssembler::toModel)
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "Provides the detailed information for a user identified by his/ her user ID.",
            response = UserResource.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "the detailed for the user identified by his/ her user ID."),
            @ApiResponse(code = 401, message = "You are not authorized to view the list"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @GetMapping("/{userId}")
    public ResponseEntity<UserResource> getUser(
            @ApiParam(
                    name = "userId",
                    type = "String",
                    value = "The unique user ID as defined in the keycloak server",
                    example = "bwayne",
                    required = true)
            @PathVariable("userId") String userId) {
        return userService
                .getUserById(userId)
                .map(currentUser -> ResponseEntity.ok(userResourceAssembler.toModel(currentUser)))
                .orElse(ResponseEntity.notFound().build());
    }
}
