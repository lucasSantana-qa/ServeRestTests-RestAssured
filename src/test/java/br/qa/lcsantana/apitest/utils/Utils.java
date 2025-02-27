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

    private static User USER;
    private static String ID_RESPONSE;

    public static void cadastrarUsuario(){
        USER = new User();
        USER.setNome(faker.name().fullName());
        USER.setEmail(faker.internet().emailAddress());
        USER.setPassword(faker.internet().password());
        USER.setAdministrador("true");

        ID_RESPONSE = given()
                .contentType(ContentType.JSON)
                .body(USER)
                .when()
                .post("/usuarios")
                .then()
                .statusCode(201)
                .extract().body().path("_id");
    }

    public static void login() {
        Map<String, String> USER_LOGIN = new HashMap<>();
        USER_LOGIN.put("email", USER.getEmail());
        USER_LOGIN.put("password", USER.getPassword());

        String TOKEN = given()
                .contentType(ContentType.JSON)
                .body(USER_LOGIN)
                .when()
                .post("/login")
                .then()
                .statusCode(200)
                .extract().path("authorization");

        requestSpecification.header("Authorization", TOKEN);
    }

    public static void deletarProduto(String PRODUCT_ID) {
        given()
                .contentType(ContentType.JSON)
                .pathParam("_id", PRODUCT_ID)
                .when()
                .delete("/produtos/{_id}");
    }

    public static String getIdResponse() {
        return ID_RESPONSE;
    }

    public static User getUser() {
        return USER;
    }

    public static Map<String, Object> getProduct(String NOME, Integer PRECO, String DESC, Integer QTD) {
        Map<String, Object> product = new HashMap<>();
        product.put("nome", NOME);
        product.put("preco", PRECO);
        product.put("descricao", DESC);
        product.put("quantidade", QTD);

        return product;
    }

}
