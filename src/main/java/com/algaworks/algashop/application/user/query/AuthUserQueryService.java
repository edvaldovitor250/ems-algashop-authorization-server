package com.algaworks.algashop.application.user.query;

import com.algaworks.algashop.authorizationserver.domain.model.user.AuthUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthUserQueryService {

	private final AuthUserRepository authUserRepository;

	public AuthUserOutput findById(UUID userId) {
		return authUserRepository.findById(userId)
				.map(AuthUserOutput::from)
				.orElseThrow(() -> new AuthUserNotFoundException(userId));
	}

	public Page<AuthUserOutput> list(AuthUserFilter filter, Pageable pageable) {
		Page<com.algaworks.algashop.authorizationserver.domain.model.user.AuthUser> page;

		if (filter.getEmail() != null && filter.getType() != null) {
			page = authUserRepository.findByEmailContainingAndType(filter.getEmail(), filter.getType(), pageable);
		} else if (filter.getEmail() != null) {
			page = authUserRepository.findByEmailContaining(filter.getEmail(), pageable);
		} else if (filter.getType() != null) {
			page = authUserRepository.findByType(filter.getType(), pageable);
		} else {
			page = authUserRepository.findAll(pageable);
		}

		return page.map(AuthUserOutput::from);
	}

}
