package com.example.library.users.api.resource;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ModifyingUserResource extends UserResource {

    @NotNull
    @Pattern(regexp = "[A-Za-z0-9_!]{8,100}")
    private String password;

    private List<String> roles = new ArrayList<>();

    public ModifyingUserResource() {
    }

    public ModifyingUserResource(
            UUID identifier,
            String userId,
            String email,
            String firstName,
            String lastName,
            String password,
            List<String> roles) {
        super(identifier, userId, email, firstName, lastName, roles);
        this.password = password;
        this.roles.addAll(roles);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
