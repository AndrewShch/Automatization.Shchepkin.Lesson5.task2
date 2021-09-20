package ru.netology.data;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class DataGenerator {

    private static final Faker faker = new Faker(new Locale("en"));

    private DataGenerator() {
    }


    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    @BeforeAll
    static void makeRequest(RegistrationInfo registrationInfo) {
        // сам запрос
        given() // "дано"
                .spec(requestSpec) // указываем, какую спецификацию используем
                .body(registrationInfo) // передаём в теле объект, который будет преобразован в JSON
                .when() // "когда"
                .post("/api/system/users") // на какой путь, относительно BaseUri отправляем запрос
                .then() // "тогда ожидаем"
                .statusCode(200); // код 200 OK
    }

    public static class Registration {
        private Registration() {
        }

        public static String randomLogin() {
            return faker.name().username();
        }

        public static String randomPassword() {
            return faker.internet().password();
        }


        public static RegistrationInfo shouldValidLogin() {
            RegistrationInfo registrationInfo = new RegistrationInfo(
                    randomLogin(),
                    randomPassword(),
                    "active");
            makeRequest(registrationInfo);
            return registrationInfo;
        }

        public static RegistrationInfo shouldNoValidLogin() {
            RegistrationInfo registrationInfo = new RegistrationInfo(
                    randomLogin(),
                    randomPassword(),
                    "blocked");
            makeRequest(registrationInfo);
            return registrationInfo;
        }


        public static RegistrationInfo shouldGetInvalidLogin() {
            String password = randomPassword();
            RegistrationInfo registrationInfo = new RegistrationInfo(
                    randomLogin(),
                    password,
                    "active");
            makeRequest(registrationInfo);
            return new RegistrationInfo(
                    randomLogin(),
                    password,
                    "active");
        }
        public static RegistrationInfo shouldGetInvalidPassword() {
            String login = randomLogin();
            RegistrationInfo registrationInfo = new RegistrationInfo(
                    login,
                    randomPassword(),
                    "active");
            makeRequest(registrationInfo);
            return new RegistrationInfo(
                    login,
                    randomPassword(),
                    "active");
        }

    }

}


