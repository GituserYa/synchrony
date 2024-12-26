package com.example.synchrony;

import org.apache.camel.CamelContext;
import org.apache.camel.spring.SpringCamelContext;
import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication

public class SynchronyApplication  {

@Autowired

    public static void main(String[] args) {
        SpringApplication.run(SynchronyApplication.class, args);
    }

//    @Bean
//    public SecurityFilterChain securityFilterChainAs(HttpSecurity http) throws Exception {
//        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
//        return http.formLogin().and().build();
//    }
@Bean
public CamelContext camelContext() throws Exception {
    return new SpringCamelContext();
}
    @Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

//    @Bean
//    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception{
//    http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry.anyRequest().authenticated()).formLogin(Customizer.withDefaults());
//
//        return http.build();
    }

//    public void bindAuthenticationProvider(AuthenticationManagerBuilder authenticationManagerBuilder){
//        authenticationManagerBuilder.authenticationProvider(customAuthenticationProvider);
//
//    }



