package ch.baloise.keycloak.client.admin;

import ch.baloise.keycloak.client.admin.api.User;

import java.util.List;
import java.util.Optional;

/**
 * Provides a facade we use to ease up access to our keycloak instance.
 */
public interface KeycloakAdminFacade {

    /**
     * Connects your facade to a keycloak server so that you can start requesting.
     *
     * @param serverUrl    the server URL of teh keycloak server such as "http://localhost:8080/auth", must not be null.
     * @param realmId      the unique ID of the realm that should be administrated, must not be null.
     * @param clientId     the client ID of that is used to access the realm and which must be configured using service account roles, must not be null.
     * @param clientSecret the secret we have configured in teh client on keycloak to access it, must not be null.
     */
    void connect(String serverUrl, String realmId, String clientId, String clientSecret);

    /**
     * Searches a user using his/ her email address.
     *
     * @param emailAddress the address we want to search for, fragments are wildcards so that null or empty finds all users.
     * @param offset       the starting position in the result we want to have, enabling paged result handling.
     * @param maxAmount    the max amount of results we want to have, enabling paged result handling.
     * @return the collection of user IDs that match search criteria.
     */
    List<User> findUsersByMail(String emailAddress, int offset, int maxAmount);

    /**
     * Searches a user using his/ her unique ID.
     *
     * @param userId the unique ID we want to search for, must not be null.
     * @return the collection of user IDs that match search criteria.
     */
    Optional<User> getUserById(String userId);

    /**
     * Provides all users that have a role assigned.
     *
     * @param roleId the unique role ID as configured in keycloak, can be null (not finding anything).
     * @return the collection of user IDs that match search criteria.
     */
    List<User> findUsersByRole(String roleId);

    /**
     * Closes an existing connection to the facade.
     */
    void close();
}
