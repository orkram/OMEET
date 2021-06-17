//Autorzy kodu źródłowego: Bartosz Panuś
//Kod został utworzony w ramach kursu Projekt Zespołowy
//na Politechnice Wrocławskiej
package com.orange.OrangeCommunicatorBackend.config;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.*;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@Configuration
@EnableSwagger2

public class SpringFoxConfig {

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${swagger.auth.token-url:}")
    private String authTokenUrl;

    @Value("${swagger.auth.auth-url:}")
    private String authUrl;

    @Value("${swagger.auth.client-id:}")
    private String authClientId;

    @Value("${swagger.auth.secret:}")
    private String secret;

    private String path = "/api/**";
    private String pathReg = "/api/.*";

    private String title = "Kaliber APi";
    private String description = "Projekt";
    private String version = "1.0";

    private static final String GROUP_NAME = "Kaliber-Api";
    private static final String OAUTH_NAME = "spring_oauth";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(true)
                .apiInfo(apiInfo())
                .select()
                .paths(PathSelectors.ant(path))
                .build()
                .securityContexts(Lists.newArrayList(securityContext()))
                .securitySchemes(Lists.newArrayList(securityScheme()));
    }

    @Bean
    public SecurityConfiguration security() {
        return SecurityConfigurationBuilder.builder()
                .realm(realm)
                .clientId(authClientId)
                .clientSecret(secret)
                .appName(GROUP_NAME)
                .scopeSeparator(" ")
                .useBasicAuthenticationWithAccessCodeGrant(true)
                .build();
    }


    //@Bean
    //public SecurityConfiguration securityConfiguration() {
      //  return new SecurityConfiguration(authClientId, "", realm, "", "", Collections.emptyMap(), false);
    //}


    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(Arrays.asList(new SecurityReference(OAUTH_NAME, scopes())))
                .forPaths(PathSelectors.ant(path))
                .build();
    }

    private SecurityScheme securityScheme() {
        GrantType grantType =
                new AuthorizationCodeGrantBuilder()
                        .tokenEndpoint(new TokenEndpoint(authTokenUrl, GROUP_NAME))
                        .tokenRequestEndpoint(
                                new TokenRequestEndpoint(authUrl, authClientId, secret))
                        .build();

        SecurityScheme oauth =
                new OAuthBuilder()
                        .name(OAUTH_NAME)
                        .grantTypes(Arrays.asList(grantType))
                        .scopes(Arrays.asList(scopes()))
                        .build();
        return oauth;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(title)
                .description(description)
                .version(version)
                .build();
    }

    private AuthorizationScope[] scopes() {
        AuthorizationScope[] scopes = {
                //new AuthorizationScope("user", "for CRUD operations"),
                //new AuthorizationScope("read", "for read operations"),
                // AuthorizationScope("write", "for write operations")
        };
        return scopes;
    }

}
