package br.qa.lcsantana.utils;

import br.qa.lcsantana.User;
import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import static io.restassured.RestAssured.*;
import static io.restassured.RestAssured.given;

public class Utils {

    static Faker faker = new Faker();

    private static String idResponse;

    public static void setupUri() {
        baseURI = "https://serverest.dev";
    }

    public static User createUser(){

        User user = new User(faker.name().fullName(),
                faker.internet().emailAddress(),
                faker.internet().password(),
                "true");


        idResponse = given()
                .contentType(ContentType.JSON)
                .body(user)
                .log().all()
                .when()
                .post("/usuarios")
                .then()
                .log().all()
                .statusCode(201)
                .extract().body().path("_id");
        return user;
    }

    public static String getIdResponse() {
        return idResponse;
    }

}
