package com.example.library.inventory.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("library.server.keycloak")
public class KeycloakAdminProperties {
    private String serverUrl;

    private String realmId;

    private String clientId;

    private String clientSecret;

    private final String AUTH_SERVER_AUTHORIZE = "%s/realms/%s/protocol/openid-connect/auth";

    private final String AUTH_SERVER_TOKEN = "%s/realms/%s/protocol/openid-connect/token";

    public String getAuthServerAuthorizeUrl() {
        return String.format(AUTH_SERVER_AUTHORIZE, serverUrl, realmId);
    }

    public String getAuthServerTokenUrl() {
        return String.format(AUTH_SERVER_TOKEN, serverUrl, realmId);
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public String getRealmId() {
        return realmId;
    }

    public void setRealmId(String realmId) {
        this.realmId = realmId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}
