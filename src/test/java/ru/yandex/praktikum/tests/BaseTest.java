package ru.yandex.praktikum.tests;

import io.restassured.RestAssured;
import org.junit.BeforeClass;
import io.qameta.allure.Step;

public class BaseTest {

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/api/v1";
    }
}
