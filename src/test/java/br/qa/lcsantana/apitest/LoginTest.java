package br.qa.lcsantana.apitest;

import br.qa.lcsantana.apitest.core.BaseTest;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class LoginTest extends BaseTest {

    private static Map<String, String> INVALID_USER;

    @Test
    public void testNaoDeveFazerLoginComUsuarioInvalido() {
        INVALID_USER = getInvalidUser("@testeLogininvalido@.com", "");

        given()
                .body(INVALID_USER)
                .when()
                .post("/login")
                .then()
                .statusCode(400)
                .body("email", is("email deve ser um email válido"))
                .body("password", is("password não pode ficar em branco"))
        ;
    }

    @Test
    public void testNaoDeveFazerLoginComUsuarioNaoCadastrado(){
        INVALID_USER = getInvalidUser("testeNaoCadastrado@email.com", "1234");

        given()
                .body(INVALID_USER)
                .when()
                .post("/login")
                .then()
                .statusCode(401)
                .body("message", is("Email e/ou senha inválidos"))
        ;
    }

    public Map<String, String> getInvalidUser(String EMAIL, String PASSWORD) {
        Map<String, String> INVALID_USER = new HashMap<>();
        INVALID_USER.put("email", EMAIL);
        INVALID_USER.put("password", PASSWORD);

        return INVALID_USER;
    }

}
