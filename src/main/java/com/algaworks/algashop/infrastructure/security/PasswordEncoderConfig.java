package com.algaworks.algashop.infrastructure.security;

@Configuration
public class PasswordEncoderConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
       Map<String, PasswordEncoder> encoders = new HashMap<>();
       encoders.put("bcrypt", new BCryptPasswordEncoder());
         encoders.put("noop", new NoOpPasswordEncoder());
         return new DelegatingPasswordEncoder("bcrypt", encoders);
    }

}
