package com.algaworks.algashop.authorizationserver.contract.base;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.UUID;

class UserControllerContractTest extends UserBase {

	@Test
	void listUsersContract() {
		RestAssuredMockMvc.given()
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.queryParam("page", 0)
				.queryParam("size", 20)
			.when()
				.get("/api/v1/users")
			.then()
				.assertThat()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.statusCode(HttpStatus.OK.value())
				.body(
						"content", Matchers.notNullValue(),
						"content[0].id", Matchers.notNullValue(),
						"content[0].name", Matchers.is("John Doe"),
						"content[0].email", Matchers.is("john@example.com"),
						"content[0].type", Matchers.is("CUSTOMER"),
						"content[0].enabled", Matchers.is(true)
				);
	}

	@Test
	void findUserByIdContract() {
		RestAssuredMockMvc.given()
				.accept(MediaType.APPLICATION_JSON_VALUE)
			.when()
				.get("/api/v1/users/{userId}", VALID_USER_ID)
			.then()
				.assertThat()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.statusCode(HttpStatus.OK.value())
				.body(
						"id", Matchers.is(VALID_USER_ID.toString()),
						"name", Matchers.is("John Doe"),
						"email", Matchers.is("john@example.com"),
						"type", Matchers.is("CUSTOMER"),
						"enabled", Matchers.is(true)
				);
	}

	@Test
	void findUserByIdNotFoundContract() {
		RestAssuredMockMvc.given()
				.accept(MediaType.APPLICATION_JSON_VALUE)
			.when()
				.get("/api/v1/users/{userId}", NOT_FOUND_USER_ID)
			.then()
				.assertThat()
				.contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
				.statusCode(HttpStatus.NOT_FOUND.value())
				.body(
						"status", Matchers.is(HttpStatus.NOT_FOUND.value()),
						"type", Matchers.is("/errors/user-not-found"),
						"title", Matchers.notNullValue(),
						"instance", Matchers.notNullValue()
				);
	}

	@Test
	void deleteUserContract() {
		RestAssuredMockMvc.given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when()
				.delete("/api/v1/users/{userId}", VALID_USER_ID)
			.then()
				.assertThat()
				.statusCode(HttpStatus.NO_CONTENT.value());
	}

	@Test
	void deleteUserNotFoundContract() {
		RestAssuredMockMvc.given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when()
				.delete("/api/v1/users/{userId}", NOT_FOUND_USER_ID)
			.then()
				.assertThat()
				.contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
				.statusCode(HttpStatus.NOT_FOUND.value())
				.body(
						"status", Matchers.is(HttpStatus.NOT_FOUND.value()),
						"type", Matchers.is("/errors/user-not-found"),
						"title", Matchers.notNullValue(),
						"instance", Matchers.notNullValue()
				);
	}

	@Test
	void getMyProfileContract() {
		RestAssuredMockMvc.given()
				.accept(MediaType.APPLICATION_JSON_VALUE)
			.when()
				.get("/api/v1/users/me")
			.then()
				.assertThat()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.statusCode(HttpStatus.OK.value())
				.body(
						"id", Matchers.is(VALID_USER_ID.toString()),
						"name", Matchers.is("John Doe"),
						"email", Matchers.is("john@example.com"),
						"type", Matchers.is("CUSTOMER"),
						"enabled", Matchers.is(true)
				);
	}

	@Test
	void updateMyProfileContract() {
		String jsonInput = """
		{
		  "name": "Jane Doe"
		}
		""";

		RestAssuredMockMvc.given()
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(jsonInput)
			.when()
				.put("/api/v1/users/me")
			.then()
				.assertThat()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.statusCode(HttpStatus.OK.value())
				.body(
						"id", Matchers.is(VALID_USER_ID.toString()),
						"name", Matchers.is("John Doe"),
						"email", Matchers.is("john@example.com"),
						"type", Matchers.is("CUSTOMER"),
						"enabled", Matchers.is(true)
				);
	}

	@Test
	void deleteMyProfileContract() {
		RestAssuredMockMvc.given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when()
				.delete("/api/v1/users/me")
			.then()
				.assertThat()
				.statusCode(HttpStatus.NO_CONTENT.value());
	}

}
