package org.example.lab3;

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

public class PetTest {

  private static final String baseUrl = "https://petstore.swagger.io/v2";

  private static final String PET = "/pet";
  private static final String PET_ID = PET + "/{petId}";

  @BeforeClass
  public void setUp() {
    RestAssured.baseURI = baseUrl;
    RestAssured.defaultParser = Parser.JSON;
    RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
    RestAssured.responseSpecification = new ResponseSpecBuilder().build();
  }

  @Test
  public void verifyCreateAction() {
    Map<String, ?> body = Map.of(
        "id", 29,
        "category", Map.of("name", "N.H."),
        "name", "Nikita",
        "photoUrls", new String[] { "photo" },
        "tags",  new Map[] { Map.of("name", "N.H.") },
        "status", "available"
    );
    given().body(body)
        .post(PET)
        .then()
        .statusCode(HttpStatus.SC_OK);
  }

  @Test(dependsOnMethods = "verifyCreateAction")
  public void verifyGetAction() {
    given().pathParam("petId", 29)
        .get(PET_ID)
        .then()
        .statusCode(HttpStatus.SC_OK)
        .and()
        .body("name", equalTo("Nikita"));
  }

  @Test(dependsOnMethods = "verifyCreateAction")
  public void verifyUpdateAction() {
    Map<String, ?> body = Map.of(
        "id", 29,
        "category", Map.of("name", "N.H."),
        "name", "Nikita Horbenko",
        "photoUrls", new String[] { "photo" },
        "tags",  new Map[] { Map.of("name", "N.H.") },
        "status", "available"
    );
    given().body(body)
        .put(PET)
        .then()
        .statusCode(HttpStatus.SC_OK)
        .and()
        .body("name", equalTo("Nikita Horbenko"));
  }
}
