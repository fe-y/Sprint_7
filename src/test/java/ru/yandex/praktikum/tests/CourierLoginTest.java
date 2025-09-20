package ru.yandex.praktikum.tests;

import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.models.Courier;
import ru.yandex.praktikum.models.CourierLogin;
import ru.yandex.praktikum.steps.CourierSteps;
import io.qameta.allure.Step;
import io.qameta.allure.Description;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CourierLoginTest extends BaseTest {

    private int courierId;
    private String uniqueLogin;
    private final CourierSteps courierSteps = new CourierSteps();

    @Before
    public void setUp() {
        uniqueLogin = "courier" + System.currentTimeMillis();
        Courier courier = new Courier(uniqueLogin, "password123", "Test User");
        createCourierStep(courier);
    }

    @After
    public void tearDown() {
        if (uniqueLogin != null) {
            if (courierId == 0) {
                Response loginCorrect = loginCourierStep(new CourierLogin(uniqueLogin, "password123"));
                courierId = loginCorrect.path("id");
            }
            deleteCourierStep(courierId);
        }
    }

    @Test
    @Description("Проверка успешного логина курьера с правильными данными")
    public void loginCourierPositive() {
        Response loginResponse = loginCourierStep(new CourierLogin(uniqueLogin, "password123"))
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .extract()
                .response();
        courierId = loginResponse.path("id");
    }

    @Test
    @Description("Логин курьера с неправильным паролем")
    public void loginCourierWrongPassword() {
        loginCourierStep(new CourierLogin(uniqueLogin, "wrongPassword"))
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @Description("Логин курьера с неправильным логином")
    public void loginCourierWrongLogin() {
        loginCourierStep(new CourierLogin("wrongLogin", "password123"))
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @Description("Логин курьера с пустым логином")
    public void loginCourierEmptyLogin() {
        loginCourierStep(new CourierLogin("", "password123"))
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @Description("Логин курьера с пустым паролем")
    public void loginCourierEmptyPassword() {
        loginCourierStep(new CourierLogin(uniqueLogin, ""))
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Step("Создаём курьера: {courier.login}")
    private void createCourierStep(Courier courier) {
        courierSteps.createCourier(courier).then().statusCode(201);
    }

    @Step("Логинимся курьером: {login.login}")
    private Response loginCourierStep(CourierLogin login) {
        return courierSteps.loginCourier(login);
    }

    @Step("Удаляем курьера с ID: {id}")
    private void deleteCourierStep(int id) {
        courierSteps.deleteCourier(id).then().statusCode(200);
    }
}