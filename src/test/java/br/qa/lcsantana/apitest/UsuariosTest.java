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

    @Test
    public void testBuscarUsuarioPorId(){
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
    public void testDeletarUsuarioNaoExistente(){
        given()
                .contentType(ContentType.JSON)
                .pathParam("_id", 0)
                .when()
                .delete("/usuarios/{_id}")
                .then()
                .statusCode(200)
                .body("message", is("Nenhum registro excluído"))
        ;
    }

    @Test
    public void testDeletarUsuarioCadastrado(){
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