package com.algaworks.algashop.authorizationserver.application.user.query;

import com.algaworks.algashop.authorizationserver.domain.model.user.AuthUser;
import com.algaworks.algashop.authorizationserver.domain.model.user.AuthUserRepository;
import com.algaworks.algashop.authorizationserver.domain.model.user.AuthUserType;
import com.algaworks.algashop.authorizationserver.utils.TestcontainerPostgreSQLConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Import(TestcontainerPostgreSQLConfig.class)
@Sql(scripts = "classpath:db/testdata/afterMigrate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class AuthUserQueryServiceIT {

	@Autowired
	private AuthUserQueryService queryService;

	@Autowired
	private AuthUserRepository authUserRepository;

	private UUID existingUserId;

	@BeforeEach
	void setUp() {
		AuthUser user = AuthUser.brandNew(
				"test@example.com",
				"Test User",
				AuthUserType.CUSTOMER,
				"hashedpassword"
		);
		existingUserId = authUserRepository.save(user).getId();
	}

	@Test
	void shouldFindUserById() {
		AuthUserOutput output = queryService.findById(existingUserId);

		assertThat(output).isNotNull();
		assertThat(output.getId()).isEqualTo(existingUserId);
		assertThat(output.getName()).isEqualTo("Test User");
		assertThat(output.getEmail()).isEqualTo("test@example.com");
		assertThat(output.getType()).isEqualTo(AuthUserType.CUSTOMER);
		assertThat(output.isEnabled()).isTrue();
	}

	@Test
	void shouldThrowExceptionWhenUserNotFound() {
		UUID nonExistentId = UUID.randomUUID();

		assertThatThrownBy(() -> queryService.findById(nonExistentId))
				.isInstanceOf(AuthUserNotFoundException.class);
	}

	@Test
	void shouldListUsersWithEmailFilter() {
		AuthUserFilter filter = new AuthUserFilter();
		filter.setEmail("test");
		Pageable pageable = PageRequest.of(0, 20);

		Page<AuthUserOutput> result = queryService.list(filter, pageable);

		assertThat(result).isNotNull();
		assertThat(result.getContent()).isNotEmpty();
		assertThat(result.getContent().stream()
				.allMatch(u -> u.getEmail().contains("test"))).isTrue();
	}

	@Test
	void shouldListUsersWithTypeFilter() {
		AuthUserFilter filter = new AuthUserFilter();
		filter.setType(AuthUserType.CUSTOMER);
		Pageable pageable = PageRequest.of(0, 20);

		Page<AuthUserOutput> result = queryService.list(filter, pageable);

		assertThat(result).isNotNull();
		assertThat(result.getContent()).isNotEmpty();
		assertThat(result.getContent().stream()
				.allMatch(u -> u.getType() == AuthUserType.CUSTOMER)).isTrue();
	}

	@Test
	void shouldListAllUsersWithPagination() {
		AuthUserFilter filter = new AuthUserFilter();
		Pageable pageable = PageRequest.of(0, 10);

		Page<AuthUserOutput> result = queryService.list(filter, pageable);

		assertThat(result).isNotNull();
		assertThat(result.getContent()).isNotEmpty();
		assertThat(result.getNumber()).isEqualTo(0);
		assertThat(result.getSize()).isEqualTo(10);
	}

}
