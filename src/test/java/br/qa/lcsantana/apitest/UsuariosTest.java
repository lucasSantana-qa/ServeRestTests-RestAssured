package br.qa.lcsantana.apitest;

import br.qa.lcsantana.apitest.core.BaseTest;
import br.qa.lcsantana.apitest.utils.Utils;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.junit.jupiter.api.*;
import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class UsuariosTest extends BaseTest {

    @BeforeAll
    public static void setupTest() {
        Utils.registerUser();
        Utils.login();
    }

    @Test
    public void testSearchUserById(){
         User responseUser = given()
                .pathParam("_id", Utils.getIdResponse())
                .when()
                .get("usuarios/{_id}")
                .then()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("getUsersByIdSchema.json"))
                .body("_id", is(Utils.getIdResponse()))
                 .statusCode(200)
                .extract().as(User.class);

        assertThat(responseUser.getNome(), is(Utils.getUser().getNome()));
        assertThat(responseUser.getAdministrador(), is(Utils.getUser().getAdministrador()));
        assertThat(responseUser.getEmail(), is(Utils.getUser().getEmail()));
        assertThat(responseUser.getPassword(), is(Utils.getUser().getPassword()));
    }

    @Test
    public void testDeleteNonExistentUser(){
        given()
                .contentType(ContentType.JSON)
                .pathParam("_id", 0)
                .log().all()
                .when()
                .delete("/usuarios/{_id}")
                .then()
                .log().all()
                .statusCode(200)
                .body("message", is("Nenhum registro excluído"))
        ;
    }

    @Test
    public void testDeleteUser(){
        given()
                .pathParam("_id", Utils.getIdResponse())
                .contentType(ContentType.JSON)
                .when()
                .delete("/usuarios/{_id}")
                .then()
                .statusCode(200)
                .body("message", is("Registro excluído com sucesso"))
        ;
    }
}