package br.qa.lcsantana.apitest.core;

import br.qa.lcsantana.apitest.utils.Utils;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import org.junit.jupiter.api.BeforeAll;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

public class BaseTest implements Constraints {

    @BeforeAll
    public static void setupTest() {
        Utils.cadastrarUsuario();
        String Token = Utils.login();

        requestSpecification.header("Authorization", Token);
    }

    @BeforeAll
    public static void setupRequest() {
        baseURI = APP_BASE_URL;

        RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
        reqBuilder.setContentType(CONTENT_TYPE);
        requestSpecification = reqBuilder.build();

        ResponseSpecBuilder resBuilder = new ResponseSpecBuilder();
        resBuilder.expectResponseTime(lessThanOrEqualTo(MAX_TIMEOUT));
        responseSpecification = resBuilder.build();

        enableLoggingOfRequestAndResponseIfValidationFails();
    }
}
