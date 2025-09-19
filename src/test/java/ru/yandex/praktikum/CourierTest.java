package ru.yandex.praktikum;

import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.models.Courier;
import ru.yandex.praktikum.models.CourierLogin;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class CourierTest {

    private int courierId;
    private Courier courier;

    @Before
    public void setUp() {
        String uniqueLogin = "testLogin" + System.currentTimeMillis();
        courier = new Courier(uniqueLogin, "testPassword", "TestName");

        // Создание курьера
        Response createResponse = given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/courier")
                .then()
                .statusCode(201)
                .extract()
                .response();

        // Логин курьера
        Response loginResponse = given()
                .header("Content-type", "application/json")
                .body(new CourierLogin(courier.getLogin(), courier.getPassword()))
                .when()
                .post("/courier/login")
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .extract()
                .response();

        courierId = loginResponse.path("id");
    }

    @Test
    public void testLoginCourier() {
        // Проверяем повторный логин
        given()
                .header("Content-type", "application/json")
                .body(new CourierLogin(courier.getLogin(), courier.getPassword()))
                .when()
                .post("/courier/login")
                .then()
                .statusCode(200)
                .body("id", notNullValue());
    }

    @After
    public void tearDown() {
        if (courierId != 0) {
            given()
                    .when()
                    .delete("/courier/" + courierId)
                    .then()
                    .statusCode(200);
        }
    }
}
