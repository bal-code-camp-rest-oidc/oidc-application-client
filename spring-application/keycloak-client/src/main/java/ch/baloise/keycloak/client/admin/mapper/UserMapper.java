package ch.baloise.keycloak.client.admin.mapper;

import ch.baloise.keycloak.client.admin.api.User;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.UUID;

public class UserMapper {

    public static User map(UserRepresentation keycloakUser) {
        User currentUser = new User();
        currentUser.setUserName(keycloakUser.getUsername());
        currentUser.setEmail(keycloakUser.getEmail());
        currentUser.setFirstName(keycloakUser.getFirstName());
        currentUser.setLastName((keycloakUser.getLastName()));
        currentUser.setIdentifier(UUID.fromString(keycloakUser.getId()));
        currentUser.setRoles(keycloakUser.getRealmRoles());

        return currentUser;
    }
}
