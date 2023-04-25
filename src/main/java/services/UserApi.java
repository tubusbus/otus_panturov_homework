package services;

import dto.User;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class UserApi {
  private RequestSpecification spec;
 final String baseUrl = System.getProperty("base.url","https://petstore.swagger.io/v2");
 final String userPath = "/user";

  public UserApi() {
    spec = given()
            .baseUri(baseUrl)
            .contentType(ContentType.JSON);
  }

  public ValidatableResponse createUser(User user) {
    return given(spec)
            .basePath(userPath)
            .body(user)
            .log().all()
            .when()
            .post()
            .then()
            .log()
            .all();
  }

  public ValidatableResponse getUser(String userName) {
    return given(spec)
            .basePath(userPath)
            .get(String.format("/%s", userName))
            .then()
            .log()
            .all();
  }

  public ValidatableResponse deleteUser(String userName) {
    return given(spec)
            .basePath(userPath)
            .delete(String.format("/%s", userName))
            .then()
            .log()
            .all();
  }

}
