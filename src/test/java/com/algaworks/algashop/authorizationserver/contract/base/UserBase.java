package com.algaworks.algashop.authorizationserver.contract.base;

import com.algaworks.algashop.authorizationserver.application.user.management.AuthUserManagementApplicationService;
import com.algaworks.algashop.authorizationserver.application.user.query.AuthUserOutput;
import com.algaworks.algashop.authorizationserver.application.user.query.AuthUserQueryService;
import com.algaworks.algashop.authorizationserver.application.user.query.AuthUserNotFoundException;
import com.algaworks.algashop.authorizationserver.domain.model.user.AuthUserType;
import com.algaworks.algashop.authorizationserver.presentation.UserController;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@WebMvcTest(controllers = UserController.class)
public class UserBase {

	@Autowired
	private WebApplicationContext context;

	@MockitoBean
	private AuthUserQueryService queryService;

	@MockitoBean
	private AuthUserManagementApplicationService managementService;

	public static final UUID VALID_USER_ID = UUID.fromString("52601e34-b849-4f52-b7f5-43f38a3fa83a");

	public static final UUID NOT_FOUND_USER_ID = UUID.fromString("99999999-9999-9999-9999-999999999999");

	@BeforeEach
	void setUp() {
		RestAssuredMockMvc.mockMvc(
				MockMvcBuilders.webAppContextSetup(context)
						.defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
						.build()
		);

		RestAssuredMockMvc.enableLoggingOfRequestAndResponseIfValidationFails();

		AuthUserOutput validUser = AuthUserOutput.builder()
				.id(VALID_USER_ID)
				.name("John Doe")
				.email("john@example.com")
				.type(AuthUserType.CUSTOMER)
				.enabled(true)
				.build();

		Mockito.when(queryService.findById(VALID_USER_ID))
				.thenReturn(validUser);

		Mockito.when(queryService.findById(NOT_FOUND_USER_ID))
				.thenThrow(new AuthUserNotFoundException(NOT_FOUND_USER_ID));

		Mockito.when(queryService.list(Mockito.any(), Mockito.any()))
				.thenReturn(new PageImpl<>(List.of(validUser)));

		Mockito.doNothing().when(managementService).anonymize(VALID_USER_ID);

		Mockito.doNothing().when(managementService).anonymize(NOT_FOUND_USER_ID);
	}

}
