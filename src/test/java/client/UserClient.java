package client;

import io.restassured.response.Response;
import model.User;

import static io.restassured.RestAssured.given;

public class UserClient {

    private static final String BASE_URL = "https://stellarburgers.education-services.ru";

    public Response create(User user) {
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .post(BASE_URL + "/api/auth/register");
    }

    public Response login(User user) {
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .post(BASE_URL + "/api/auth/login");
    }

    public void delete(String accessToken) {
        given()
                .header("Authorization", accessToken)
                .delete(BASE_URL + "/api/auth/user");
    }
}