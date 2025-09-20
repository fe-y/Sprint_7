package ru.yandex.praktikum.steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import ru.yandex.praktikum.models.Courier;
import ru.yandex.praktikum.models.CourierLogin;

import static io.restassured.RestAssured.given;

public class CourierSteps {

    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru/api/v1";

    @Step("Создание курьера: {courier}")
    public Response createCourier(Courier courier) {
        return given()
                .contentType(ContentType.JSON)
                .body(courier)
                .when()
                .post(BASE_URL + "/courier");
    }

    @Step("Логин курьера: {courierLogin}")
    public Response loginCourier(CourierLogin courierLogin) {
        return given()
                .contentType(ContentType.JSON)
                .body(courierLogin)
                .when()
                .post(BASE_URL + "/courier/login");
    }

    @Step("Удаление курьера с id: {courierId}")
    public Response deleteCourier(int courierId) {
        return given()
                .when()
                .delete(BASE_URL + "/courier/" + courierId);
    }
}