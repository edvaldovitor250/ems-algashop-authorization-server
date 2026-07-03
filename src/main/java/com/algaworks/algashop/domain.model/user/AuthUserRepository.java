package com.algaworks.algashop.domain.model;

import com.algaworks.algashop.authorizationserver.domain.model.user.AuthUser;
import com.algaworks.algashop.authorizationserver.domain.model.user.AuthUserType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AuthUserRepository extends JpaRepository<AuthUser, UUID> {
    Optional<AuthUser> findByEmail(String email);

    boolean existsByEmail(String email);

    Page<AuthUser> findByEmailContaining(String email, Pageable pageable);

    Page<AuthUser> findByType(AuthUserType type, Pageable pageable);

    Page<AuthUser> findByEmailContainingAndType(String email, AuthUserType type, Pageable pageable);

    Page<AuthUser> findAll(Pageable pageable);
}
