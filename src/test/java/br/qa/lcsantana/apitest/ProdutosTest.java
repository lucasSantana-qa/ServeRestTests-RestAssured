package br.qa.lcsantana.apitest;

import br.qa.lcsantana.apitest.core.BaseTest;
import br.qa.lcsantana.apitest.utils.Utils;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

import static br.qa.lcsantana.apitest.utils.Utils.*;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ProdutosTest extends BaseTest {

    @BeforeAll
    public static void setupTest() {
        Utils.cadastrarUsuario();
        Utils.login();
    }

    @Test
    public void testCadastrarProdutos() {
        Map<String, Object> PRODUCT = new HashMap<>();
        PRODUCT.put("nome", "Teste");
        PRODUCT.put("preco", 50000);
        PRODUCT.put("descricao", "apenas um teste");
        PRODUCT.put("quantidade", 1);

        //cadastrar produto
        String id = given()
                .contentType(ContentType.JSON)
                .body(PRODUCT)
                .when()
                .post("/produtos")
                .then()
                .statusCode(201)
                .extract().path("_id");

        //verificar se produto aparece na lista de produtos cadastrados
        given()
                .when()
                .get("/produtos")
                .then()
                .body("produtos.nome", hasItem("Teste"))
        ;
        Utils.deletarProduto(id);
    }

    @Test
    public void testListarProdutos() {
        given()
                .when()
                .get("/produtos")
                .then()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("getProductsSchema.json"))
        ;
    }

    @Test
    public void testDeletarProdutoCadastrado() {
        Map<String, Object> PRODUCT = getProduct("Teste delete",
                50000,
                "apenas um teste delete",
                1);

        String id = given()
                .contentType(ContentType.JSON)
                .body(PRODUCT)
                .when()
                .post("/produtos")
                .then()
                .extract().path("_id");

        given()
                .contentType(ContentType.JSON)
                .pathParam("_id", id)
                .when()
                .delete("/produtos/{_id}")
                .then()
                .statusCode(200)
        ;

        given()
                .when()
                .get("/produtos")
                .then()
                .body("produtos.nome", hasItem(not("Teste delete")))
        ;
    }
}
