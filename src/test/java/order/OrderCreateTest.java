package order;

import client.OrderClient;
import client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import model.Order;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;

public class OrderCreateTest {

    private OrderClient orderClient = new OrderClient();
    private UserClient userClient = new UserClient();
    private String accessToken;

    @Before
    public void setUp() {
        User user = new User("order" + System.currentTimeMillis() + "@test.ru", "password123", "TestUser");
        var response = userClient.create(user);
        accessToken = response.jsonPath().getString("accessToken");
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.delete(accessToken);
        }
    }

    @Test
    @DisplayName("Создание заказа с авторизацией")
    public void createOrderWithAuthReturns200() {
        Order order = new Order(List.of("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f"));
        orderClient.create(order, accessToken)
                .then().statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void createOrderWithoutAuthReturns200() {
        Order order = new Order(List.of("61c0c5a71d1f82001bdaaa6d"));
        orderClient.createWithoutAuth(order)
                .then().statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами")
    public void createOrderWithIngredientsReturns200() {
        Order order = new Order(List.of("61c0c5a71d1f82001bdaaa6d"));
        orderClient.create(order, accessToken)
                .then().statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void createOrderWithoutIngredientsReturns400() {
        Order order = new Order(List.of());
        orderClient.create(order, accessToken)
                .then().statusCode(400)
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    public void createOrderWithWrongIngredientsReturns400() {
        Order order = new Order(List.of("wronghash123"));
        orderClient.create(order, accessToken)
                .then().statusCode(400);
    }
}