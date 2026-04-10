package user;

import client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import model.User;
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
    public void createUniqueUserReturns200() {
        User user = new User("unique" + System.currentTimeMillis() + "@test.ru", "password123", "TestUser");
        var response = userClient.create(user);
        accessToken = response.jsonPath().getString("accessToken");
        response.then().statusCode(200).body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание уже зарегистрированного пользователя")
    public void createExistingUserReturns403() {
        User user = new User("existing" + System.currentTimeMillis() + "@test.ru", "password123", "TestUser");
        var firstResponse = userClient.create(user);
        accessToken = firstResponse.jsonPath().getString("accessToken");
        userClient.create(user)
                .then().statusCode(403)
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя без обязательного поля")
    public void createUserWithoutRequiredFieldReturns403() {
        User user = new User(null, "password123", "TestUser");
        userClient.create(user)
                .then().statusCode(403)
                .body("message", equalTo("Email, password and name are required fields"));
    }
}