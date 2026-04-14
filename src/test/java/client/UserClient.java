package client;

import io.restassured.response.Response;
import model.User;

import static client.Endpoints.*;
import static io.restassured.RestAssured.given;

public class UserClient {

    private static final String BASE_URL = "https://stellarburgers.education-services.ru";

    public Response create(User user) {
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .post(BASE_URL + REGISTER);
    }

    public Response login(User user) {
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .post(BASE_URL + LOGIN);
    }

    public void delete(String accessToken) {
        given()
                .header("Authorization", accessToken)
                .delete(BASE_URL + USER);
    }
}