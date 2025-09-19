package steps;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.RestAssured;

import static io.restassured.RestAssured.given;

public class OrderSteps {

    private final String BASE_URI = "https://qa-scooter.praktikum-services.ru";
    private final String POST_CREATE = "/api/v1/orders";

    public Response createOrder(String[] color) {
        Order order = new Order(color);

        return RestAssured.given()
                .baseUri(BASE_URI)
                .contentType(ContentType.JSON)
                .body(order)
                .when()
                .post(POST_CREATE);
    }
}