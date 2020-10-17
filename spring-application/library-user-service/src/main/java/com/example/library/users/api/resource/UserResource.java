package com.example.library.users.api.resource;

import org.springframework.hateoas.RepresentationModel;

import com.example.library.api.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class UserResource extends RepresentationModel<UserResource> {

    private UUID identifier;

    @NotNull
    @Size(min = 1, max = 100)
    @Email
    private String email;

    @NotNull
    @Size(min = 1, max = 100)
    private String firstName;

    @NotNull
    @Size(min = 1, max = 100)
    private String lastName;

    @NotNull
    @Size(min = 1, max = 100)
    private String userId;

    private List<String> roles;

    public UserResource() {
    }

    public UserResource(User user) {
        this(
                user.getIdentifier(),
                user.getUserName(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRoles());
    }

    public UserResource(UUID identifier, String userid, String email, String firstName, String lastName, List<String> roles) {
        this.identifier = identifier;
        this.userId = userid;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles = roles;
    }

    public UUID getIdentifier() {
        return identifier;
    }

    public void setIdentifier(UUID identifier) {
        this.identifier = identifier;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UserResource that = (UserResource) o;
        return identifier.equals(that.identifier)
                && userId.equals(that.userId)
                && email.equals(that.email)
                && firstName.equals(that.firstName)
                && lastName.equals(that.lastName)
                && Objects.equals(roles, that.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), identifier, userId, email, firstName, lastName);
    }

    @Override
    public String toString() {
        return "UserResource{"
                + "identifier="
                + identifier
                + '\''
                + "userId="
                + userId
                + ", email='"
                + email
                + '\''
                + ", firstName='"
                + firstName
                + '\''
                + ", lastName='"
                + lastName
                + '\''
                + ", roles='"
                + roles
                + '\''
                + '}';
    }
}
