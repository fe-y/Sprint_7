package ru.yandex.praktikum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.models.Courier;
import ru.yandex.praktikum.models.CourierLogin;
import ru.yandex.praktikum.steps.CourierSteps;

import static org.apache.http.HttpStatus.*;
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
            try {
                // Если id ещё не получен, пробуем залогиниться и получить его
                if (courierId == 0) {
                    Response loginResponse = loginCourierStep(new CourierLogin(uniqueLogin, "password123"));
                    if (loginResponse.statusCode() == SC_OK) {
                        courierId = loginResponse.path("id");
                    }
                }
            } catch (Exception e) {
                System.out.println("Не удалось получить id курьера для удаления: " + e.getMessage());
            }

            // Удаляем курьера, если id получен
            if (courierId != 0) {
                deleteCourierStep(courierId);
            }
        }
    }

    @Test
    @Description("Проверка успешного логина курьера с правильными данными")
    public void loginCourierPositive() {
        Response loginResponse = loginCourierStep(new CourierLogin(uniqueLogin, "password123"))
                .then()
                .statusCode(SC_OK)
                .body("id", notNullValue())
                .extract()
                .response();
        courierId = loginResponse.path("id");
    }

    @Test
    @Description("Проверка ошибки при попытке логина курьера с неверным паролем")
    public void loginCourierWrongPassword() {
        loginCourierStep(new CourierLogin(uniqueLogin, "wrongPassword"))
                .then()
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @Description("Проверка ошибки при попытке логина курьера с неверным логином")
    public void loginCourierWrongLogin() {
        loginCourierStep(new CourierLogin("wrongLogin", "password123"))
                .then()
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @Description("Проверка ошибки при попытке логина курьера без логина")
    public void loginCourierEmptyLogin() {
        loginCourierStep(new CourierLogin("", "password123"))
                .then()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @Description("Проверка ошибки при попытке логина курьера без пароля")
    public void loginCourierEmptyPassword() {
        loginCourierStep(new CourierLogin(uniqueLogin, ""))
                .then()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Step("Создаём курьера: {courier.login}")
    private void createCourierStep(Courier courier) {
        courierSteps.createCourier(courier)
                .then()
                .statusCode(SC_CREATED);
    }

    @Step("Логинимся курьером: {login.login}")
    private Response loginCourierStep(CourierLogin login) {
        return courierSteps.loginCourier(login);
    }

    @Step("Удаляем курьера с ID: {id}")
    private void deleteCourierStep(int id) {
        courierSteps.deleteCourier(id)
                .then()
                .statusCode(SC_OK);
    }
}