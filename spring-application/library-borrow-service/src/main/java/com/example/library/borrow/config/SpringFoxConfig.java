package com.example.library.borrow.config;

import com.example.library.borrow.properties.KeycloakAdminProperties;
import com.example.library.borrow.properties.swagger.SwaggerApiInfoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.AuthorizationCodeGrantBuilder;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.Collections;

/**
 * Provides configuration and settings we use for swagger integration.<br/>
 * <p>
 * See: https://www.baeldung.com/swagger-2-documentation-for-spring-rest-api
 */
@Configuration
public class SpringFoxConfig {

    /**
     * Provides the properties we use to access keycloak using the admin API.
     */
    private final KeycloakAdminProperties keycloakAdminProperties;

    /**
     * Provides properties intended to enhance the swagger API documentation.
     */
    private final SwaggerApiInfoProperties swaggerApiInfoProperties;

    /**
     * Constructor injecting properties from application.yml.
     *
     * @param keycloakAdminProperties  the properties we need to access properties in the YAML to access keycloak over the admin client.
     * @param swaggerApiInfoProperties the properties to access the API info in the YAML.
     */
    public SpringFoxConfig(KeycloakAdminProperties keycloakAdminProperties, SwaggerApiInfoProperties swaggerApiInfoProperties) {
        this.keycloakAdminProperties = keycloakAdminProperties;
        this.swaggerApiInfoProperties = swaggerApiInfoProperties;
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(Arrays.asList(securityScheme()))
                .securityContexts(Arrays.asList(securityContext()));
    }

    private SecurityScheme securityScheme() {
        GrantType grantType = new AuthorizationCodeGrantBuilder()
                .tokenEndpoint(new TokenEndpoint(keycloakAdminProperties.getAuthServerTokenUrl(), "oauthtoken"))
                .tokenRequestEndpoint(
                        new TokenRequestEndpoint(keycloakAdminProperties.getAuthServerAuthorizeUrl(), keycloakAdminProperties.getClientId(), keycloakAdminProperties.getClientSecret()))
                .build();

        SecurityScheme oauthSecurityScheme = new OAuthBuilder().name("spring_oauth")
                .grantTypes(Arrays.asList(grantType))
                .build();
        return oauthSecurityScheme;
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(
                        Arrays.asList(SecurityReference.builder().reference("spring_oauth").scopes(new AuthorizationScope[0]).build()))
                .forPaths(PathSelectors.any())
                .build();
    }

    /**
     * Internal method that provides the detailed information for the swagger UI.
     *
     * @return detailed information for the swagger UI.
     */
    private ApiInfo apiInfo() {
        return new ApiInfo(
                swaggerApiInfoProperties.getTitle(),
                swaggerApiInfoProperties.getDescription(),
                swaggerApiInfoProperties.getVersion(),
                swaggerApiInfoProperties.getTermsOfServiceUrl(),
                new Contact(swaggerApiInfoProperties.getContact().getName(), swaggerApiInfoProperties.getContact().getUrl(), swaggerApiInfoProperties.getContact().getEmail()),
                swaggerApiInfoProperties.getLicense(),
                swaggerApiInfoProperties.getLicenseUrl(),
                Collections.emptyList());
    }
}
