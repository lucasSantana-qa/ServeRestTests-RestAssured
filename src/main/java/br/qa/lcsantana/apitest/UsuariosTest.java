package br.qa.lcsantana.apitest;

import br.qa.lcsantana.User;
import static br.qa.lcsantana.utils.Utils.*;

import br.qa.lcsantana.utils.Utils;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.junit.jupiter.api.*;
import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsuariosTest {

    private static User user;

    @BeforeAll
    public static void setup(){
        Utils.setupUri();
        user = registerUser();
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

        assertThat(user.getNome(), is(user.getNome()));
        assertThat(user.getAdministrador(), is("true"));
        assertThat(user.getEmail(), is(responseUser.getEmail()));
        assertThat(user.getPassword(), is(responseUser.getPassword()));
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