package ru.yandex.praktikum;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.models.Courier;
import ru.yandex.praktikum.models.CourierLogin;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class CourierLoginTest {

    private int courierId; // для хранения id созданного курьера

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/api/v1";
    }

    @After
    public void tearDown() {
        if (courierId != 0) {
            // удаляем курьера после теста
            given()
                    .when()
                    .delete("/courier/" + courierId)
                    .then()
                    .statusCode(200);
        }
    }

    @Test
    public void loginCourierPositive() {
        String uniqueLogin = "courier" + System.currentTimeMillis();
        Courier courier = new Courier(uniqueLogin, "password123", "John Smith");

        // создаём курьера
        Response createResponse = given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/courier")
                .then()
                .statusCode(201)
                .extract()
                .response();

        // логинимся
        CourierLogin login = new CourierLogin(uniqueLogin, "password123");
        Response loginResponse = given()
                .header("Content-type", "application/json")
                .body(login)
                .when()
                .post("/courier/login")
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .extract()
                .response();

        courierId = loginResponse.path("id"); // сохраняем id для удаления
    }

    @Test
    public void loginCourierWrongPassword() {
        String uniqueLogin = "courier" + System.currentTimeMillis();
        Courier courier = new Courier(uniqueLogin, "password123", "Anna Smith");

        // создаём курьера
        Response createResponse = given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/courier")
                .then()
                .statusCode(201)
                .extract()
                .response();

        CourierLogin login = new CourierLogin(uniqueLogin, "wrongPassword");

        // проверка на неправильный пароль
        given()
                .header("Content-type", "application/json")
                .body(login)
                .when()
                .post("/courier/login")
                .then()
                .statusCode(404);

        // сохраняем id для удаления
        Response loginCorrect = given()
                .header("Content-type", "application/json")
                .body(new CourierLogin(uniqueLogin, "password123"))
                .when()
                .post("/courier/login")
                .then()
                .statusCode(200)
                .extract()
                .response();
        courierId = loginCorrect.path("id");
    }
}