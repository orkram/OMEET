package com.orange.OrangeCommunicatorBackend.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakClientConfig {

    private static String secretKey;

    private static String clientId;

    private static String authUrl;

    private static String realm;

    @Autowired
    KeycloakClientConfig(@Value("${keycloak.credentials.secret}") String secretKey,
                         @Value("${keycloak.resource}") String clientId,
                         @Value("${keycloak.auth-server-url}") String authUrl,
                         @Value("${keycloak.realm}") String realm){

        KeycloakClientConfig.secretKey = secretKey;
        KeycloakClientConfig.clientId = clientId;
        KeycloakClientConfig.authUrl = authUrl;
        KeycloakClientConfig.realm = realm;
    }


    @Bean
    public static Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .serverUrl(authUrl)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(secretKey)
                .build();
    }
}
