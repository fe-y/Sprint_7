package ru.mesto.tests;

import io.qameta.allure.Step;
import org.junit.Before;
import org.junit.Test;
import ru.mesto.clients.ScooterClient;
import ru.yandex.praktikum.steps.DataGenerator;

import static org.hamcrest.CoreMatchers.equalTo;

public class CourierLoginTest {
    private ScooterClient client;
    private String login;
    private String password;

    @Before
    public void setup() {
        client = new ScooterClient();
        login = DataGenerator.getRandomLogin();
        password = DataGenerator.getRandomPassword();

        createCourier(login, password);
    }

    @Test
    public void loginCourierSuccess() {
        int id = loginCourier(login, password);
    }

    @Step("Создание курьера с логином: {login} и паролем: {password}")
    public void createCourier(String login, String password) {
        String body = "{ \"login\": \"" + login + "\", \"password\": \"" + password + "\", " +
                "\"firstName\": \"" + DataGenerator.getRandomFirstName() + "\" }";
        client.createCourier(body)
                .statusCode(201)
                .body("ok", equalTo(true));
    }

    @Step("Логин курьера с логином: {login} и паролем: {password}")
    public int loginCourier(String login, String password) {
        String body = "{ \"login\": \"" + login + "\", \"password\": \"" + password + "\" }";
        return client.loginCourier(body)
                .statusCode(200)
                .body("id", notNullValue())
                .extract().path("id");
    }
}