package br.qa.lcsantana.apitest;

import br.qa.lcsantana.User;
import static br.qa.lcsantana.utils.Utils.*;

import br.qa.lcsantana.utils.Utils;
import io.restassured.http.ContentType;
import org.junit.BeforeClass;
import org.junit.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UsuariosTest {

    private static User user;

    @BeforeClass
    public static void setup(){
        Utils.setupUri();
        user = createUser();
    }

    @Test
    public void testSearchUserById(){
        User responseUser = given()
                .pathParam("_id", id)
                .when()
                .get("usuarios/{_id}")
                .then()
                .extract().as(User.class);

        assertThat(user.getNome(), is(responseUser.getNome()));
        assertThat(user.getAdministrador(), is("true"));
        assertThat(user.getEmail(), is(responseUser.getEmail()));
        assertThat(user.getPassword(), is(responseUser.getPassword()));
        assertThat(user.getId(), is(responseUser.getId()));
        ;
    }

    @Test
    public void testDeleteUser(){
        given()
                .pathParam("_id", id)
                .contentType(ContentType.JSON)
                .when()
                .delete("/usuarios/{_id}")
                .then()
                .statusCode(200)
                .body("message", is("Registro excluído com sucesso"))
        ;
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
}