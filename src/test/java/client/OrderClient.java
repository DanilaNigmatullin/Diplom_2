package client;

import io.restassured.response.Response;
import model.Order;

import static client.Endpoints.*;
import static io.restassured.RestAssured.given;

public class OrderClient {

    private static final String BASE_URL = "https://stellarburgers.education-services.ru";

    public Response create(Order order, String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .body(order)
                .post(BASE_URL + ORDERS);
    }

    public Response createWithoutAuth(Order order) {
        return given()
                .header("Content-type", "application/json")
                .body(order)
                .post(BASE_URL + ORDERS);
    }
}