package com.example.library.inventory.config;

import com.example.library.inventory.properties.swagger.SwaggerKeycloakProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {


  /**
   * Provides the properties we use to access keycloak using the correct client infos.
   */
  private final SwaggerKeycloakProperties swaggerKeycloakProperties;

  /**
   * Constructor injecting properties from application.yml.
   *
   * @param swaggerKeycloakProperties properties we use to access keycloak using the correct client infos.
   */
  public WebSecurityConfiguration(SwaggerKeycloakProperties swaggerKeycloakProperties) {
    this.swaggerKeycloakProperties = swaggerKeycloakProperties;
  }

  @Bean
  public SecurityConfiguration security() {
    return SecurityConfigurationBuilder.builder()
            .clientId(swaggerKeycloakProperties.getClientId())
            .clientSecret(swaggerKeycloakProperties.getClientSecret())
            .scopeSeparator(" ")
            .useBasicAuthenticationWithAccessCodeGrant(true)
            .build();
  }

  @Override
  public void configure(WebSecurity web) {
    web.ignoring().antMatchers(
            "/v2/api-docs",
            "/v3/api-docs",
            "/configuration/ui/**",
            "/swagger-resources/**",
            "/configuration/security/**",
            "/swagger-ui/**",
            "/swagger-ui/index.html",
            "/webjars/**",
            "/index.html");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .cors(withDefaults())
        .csrf()
        .disable()
        .authorizeRequests()
        .anyRequest()
        .fullyAuthenticated()
        .and()
        .oauth2ResourceServer()
        .jwt();
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList("https://localhost:9092", "http://localhost:9092"));
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
    configuration.setAllowedHeaders(Collections.singletonList(CorsConfiguration.ALL));
    configuration.setAllowCredentials(true);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
