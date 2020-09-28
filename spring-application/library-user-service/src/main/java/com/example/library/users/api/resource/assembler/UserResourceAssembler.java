package com.example.library.users.api.resource.assembler;

import com.example.library.users.api.UserRestController;
import com.example.library.users.api.resource.UserResource;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Component;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

import com.example.library.api.User;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserResourceAssembler extends RepresentationModelAssemblerSupport<User, UserResource> {

    public UserResourceAssembler() {
        super(UserRestController.class, UserResource.class);
    }

    @Override
    public UserResource toModel(User user) {
        UserResource userResource = new UserResource(user);
        userResource.add(
                linkTo(methodOn(UserRestController.class).getUser(user.getUserName())).withSelfRel());
        return userResource;
    }

    @Override
    public CollectionModel<UserResource> toCollectionModel(Iterable<? extends User> users) {
        CollectionModel<UserResource> userResources = super.toCollectionModel(users);
        userResources.add(
                linkTo(methodOn(UserRestController.class).getAllUsers()).withSelfRel());
        return userResources;
    }
}
