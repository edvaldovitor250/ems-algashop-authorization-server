package com.algaworks.algashop.infrastructure.security.query;

public interface ClientAllowedQueryService {

    Set<String> findByRole(AuthUserType authUserType);
}
