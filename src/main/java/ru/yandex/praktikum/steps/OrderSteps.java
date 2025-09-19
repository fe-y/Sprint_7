package ru.yandex.praktikum.steps;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import ru.yandex.praktikum.models.Order;

import static io.restassured.RestAssured.given;

public class OrderSteps {

    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";
    private static final String ORDERS = "/api/v1/orders";

    public Response createOrder(Order order) {
        return given()
                .contentType(ContentType.JSON)
                .body(order)
                .when()
                .post(BASE_URL + ORDERS);
    }

    public Response createOrder(String[] color) {
        Order order = new Order();
        order.setColor(color);
        return given()
                .contentType(ContentType.JSON)
                .body(order)
                .when()
                .post(BASE_URL + ORDERS);
    }

    public Response getOrdersList() {
        return given()
                .when()
                .get(BASE_URL + ORDERS);
    }
}