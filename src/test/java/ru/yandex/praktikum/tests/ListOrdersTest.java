package ru.yandex.praktikum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.apache.http.HttpStatus.SC_OK;

public class ListOrdersTest extends BaseTest {

    private static final String GET_ORDERS = "/orders";

    @Step("Отправка запроса на получение списка заказов")
    public Response requestListOrders() {
        // только отправка запроса и возврат Response
        return given()
                .contentType(JSON)
                .get(GET_ORDERS)
                .andReturn();
    }

    @Test
    @Description("Проверка, что запрос на получение списка заказов возвращает успешный ответ и не пустой массив orders")
    public void getListOrders() {
        Response response = requestListOrders();

        // проверки делаем здесь в тесте
        response.then()
                .statusCode(SC_OK)
                .body("orders", notNullValue());
    }
}