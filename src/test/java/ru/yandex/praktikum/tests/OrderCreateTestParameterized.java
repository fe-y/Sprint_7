package ru.yandex.praktikum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.praktikum.steps.OrderSteps;

import static org.hamcrest.Matchers.notNullValue;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;

@RunWith(Parameterized.class)
public class OrderCreateTestParameterized extends BaseTest {

    private final String[] color;
    private final OrderSteps orderSteps = new OrderSteps();
    private int track;

    @Parameterized.Parameters(name = "Цвета заказа: {0}")
    public static Object[][] color() {
        return new Object[][]{
                {new String[]{"BLACK"}},
                {new String[]{"GREY"}},
                {new String[]{"BLACK", "GREY"}},
                {new String[]{}}
        };
    }

    public OrderCreateTestParameterized(String[] color) {
        this.color = color;
    }

    @Test
    @Description("Проверка создания заказа с разными вариантами цвета. Убедиться, что возвращается track заказа.")
    public void createOrderTest() {
        createOrderWithColors(color);
    }

    @Step("Создание заказа с цветами: {colors}")
    private void createOrderWithColors(String[] colors) {
        // Сначала делаем запрос и получаем track
        Response response = orderSteps.createOrder(colors).andReturn();
        track = response.path("track"); // track присвоен до проверок

        // Проверки после присвоения track
        response.then()
                .statusCode(SC_CREATED)
                .body("track", notNullValue());
    }

    @After
    @Step("Отмена заказа с треком: {track}")
    public void cancelOrder() {
        if (track != 0) {
            orderSteps.cancelOrder(track)
                    .then()
                    .statusCode(SC_OK);
        }
    }
}