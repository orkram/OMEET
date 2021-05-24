package com.orange.OrangeCommunicatorBackend.config;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;


@KeycloakConfiguration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class KeycloakConfig extends KeycloakWebSecurityConfigurerAdapter {

    @Autowired
    protected void configureGlobal(final AuthenticationManagerBuilder auth) {
        final KeycloakAuthenticationProvider provider = super.keycloakAuthenticationProvider();
        provider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
        auth.authenticationProvider(provider);
    }

    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        //return new NullAuthenticatedSessionStrategy();
        return new RegisterSessionAuthenticationStrategy(
                new SessionRegistryImpl());
    }


    @Bean
    public KeycloakConfigResolver keycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
       // web.ignoring().antMatchers("/v2/api-docs",
                //"/swagger-ui.html",
               // "/swagger-ui/**");
        web.ignoring().antMatchers("/api/v1/account/login/**")
                .antMatchers("/api/v1/account/register/**")
                .antMatchers("/api/v1/account/{username}/logout/**")
                .antMatchers("/api/v1/account/{username}/refresh-token/**")
                .antMatchers("/api/v1/contacts/add/**");
        web.ignoring().antMatchers("/v2/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**")
                .antMatchers("/resources/**")
                .antMatchers("/style.css")
                .antMatchers("/logoV1res.png")
                .antMatchers("**/favicon.ico");

    }

    @Override
    protected void configure(final HttpSecurity httpSecurity) throws Exception {
        super.configure(httpSecurity);
        httpSecurity
                .cors().and()
                .csrf().disable()
                .authorizeRequests()
                //.antMatchers("api/v1/account/login/**")
                //.permitAll()
                //.antMatchers("api/v1/account/register/**")
                //.permitAll()
                .antMatchers("/api/v1/**").fullyAuthenticated()
                .antMatchers( "/resources/**").permitAll()
                .antMatchers("**/favicon.ico").permitAll()
                .anyRequest().permitAll();
    }

}
