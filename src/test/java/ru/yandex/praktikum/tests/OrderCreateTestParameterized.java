package ru.yandex.praktikum.tests;

import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.praktikum.steps.OrderSteps;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderCreateTestParameterized {

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
    @DisplayName("Создание заказа с различными цветами")
    public void createOrderTest() {
        createOrderWithColors(color);
    }

    @Step("Создание заказа с цветами: {colors}")
    private void createOrderWithColors(String[] colors) {
        Response response = orderSteps.createOrder(colors)
                .then()
                .statusCode(201)
                .body("track", notNullValue())
                .extract()
                .response();

        track = response.path("track");
    }

    @After
    @Step("Отмена заказа с треком: {track}")
    public void cancelOrder() {
        if (track != 0) {
            orderSteps.cancelOrder(track).then().statusCode(200);
        }
    }
}