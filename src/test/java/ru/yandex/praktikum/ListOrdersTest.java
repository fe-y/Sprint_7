package ru.yandex.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.CoreMatchers.notNullValue;

public class ListOrdersTest {

    private static final String GET_ORDERS = "/api/v1/orders";

    @Before
    public void setUp() {
        baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Step("Отправка запроса на получение списка заказов")
    private void requestListOrders() {
        given()
                .contentType(JSON)
                .get(GET_ORDERS)
                .then()
                .statusCode(200)
                .body("orders", notNullValue());
    }

    @Test
    @Description("Запрос на получение списка заказов")
    public void getListOrders() {
        requestListOrders();
    }
}