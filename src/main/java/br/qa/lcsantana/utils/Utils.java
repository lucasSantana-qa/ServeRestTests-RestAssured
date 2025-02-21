package br.qa.lcsantana.utils;

import br.qa.lcsantana.User;
import com.github.javafaker.Faker;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.*;
import static io.restassured.RestAssured.given;

public class Utils {

    static Faker faker = new Faker();

    public static String id;

    public static void setupUri() {
        baseURI = "https://serverest.dev";

    }

    public static User createUser(){
        User user = new User(faker.name().fullName(),
                faker.internet().emailAddress(),
                faker.internet().password(),
                "true");

        id = given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("/usuarios")
                .then()
                .statusCode(201)
                .extract().path("_id");
        return user;
    }
}
