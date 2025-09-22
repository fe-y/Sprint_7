package ru.yandex.praktikum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.apache.http.HttpStatus.SC_OK;

public class ListOrdersTest extends BaseTest {

    private static final String GET_ORDERS = "/orders";

    @Step("Отправка запроса на получение списка заказов")
    public void requestListOrders() {
        given()
                .contentType(JSON)
                .get(GET_ORDERS)
                .then()
                .statusCode(SC_OK)
                .body("orders", notNullValue());
    }

    @Test
    @Description("Проверка, что запрос на получение списка заказов возвращает успешный ответ и не пустой массив orders")
    public void getListOrders() {
        requestListOrders();
    }
}