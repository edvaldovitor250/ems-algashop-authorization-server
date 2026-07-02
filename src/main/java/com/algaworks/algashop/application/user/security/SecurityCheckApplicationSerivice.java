package com.algaworks.algashop.application.user.security;

public interface SecurityCheckApplicationSerivice {

    UUID getAuthenticatedUserId();
    boolean isAuthenticated();
    boolean isMachineAuthenticated();

}
