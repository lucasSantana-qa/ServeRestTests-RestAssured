package br.qa.lcsantana.apitest;

import br.qa.lcsantana.core.BaseTest;
import br.qa.lcsantana.utils.Utils;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ProdutosTest extends BaseTest {

    @BeforeAll
    public static void setup() {
        Utils.registerUser();
        Utils.login();
    }

    @Test
    public void testRegisterProduct() {
        Map<String, Object> product = new HashMap<>();
        product.put("nome", "Teste");
        product.put("preco", 50000);
        product.put("descricao", "apenas um teste");
        product.put("quantidade", 1);

         //cadastrar produto
        String id = given()
                .contentType(ContentType.JSON)
                .body(product)
                .when()
                .post("/produtos")
                .then()
                .log().all()
                .statusCode(201)
                .extract().path("_id");

        //verificar se produto aparece na lista de produtos cadastrados
        given()
                .when()
                .get("/produtos")
                .then()
                .body("produtos.nome", hasItem("Teste"))
        ;
        Utils.deleteProduct(id);
    }

    @Test
    public void testGetProducts() {
        given()
                .when()
                .get("/produtos")
                .then()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("getProductsSchema.json"))
        ;
    }

    @Test
    public void testDeleteProduct() {
        Map<String, Object> product = getProduct("Teste delete",
                50000,
                "apenas um teste delete",
                1);

        String id = given()
                .contentType(ContentType.JSON)
                .body(product)
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

    public Map<String, Object> getProduct(String nome, Integer preco, String descricao, Integer qtd) {
        Map<String, Object> product = new HashMap<>();
        product.put("nome", nome);
        product.put("preco", preco);
        product.put("descricao", descricao);
        product.put("quantidade", qtd);

        return product;
    }
}
