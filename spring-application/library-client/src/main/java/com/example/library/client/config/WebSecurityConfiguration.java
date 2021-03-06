package com.example.library.client.config;

import com.example.library.client.web.CustomLogoutSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    /**
     * Provides the identifier we use for the claim identifying groups.
     */
    private static final String GROUPS_CLAIM = "groups";

    /**
     * Provides the prefix we need for spring authorization.
     */
    private static final String ROLE_PREFIX = "ROLE_";

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new CustomLogoutSuccessHandler();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //fully authenticate with oauth2 and configure an OAuth2 client and an OIDC login client and reconfigure the
        //userinfo endpoint user mapping to map authorities different as the standard one
        http.authorizeRequests()
                .anyRequest()
                .fullyAuthenticated()
                .and()
                .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutSuccessHandler(logoutSuccessHandler())
                .deleteCookies("JSESSIONID")
                .and()
                .oauth2Client()
                .and()
                .oauth2Login()
                .userInfoEndpoint()
                .userAuthoritiesMapper(userAuthoritiesMapper());
    }

    /**
     * Internal method that provides a custom mapping of the groups claim to spring security authority roles.
     *
     * @return the customized mapper.
     */
    private GrantedAuthoritiesMapper userAuthoritiesMapper() {
        return (authorities) -> {
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();

            authorities.forEach(
                    authority -> {
                        if (authority instanceof OidcUserAuthority) {
                            OidcUserAuthority oidcUserAuthority = (OidcUserAuthority) authority;

                            OidcIdToken idToken = oidcUserAuthority.getIdToken();
                            OidcUserInfo userInfo = oidcUserAuthority.getUserInfo();

                            List<SimpleGrantedAuthority> groupAuthorities =
                                    userInfo.getClaimAsStringList(GROUPS_CLAIM).stream()
                                            .map(currentGroup -> new SimpleGrantedAuthority(ROLE_PREFIX + currentGroup.toUpperCase()))
                                            .collect(Collectors.toList());
                            mappedAuthorities.addAll(groupAuthorities);
                        }
                    });

            return mappedAuthorities;
        };
    }
}
