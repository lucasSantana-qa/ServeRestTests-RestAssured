package br.qa.lcsantana.apitest.utils;

import br.qa.lcsantana.apitest.User;
import br.qa.lcsantana.apitest.core.BaseTest;
import com.github.javafaker.Faker;
import io.restassured.http.ContentType;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.requestSpecification;

public class Utils extends BaseTest {

    static Faker faker = new Faker();

    private static User user;
    private static String idResponse;

    public static void registerUser(){
         user = new User();
         user.setNome(faker.name().fullName());
         user.setEmail(faker.internet().emailAddress());
         user.setPassword(faker.internet().password());
         user.setAdministrador("true");

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
    }

    public static void login() {
        Map<String, String> userLogin = new HashMap<>();
        userLogin.put("email", user.getEmail());
        userLogin.put("password", user.getPassword());

        String token = given()
                .contentType(ContentType.JSON)
                .body(userLogin)
                .when()
                .post("/login")
                .then()
                .statusCode(200)
                .extract().path("authorization");

        requestSpecification.header("Authorization", token);
    }

    public static void deleteProduct(String productId) {
        given()
                .contentType(ContentType.JSON)
                .pathParam("_id", productId)
                .when()
                .delete("/produtos/{_id}");
    }

    public static String getIdResponse() {
        return idResponse;
    }

    public static User getUser() {
        return user;
    }

}
