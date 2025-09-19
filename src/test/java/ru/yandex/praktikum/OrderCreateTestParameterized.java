package ru.yandex.praktikum;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.praktikum.steps.OrderSteps;

import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderCreateTestParameterized {

    private final String[] color;

    private final OrderSteps orderSteps = new OrderSteps();

    // Параметры: массивы цветов
    @Parameterized.Parameters
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
        orderSteps.createOrder(color)
                .then()
                .statusCode(201)
                .body("track", notNullValue());
    }
}