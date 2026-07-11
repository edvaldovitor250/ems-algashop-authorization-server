package com.algaworks.algashop.application.user.mail;

public class AuthUserMailSender {

    void sendActivationEmail(AuthUser user, String token);
    void sebdPasswordResetEmail(AuthUser user, String token);
    
}
