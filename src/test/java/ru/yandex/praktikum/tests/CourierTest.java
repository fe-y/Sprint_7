package ru.yandex.praktikum.tests;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.models.Courier;
import ru.yandex.praktikum.models.CourierLogin;
import ru.yandex.praktikum.steps.CourierSteps;
import static org.hamcrest.Matchers.containsString;

public class CourierTest {

    private int courierId;
    private Courier courier;
    private final CourierSteps courierSteps = new CourierSteps();

    @Before
    public void setUp() {
        String uniqueLogin = "testLogin" + System.currentTimeMillis();
        courier = new Courier(uniqueLogin, "testPassword", "TestName");
    }

    @After
    public void tearDown() {
        if (courierId != 0) {
            deleteCourier(courierId);
        }
    }

    @Test
    public void createCourierPositive() {
        createCourier(courier);
        Response loginResponse = loginCourier(courier);
        courierId = loginResponse.path("id");
    }

    @Test
    public void createCourierDuplicate() {
        createCourier(courier);
        createCourierExpectingError(courier, 409, "Этот логин уже используется");
    }

    @Test
    public void createCourierWithoutLogin() {
        Courier invalid = new Courier(null, "password123", "NoLogin");
        createCourierExpectingError(invalid, 400, "Недостаточно данных для создания учетной записи");
    }

    @Test
    public void createCourierWithoutPassword() {
        Courier invalid = new Courier("loginWithoutPass", null, "NoPass");
        createCourierExpectingError(invalid, 400, "Недостаточно данных для создания учетной записи");
    }

    @Step("Создать курьера {courier.login}")
    private void createCourier(Courier courier) {
        courierSteps.createCourier(courier).then().statusCode(201);
    }

    @Step("Попытка создать курьера {courier.login}, ожидаем ошибку {expectedStatus}")
    private void createCourierExpectingError(Courier courier, int expectedStatus, String expectedMessage) {
        courierSteps.createCourier(courier)
                .then()
                .statusCode(expectedStatus)
                .body("message", containsString(expectedMessage));
    }

    @Step("Войти как курьер {courier.login}")
    private Response loginCourier(Courier courier) {
        return courierSteps.loginCourier(new CourierLogin(courier.getLogin(), courier.getPassword()))
                .then()
                .statusCode(200)
                .extract()
                .response();
    }

    @Step("Удалить курьера по ID {id}")
    private void deleteCourier(int id) {
        courierSteps.deleteCourier(id).then().statusCode(200);
    }
}