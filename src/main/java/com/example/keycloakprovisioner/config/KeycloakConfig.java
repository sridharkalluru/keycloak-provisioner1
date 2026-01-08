package com.example.keycloakprovisioner.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {

    @Bean
    public Keycloak keycloak(KeycloakAdminProperties props) {

        KeycloakBuilder builder = KeycloakBuilder.builder()
                .serverUrl(props.getServerUrl())
                .realm(props.getRealm())
                .clientId(props.getClientId());

        if ("client_credentials".equalsIgnoreCase(props.getGrantType())) {
            builder = builder
                    .clientSecret(props.getClientSecret())
                    .grantType(OAuth2Constants.CLIENT_CREDENTIALS);
        } else {
            builder = builder
                    .username(props.getUsername())
                    .password(props.getPassword())
                    .grantType(OAuth2Constants.PASSWORD);
        }

        return builder.build();
    }
}
