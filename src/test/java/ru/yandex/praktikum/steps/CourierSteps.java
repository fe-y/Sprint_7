package ru.yandex.praktikum.steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import ru.yandex.praktikum.models.Courier;
import ru.yandex.praktikum.models.CourierLogin;

import static io.restassured.RestAssured.given;

public class CourierSteps {

    // Общая спецификация для всех запросов
    private RequestSpecification spec() {
        return given()
                .contentType(ContentType.JSON);
    }

    @Step("Создание курьера: {courier}")
    public Response createCourier(Courier courier) {
        return spec()
                .body(courier)
                .post("/courier");
    }

    @Step("Логин курьера: {courierLogin}")
    public Response loginCourier(CourierLogin courierLogin) {
        return spec()
                .body(courierLogin)
                .post("/courier/login");
    }

    @Step("Удаление курьера с id: {courierId}")
    public Response deleteCourier(int courierId) {
        return spec()
                .delete("/courier/" + courierId);
    }
}