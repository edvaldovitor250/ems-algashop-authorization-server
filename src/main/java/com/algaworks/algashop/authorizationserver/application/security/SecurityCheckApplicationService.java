package com.algaworks.algashop.authorizationserver.application.security;

import java.util.UUID;

import com.algaworks.algashop.authorizationserver.domain.model.AuthUserType;

public interface SecurityCheckApplicationService {

    UUID getAuthenticatedUserId();
    boolean isAuthenticated();
    boolean isMachineAuthenticated();
    boolean canAccessOwnProfile();
    boolean canRegisterUserOfType(AuthUserType userType);

}
