package steps;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.RestAssured;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class CourierSteps {

    private final String BASE_URI = "https://qa-scooter.praktikum-services.ru";
    private final String POST_CREATE = "/api/v1/courier";
    private final String POST_LOGIN = "/api/v1/courier/login";
    private final String DELETE_DELETE = "/api/v1/courier/{id}";

    public Response createCourier(String login, String password, String firstName) {
        Map<String, String> body = new HashMap<>();
        body.put("login", login);
        body.put("password", password);
        body.put("firstName", firstName);

        return RestAssured.given()
                .baseUri(BASE_URI)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(POST_CREATE);
    }

    public ValidatableResponse loginCourier(String login, String password) {
        Map<String, String> body = new HashMap<>();
        body.put("login", login);
        body.put("password", password);

        return RestAssured.given()
                .baseUri(BASE_URI)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(POST_LOGIN)
                .then();
    }

    public ValidatableResponse deleteCourier(String id) {
        return RestAssured.given()
                .baseUri(BASE_URI)
                .contentType(ContentType.JSON)
                .pathParam("id", id)
                .when()
                .delete(DELETE_DELETE)
                .then();
    }
}