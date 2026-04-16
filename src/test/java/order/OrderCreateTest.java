package order;

import client.OrderClient;
import client.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import model.Order;
import model.User;
import org.apache.http.HttpStatus;
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
    @Description("Проверяет создание заказа с токеном авторизации, ожидается код 200")
    public void createOrderWithAuthReturns200Test() {
        Order order = new Order(List.of("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f"));
        orderClient.create(order, accessToken)
                .then().statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Проверяет создание заказа без токена авторизации, ожидается код 200")
    public void createOrderWithoutAuthReturns200Test() {
        Order order = new Order(List.of("61c0c5a71d1f82001bdaaa6d"));
        orderClient.createWithoutAuth(order)
                .then().statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами")
    @Description("Проверяет создание заказа с корректными ингредиентами, ожидается код 200")
    public void createOrderWithIngredientsReturns200Test() {
        Order order = new Order(List.of("61c0c5a71d1f82001bdaaa6d"));
        orderClient.create(order, accessToken)
                .then().statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Проверяет создание заказа с пустым списком ингредиентов, ожидается код 400")
    public void createOrderWithoutIngredientsReturns400Test() {
        Order order = new Order(List.of());
        orderClient.create(order, accessToken)
                .then().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    @Description("Проверяет создание заказа с невалидным хешем ингредиентов, ожидается код 500")
    public void createOrderWithWrongIngredientsReturns500Test() {
        Order order = new Order(List.of("randomhash"));
        orderClient.create(order, accessToken)
                .then().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }
}