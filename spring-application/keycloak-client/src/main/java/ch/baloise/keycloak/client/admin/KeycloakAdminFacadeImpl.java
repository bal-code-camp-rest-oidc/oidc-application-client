package ch.baloise.keycloak.client.admin;

import ch.baloise.keycloak.client.admin.mapper.UserMapper;
import com.example.library.api.User;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.admin.client.resource.ServerInfoResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.info.ServerInfoRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.NotFoundException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static ch.baloise.keycloak.client.admin.mapper.UserMapper.map;

/**
 * Specific implementation providing some basic functionality to cope with the admin API accessing a Keyclaok server.
 * Please ensure having configured a client as described in
 * https://github.com/bal-code-camp-rest-oidc/documentation/blob/master/keycloak-admin/admin-client-api.md.
 */
public class KeycloakAdminFacadeImpl implements KeycloakAdminFacade {

    /**
     * Provide a logger we can use - the implementation must be provided by container or caller.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * Provides the admin API facade to access the keycloak server.
     */
    private Keycloak keycloak;

    /**
     * Provides the realm ID that we use throughout all calls - we do not want to switch realms while requesting data.
     */
    private String realmId;

    @Override
    public void connect(String serverUrl, String realmId, String clientId, String clientSecret) {
        //remember realm ID
        this.realmId = realmId;

        //try to connect the facade.
        keycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realmId)
                // service called with Access Type 'confidential' eg. without user-context coming from Bearer JWT
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(clientId)
                .clientSecret(clientSecret)
                //.resteasyClient(new ResteasyClientBuilder()
                //        .connectionPoolSize(10).build())
                .build();

        //check that we have connection
        ServerInfoResource serverInfoResource = keycloak.serverInfo();
        ServerInfoRepresentation serverInfo = serverInfoResource.getInfo();

        String serverVersion = serverInfo.getSystemInfo().getVersion();
        if (serverVersion == null || serverVersion.isEmpty()) {
            LOGGER.error("Server not available.");
            throw new IllegalArgumentException("Server not available");
        } else {
            LOGGER.info("Accessing Keycloak with server version={}.", serverVersion);
        }
    }

    @Override
    public List<User> findUsersByMail(String emailAddress, int offset, int maxAmount) {
        //provide the result, might be empty
        List<User> outUsers = new ArrayList<>();

        RealmResource realmResource = keycloak.realm(realmId);
        UsersResource usersResource = realmResource.users();
        for (UserRepresentation currentUserRepresentation : usersResource.search(emailAddress, offset, maxAmount)) {
            outUsers.add(map(currentUserRepresentation));
        }
        return outUsers;
    }

    @Override
    public Optional<User> getUserById(String userId) {
        if (userId == null || userId.isEmpty()) {
            return Optional.empty();
        }
        RealmResource realmResource = keycloak.realm(realmId);
        UsersResource usersResource = realmResource.users();
        return usersResource.list().stream()
                .filter(u -> u.getId().equals(userId))
                .map(UserMapper::map)
                .findFirst();
    }

    @Override
    public List<User> findUsersByRole(String roleId) {
        //provide the result, might be empty
        List<User> outUsers = new ArrayList<>();

        //handle null
        if (roleId == null || roleId.isEmpty()) {
            return outUsers;
        }

        //search all users
        try {
            RoleResource roleResource = keycloak
                    .realm(realmId)
                    .roles()
                    .get(roleId);
            for (UserRepresentation currentUser : roleResource.getRoleUserMembers()) {
                outUsers.add(map(currentUser));
            }
        } catch (NotFoundException notFoundProblem) {
            //nothing found
            return Collections.emptyList();
        }

        return outUsers;
    }

    @Override
    public void close() {
        if (keycloak != null) {
            keycloak.close();
            LOGGER.info("Closed connection to keycloak server.");
        }
    }
}
