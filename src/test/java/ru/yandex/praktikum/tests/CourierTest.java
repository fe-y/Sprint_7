package ru.yandex.praktikum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.models.Courier;
import ru.yandex.praktikum.steps.CourierSteps;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class CourierTest extends BaseTest {

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
    @Description("Проверка успешного создания курьера и ответа API")
    public void createCourierPositive() {
        Response response = createCourierStep(courier);

        response.then()
                .statusCode(SC_CREATED)
                .body("ok", equalTo(true));

        // Берём id безопасно
        if (response.path("id") != null) {
            courierId = response.path("id");
        }
    }

    @Test
    @Description("Проверка ошибки при создании курьера с уже существующим логином")
    public void createCourierDuplicate() {
        // Создаём первого курьера
        Response firstResponse = createCourierStep(courier);
        if (firstResponse.path("id") != null) {
            courierId = firstResponse.path("id");
        }

        // Пробуем создать дубликат и проверяем ответ
        Response duplicateResponse = courierSteps.createCourier(courier);
        duplicateResponse.then()
                .statusCode(SC_CONFLICT)
                .body("message", containsString("Этот логин уже используется"));
    }

    @Test
    @Description("Проверка ошибки при попытке создать курьера без логина")
    public void createCourierWithoutLogin() {
        Courier invalid = new Courier(null, "password123", "NoLogin");
        createCourierExpectingError(invalid, SC_BAD_REQUEST,
                "Недостаточно данных для создания учетной записи");
    }

    @Test
    @Description("Проверка ошибки при попытке создать курьера без пароля")
    public void createCourierWithoutPassword() {
        Courier invalid = new Courier("loginWithoutPass", null, "NoPass");
        createCourierExpectingError(invalid, SC_BAD_REQUEST,
                "Недостаточно данных для создания учетной записи");
    }

    @Step("Создание курьера: {courier.login}")
    private Response createCourierStep(Courier courier) {
        Response response = courierSteps.createCourier(courier);
        if (response == null) {
            throw new RuntimeException("API вернул null при создании курьера!");
        }
        return response;
    }

    @Step("Попытка создать курьера {courier.login}, ожидаем ошибку {expectedStatus}")
    private void createCourierExpectingError(Courier courier, int expectedStatus, String expectedMessage) {
        courierSteps.createCourier(courier)
                .then()
                .statusCode(expectedStatus)
                .body("message", containsString(expectedMessage));
    }

    @Step("Удалить курьера по ID {id}")
    private void deleteCourier(int id) {
        courierSteps.deleteCourier(id)
                .then()
                .statusCode(SC_OK);
    }
}