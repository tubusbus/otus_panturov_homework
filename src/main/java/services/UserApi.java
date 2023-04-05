package services;

import dto.User;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import static enums.UrlsEnum.*;
import static io.restassured.RestAssured.given;

public class UserApi {
  private RequestSpecification spec;

  public UserApi() {
    spec = given()
            .baseUri(BASE_URL.getValue())
            .contentType(ContentType.JSON);
  }

  public ValidatableResponse createUser(User user) {
    return given(spec)
            .basePath(USER_PATH.getValue())
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
            .basePath(USER_PATH.getValue())
            .get(String.format("/%s", userName))
            .then()
            .log()
            .all();
  }

  public ValidatableResponse deleteUser(String userName) {
    return given(spec)
            .basePath(USER_PATH.getValue())
            .delete(String.format("/%s", userName))
            .then()
            .log()
            .all();
  }

}
