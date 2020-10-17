package com.example.library.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class User {

    private UUID identifier;

    private String email;

    private String firstName;

    private String lastName;

    private String userName;

    private List<String> roles = new ArrayList<>();

    public User() {
    }

    public User(User user) {
        this(
                user.getIdentifier(),
                user.getUserName(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRoles());
    }

    public User(
            UUID identifier, String userName, String email, String firstName, String lastName, List<String> roles) {
        this.identifier = identifier;
        this.userName = userName;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        if (roles != null) {
            this.roles.addAll(roles);
        }
    }

    public UUID getIdentifier() {
        return identifier;
    }

    public void setIdentifier(UUID identifier) {
        this.identifier = identifier;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
        if (roles != null) {
            this.roles.addAll(roles);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        return identifier.equals(user.identifier)
                && email.equals(user.email)
                && firstName.equals(user.firstName)
                && lastName.equals(user.lastName)
                && Objects.equals(roles, user.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), identifier, email, firstName, lastName, roles);
    }

    @Override
    public String toString() {
        return "User{"
                + "identifier="
                + identifier
                + ", email='"
                + email
                + '\''
                + ", firstName='"
                + firstName
                + '\''
                + ", lastName='"
                + lastName
                + '\''
                + ", roles="
                + roles
                + '}';
    }
}
