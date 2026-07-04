package com.algaworks.algashop.infrastructure.security;

@Component
@Data
@Validated
@ConfigurationProperties(prefix = "algashop.security")
@NoArgsConstructor
public class AlgaShopSecurityProperties {

    @NotNull
    @Valid
    private CorsProperties cors;

    @Data
    @NoArgsConstructor
public static class CorsProperties{
    @NotNull
    private List<String> allowedOrigins = new ArrayList<>();
}

}
