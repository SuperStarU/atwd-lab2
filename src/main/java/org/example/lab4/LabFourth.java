package org.example.lab4;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import java.util.Map;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class LabFourth {

  private static final String baseUrl = "https://27123afa-0c26-4240-8a9c-ae41886f86ef.mock.pstmn.io";
  private static final String USER = "/user";

  @BeforeClass
  public void setUp() {
    RestAssured.baseURI = baseUrl;
    RestAssured.defaultParser = Parser.JSON;
    RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
    RestAssured.responseSpecification = new ResponseSpecBuilder().build();
  }

  @Test
  public void getUser_success() {
    given().param("id", 1)
        .get(baseUrl + USER)
        .then()
        .statusCode(HttpStatus.SC_OK)
        .and()
        .body("fname", equalTo("Nikita"))
        .and()
        .body("lname", equalTo("Horbenko"));
  }

  @Test
  public void getUser_userNotFound() {
    given().param("id", 2)
        .get(baseUrl + USER)
        .then()
        .statusCode(HttpStatus.SC_NOT_FOUND)
        .and()
        .body("exception", equalTo("User not found"));
  }

  @Test
  public void createUser_success() {
    Map<String, ?> body = Map.of(
        "fname", "Nikita",
        "lname", "Horbenko",
        "group", "122m-23-2"
    );
    given().body(body)
        .post(baseUrl + USER)
        .then()
        .statusCode(HttpStatus.SC_CREATED);
  }

  @Test
  public void createUser_groupIsInvalid() {
    Map<String, ?> body = Map.of(
        "fname", "Nikita",
        "lname", "Horbenko",
        "group", "122m-25-2"
    );
    given().body(body)
        .post(baseUrl + USER)
        .then()
        .statusCode(HttpStatus.SC_BAD_REQUEST)
        .and()
        .body("exception", equalTo("Group is invalid"));
  }

  @Test
  public void updateUser_success() {
    Map<String, ?> body = Map.of(
        "fname", "29",
        "lname", "Horbenko",
        "group", "122m-25-2"
    );
    given().param("id", 1)
        .body(body)
        .put(baseUrl + USER)
        .then()
        .statusCode(HttpStatus.SC_OK)
        .and()
        .body("fname", equalTo("29"));
  }

  @Test
  public void updateUser_notAuthorized() {
    Map<String, ?> body = Map.of(
        "fname", "29",
        "lname", "Horbenko",
        "group", "122m-25-2"
    );
    given().param("id", 2)
        .body(body)
        .put(baseUrl + USER)
        .then()
        .statusCode(HttpStatus.SC_FORBIDDEN)
        .and()
        .body("exception", equalTo("You are not authorized as user 2"));
  }

  @Test
  public void deleteUser_success() {
    given().pathParam("id", 1)
        .delete(baseUrl + USER + "/{id}")
        .then()
        .statusCode(HttpStatus.SC_OK)
        .and()
        .body("message", equalTo("User deleted"));
  }

  @Test
  public void deleteUser_notAuthorized() {
    given().pathParam("id", 2)
        .delete(baseUrl + USER + "/{id}")
        .then()
        .statusCode(HttpStatus.SC_FORBIDDEN)
        .and()
        .body("exception", equalTo("You are not authorized as user 2"));
  }
}
