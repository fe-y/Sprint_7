package ru.yandex.praktikum.models;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

public class Order {

    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phone;
    private String[] color;

    public Order() {
        this.firstName = randomAlphabetic(6);
        this.lastName = randomAlphabetic(6);
        this.address = randomAlphabetic(8) + " str.";
        this.metroStation = randomNumeric(2);
        this.phone = randomNumeric(10);
        this.color = new String[]{}; // по умолчанию пустой массив
    }


    public void setColor(String[] color) {
        this.color = color;
    }


    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getAddress() { return address; }
    public String getMetroStation() { return metroStation; }
    public String getPhone() { return phone; }
    public String[] getColor() { return color; }
}