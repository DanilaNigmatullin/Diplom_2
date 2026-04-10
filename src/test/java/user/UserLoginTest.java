package user;

import client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import model.User;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class UserLoginTest {

    private UserClient userClient = new UserClient();
    private String accessToken;

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.delete(accessToken);
        }
    }

    @Test
    @DisplayName("Вход под существующим пользователем")
    public void loginExistingUserReturns200() {
        User user = new User("login" + System.currentTimeMillis() + "@test.ru", "password123", "TestUser");
        var createResponse = userClient.create(user);
        accessToken = createResponse.jsonPath().getString("accessToken");
        userClient.login(user)
                .then().statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Вход с неверным логином и паролем")
    public void loginWithWrongCredentialsReturns401() {
        User user = new User("wrong@test.ru", "wrongpassword", null);
        userClient.login(user)
                .then().statusCode(401)
                .body("message", equalTo("email or password are incorrect"));
    }
}
