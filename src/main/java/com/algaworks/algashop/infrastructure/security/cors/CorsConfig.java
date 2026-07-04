package com.algaworks.algashop.infrastructure.security.cors;

@Configuration
public class CorsConfig {

    public CorsConfigurationSource corsConfigurationSource(AlgaShopSecurityProperties securityProperties) {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(securityProperties.getCors().getAllowedOrigins());
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    
}
