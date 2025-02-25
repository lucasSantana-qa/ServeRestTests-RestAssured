package br.qa.lcsantana.utils;

import br.qa.lcsantana.User;
import com.github.javafaker.Faker;
import io.restassured.http.ContentType;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static io.restassured.RestAssured.given;

public class Utils {

    static Faker faker = new Faker();

    private static User user;
    private static String idResponse;

    private static String token;

    public static void setupUri() {
        baseURI = "https://serverest.dev";
    }

    public static User registerUser(){
        setupUri();

         user = new User(faker.name().fullName(),
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

    public static void login() {
        Map<String, String> userLogin = new HashMap<>();
        userLogin.put("email", user.getEmail());
        userLogin.put("password", user.getPassword());

        token = given()
                .contentType(ContentType.JSON)
                .body(userLogin)
                .when()
                .post("/login")
                .then()
                .extract().path("authorization");
    }

    public static void deleteProduct(String productId) {
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", Utils.getToken())
                .pathParam("_id", productId)
                .when()
                .delete("https://serverest.dev/produtos/{_id}");
    }

    public static String getIdResponse() {
        return idResponse;
    }

    public static String getToken() {
        return token;
    }

}
