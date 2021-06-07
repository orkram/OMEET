package com.orange.OrangeCommunicatorBackend.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

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

    public static String getSecretKey() {
        return secretKey;
    }

    public static String getClientId() {
        return clientId;
    }

    public static String getAuthUrl() {
        return authUrl;
    }

    public static String getRealm() {
        return realm;
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

    @Bean
    public ClassLoaderTemplateResolver templateResolver() {
        ClassLoaderTemplateResolver templateResolver =
                new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCharacterEncoding("UTF-8");

        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        return templateEngine;
    }
}
