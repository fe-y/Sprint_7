package ru.yandex.praktikum.steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import ru.yandex.praktikum.models.Order;

import static io.restassured.RestAssured.given;

public class OrderSteps {

    private static final String ORDERS = "/orders";

    @Step("Создание заказа: {order}")
    public Response createOrder(Order order) {
        return given()
                .contentType(ContentType.JSON)
                .body(order)
                .when()
                .post(ORDERS);
    }

    @Step("Создание заказа с цветами: {color}")
    public Response createOrder(String[] color) {
        Order order = new Order();
        order.setColor(color);
        return createOrder(order);
    }

    @Step("Получение списка заказов")
    public Response getOrdersList() {
        return given()
                .when()
                .get(ORDERS);
    }

    @Step("Отмена заказа с треком: {track}")
    public Response cancelOrder(int track) {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .put(ORDERS + "/cancel?track=" + track);
    }
}