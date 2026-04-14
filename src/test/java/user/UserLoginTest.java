package user;

import client.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import model.User;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class UserLoginTest {

    private UserClient userClient = new UserClient();
    private String accessToken;
    private User user;

    @Before
    public void setUp() {
        user = new User("login" + System.currentTimeMillis() + "@test.ru", "password123", "TestUser");
        var createResponse = userClient.create(user);
        accessToken = createResponse.jsonPath().getString("accessToken");
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.delete(accessToken);
        }
    }

    @Test
    @DisplayName("Вход под существующим пользователем")
    @Description("Проверяет успешный вход под существующим пользователем, ожидается код 200")
    public void loginExistingUserReturns200Test() {
        userClient.login(user)
                .then().statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Вход с неверным логином")
    @Description("Проверяет вход с несуществующим email, ожидается код 401")
    public void loginWithWrongLoginReturns401Test() {
        User wrongUser = new User("wrong@test.ru", "password123", null);
        userClient.login(wrongUser)
                .then().statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Вход с неверным паролем")
    @Description("Проверяет вход с неверным паролем, ожидается код 401")
    public void loginWithWrongPasswordReturns401Test() {
        User wrongUser = new User(user.getEmail(), "wrongpassword", null);
        userClient.login(wrongUser)
                .then().statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("message", equalTo("email or password are incorrect"));
    }
}