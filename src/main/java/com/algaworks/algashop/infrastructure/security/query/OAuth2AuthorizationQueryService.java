package com.algaworks.algashop.infrastructure.security.query;

public interface OAuth2AuthorizationQueryService {

    List<String> findAuthorizedIds(String principalName);

}
