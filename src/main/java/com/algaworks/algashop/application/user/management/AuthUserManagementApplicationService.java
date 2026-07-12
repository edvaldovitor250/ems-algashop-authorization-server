package com.algaworks.algashop.authorizationserver.application.user.management;

import com.algaworks.algashop.authorizationserver.application.security.SecurityCheckApplicationService;
import com.algaworks.algashop.authorizationserver.application.user.query.AuthUserNotFoundException;
import com.algaworks.algashop.authorizationserver.application.user.query.AuthUserOutput;
import com.algaworks.algashop.authorizationserver.domain.model.user.AuthUser;
import com.algaworks.algashop.authorizationserver.domain.model.user.AuthUserPasswordManager;
import com.algaworks.algashop.authorizationserver.domain.model.user.AuthUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthUserManagementApplicationService {

	private final AuthUserRepository authUserRepository;
	private final SecurityCheckApplicationService securityCheck;
	private final AuthUserPasswordManager passwordManager;

	public AuthUserOutput create(AuthUserInput input) {
		if (!securityCheck.canRegisterUserOfType(input.getType())) {
			throw new AccessDeniedException("Cannot register user of type " + input.getType());
		}

		if (authUserRepository.existsByEmail(input.getEmail())) {
			throw new AuthUserEmailAlreadyInUseException(input.getEmail());
		}

		AuthUser user = AuthUser.brandNew(
				input.getEmail(),
				input.getName(),
				input.getType(),
				passwordManager
		);

		return AuthUserOutput.from(authUserRepository.save(user));
	}

	public AuthUserOutput update(UUID userId, AuthUserUpdateInput input) {
		AuthUser user = authUserRepository.findById(userId)
				.orElseThrow(() -> new AuthUserNotFoundException(userId));

		verifyCanEditUser(user, input);

		user.setName(input.getName());
		user.setType(input.getType());
		user.setEnabled(input.isEnabled());

		return AuthUserOutput.from(authUserRepository.save(user));
	}

	public AuthUserOutput updateOwnProfile(UUID authenticatedUserId, MyUserUpdateInput input) {
		AuthUser user = authUserRepository.findById(authenticatedUserId)
				.orElseThrow(() -> new AuthUserNotFoundException(authenticatedUserId));

		user.setName(input.getName());

		return AuthUserOutput.from(authUserRepository.save(user));
	}

	public void deleteOwnProfile(UUID authenticatedUserId) {
		AuthUser user = authUserRepository.findById(authenticatedUserId)
				.orElseThrow(() -> new AuthUserNotFoundException(authenticatedUserId));

		if (!securityCheck.canDeleteOwnProfile()) {
			throw new AccessDeniedException("Cannot delete own profile");
		}

		user.anonymize();
		authUserRepository.save(user);
	}

	private void verifyCanEditUser(AuthUser authUser, AuthUserUpdateInput input) {
		if (!securityCheck.canEditUser(authUser.getType(), authUser.getId())) {
			throw new AccessDeniedException("Cannot edit user of type " + authUser.getType());
		}

		if (!securityCheck.canChangeUserType(authUser.getType(), input.getType())) {
			throw new AccessDeniedException("Cannot change user type to " + input.getType());
		}
	}

	public void delete(UUID userId) {
		AuthUser user = authUserRepository.findById(userId)
				.orElseThrow(() -> new AuthUserNotFoundException(userId));
		user.anonymize();
		authUserRepository.save(user);
	}

}
