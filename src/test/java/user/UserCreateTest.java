package user;

import client.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import model.User;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class UserCreateTest {

    private UserClient userClient = new UserClient();
    private String accessToken;

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.delete(accessToken);
        }
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    @Description("Проверяет успешное создание уникального пользователя, ожидается код 200")
    public void createUniqueUserReturns200Test() {
        User user = new User("unique" + System.currentTimeMillis() + "@test.ru", "password123", "TestUser");
        var response = userClient.create(user);
        accessToken = response.jsonPath().getString("accessToken");
        response.then().statusCode(HttpStatus.SC_OK).body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание уже зарегистрированного пользователя")
    @Description("Проверяет создание пользователя с уже существующим email, ожидается код 403")
    public void createExistingUserReturns403Test() {
        User user = new User("existing" + System.currentTimeMillis() + "@test.ru", "password123", "TestUser");
        var firstResponse = userClient.create(user);
        accessToken = firstResponse.jsonPath().getString("accessToken");
        userClient.create(user)
                .then().statusCode(HttpStatus.SC_FORBIDDEN)
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя без email")
    @Description("Проверяет создание пользователя без поля email, ожидается код 403")
    public void createUserWithoutEmailReturns403Test() {
        User user = new User(null, "password123", "TestUser");
        userClient.create(user)
                .then().statusCode(HttpStatus.SC_FORBIDDEN)
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без пароля")
    @Description("Проверяет создание пользователя без поля password, ожидается код 403")
    public void createUserWithoutPasswordReturns403Test() {
        User user = new User("nopassword" + System.currentTimeMillis() + "@test.ru", null, "TestUser");
        userClient.create(user)
                .then().statusCode(HttpStatus.SC_FORBIDDEN)
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без имени")
    @Description("Проверяет создание пользователя без поля name, ожидается код 403")
    public void createUserWithoutNameReturns403Test() {
        User user = new User("noname" + System.currentTimeMillis() + "@test.ru", "password123", null);
        userClient.create(user)
                .then().statusCode(HttpStatus.SC_FORBIDDEN)
                .body("message", equalTo("Email, password and name are required fields"));
    }
}