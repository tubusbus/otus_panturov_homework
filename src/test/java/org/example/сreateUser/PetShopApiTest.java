package org.example.сreateUser;

import dto.User;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import services.UserApi;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

public class PetShopApiTest {

  @ParameterizedTest(name = "{index} - Создаие пользователя {0}")
  @CsvSource({
          "user,Customer,Customerov,usersmail@mail.ru,pass,8(800)555-35-35"
  })
  public void createUser(String username, String firstName, String lastName, String email, String password, String phone) {
    UserApi userApi = new UserApi();
    User user = User.builder()
            .id(1)
            .username(username)
            .firstName(firstName)
            .lastName(lastName)
            .email(email)
            .password(password)
            .phone(phone)
            .build();
    ValidatableResponse response = userApi.createUser(user);
    response.statusCode(200);
  }

  @ParameterizedTest(name = "{index} - создание, проверка и удаление пользователя {0}")
  @CsvSource({
          "proverka,Ivan,Ivanov,kakoynibud@mail.ru,password,+7(999)999-99-01"
  })
  public void getUser(String username, String firstName, String lastName, String email, String password, String phone) {
    UserApi userApi = new UserApi();
    User user = User.builder()
            .id(152)
            .username(username)
            .firstName(firstName)
            .lastName(lastName)
            .email(email)
            .password(password)
            .phone(phone)
            .build();
    ValidatableResponse response = userApi.createUser(user);
    response.statusCode(200);

    response = userApi.getUser(username);
    response
            .statusCode(200)
            .time(lessThan(2000L))
            .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schema/GetUser.json"))
            .body("id", equalTo(152))
            .body("username", equalTo(username))
            .body("firstName", equalTo(firstName))
            .body("lastName", equalTo(lastName))
            .body("email", equalTo(email))
            .body("password", equalTo(password))
            .body("phone", equalTo(phone))
            .body("userStatus", equalTo(0));

    response = userApi.deleteUser(username);
    response
            .statusCode(200)
            .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schema/DeleteUser.json"))
            .body("code", equalTo(200))
            .body("type", equalTo("unknown"))
            .body("message", equalTo(username));
  }

}
