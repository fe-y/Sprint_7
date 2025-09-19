package ru.yandex.praktikum.steps;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import ru.yandex.praktikum.models.Courier;
import ru.yandex.praktikum.models.CourierLogin;
import static io.restassured.RestAssured.given;

public class CourierSteps {

    private final String BASE_URL = "https://qa-scooter.praktikum-services.ru";

    // Создание курьера
    public Response createCourier(Courier courier) {
        return given()
                .contentType(ContentType.JSON)
                .body(courier)
                .when()
                .post(BASE_URL + "/api/v1/courier");
    }

    // Логин курьера
    public Response loginCourier(CourierLogin courierLogin) {
        return given()
                .contentType(ContentType.JSON)
                .body(courierLogin)
                .when()
                .post(BASE_URL + "/api/v1/courier/login");
    }

    // Удаление курьера по id
    public Response deleteCourier(int courierId) {
        return given()
                .when()
                .delete(BASE_URL + "/api/v1/courier/" + courierId);
    }
}