package com.algaworks.algashop.authorizationserver.presentation;

import com.algaworks.algashop.authorizationserver.application.user.management.AuthUserInput;
import com.algaworks.algashop.authorizationserver.application.user.management.AuthUserManagementApplicationService;
import com.algaworks.algashop.authorizationserver.application.user.query.AuthUserFilter;
import com.algaworks.algashop.authorizationserver.application.user.query.AuthUserOutput;
import com.algaworks.algashop.authorizationserver.application.user.query.AuthUserQueryService;
import com.algaworks.algashop.authorizationserver.domain.model.user.AuthUserType;
import com.algaworks.algashop.authorizationserver.infrastructure.security.check.SecurityAnnotations;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

	private final AuthUserQueryService queryService;
	private final AuthUserManagementApplicationService managementService;

	@GetMapping
	@SecurityAnnotations.CanReadUsers
	public Page<AuthUserOutput> list(
			@RequestParam(required = false) String email,
			@RequestParam(required = false) AuthUserType type,
			@PageableDefault(size = 20) Pageable pageable) {
		AuthUserFilter filter = new AuthUserFilter(email, type);
		return queryService.list(filter, pageable);
	}

	@GetMapping("/{userId}")
	@SecurityAnnotations.CanReadUsers
	public AuthUserOutput findById(@PathVariable UUID userId) {
		return queryService.findById(userId);
	}

	@DeleteMapping("/{userId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@SecurityAnnotations.CanWriteUsers
	public void delete(@PathVariable UUID userId) {
		managementService.anonymize(userId);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@SecurityAnnotations.CanWriteUsers
	public AuthUserOutput create(@RequestBody @Valid AuthUserInput input) {
		return managementService.create(input);
	}

}
