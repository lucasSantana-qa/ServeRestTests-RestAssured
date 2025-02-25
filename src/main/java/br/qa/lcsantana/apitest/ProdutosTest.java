package br.qa.lcsantana.apitest;

import br.qa.lcsantana.utils.Utils;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ProdutosTest {

    @BeforeAll
    public static void setup() {
        Utils.registerUser();
        Utils.login();
    }

    @Test
    public void testRegisterProduct() {
        Map<String, Object> product = new HashMap<>();
        product.put("nome", "Monitor para teste");
        product.put("preco", 50000);
        product.put("descricao", "apenas um teste");
        product.put("quantidade", 1);

         //cadastrar produto
        String id = given()
                .header("Authorization", Utils.getToken())
                .contentType(ContentType.JSON)
                .body(product)
                .when()
                .post("https://serverest.dev/produtos")
                .then()
                .statusCode(201)
                .extract().path("_id");

        //verificar se produto aparece na lista de produtos cadastrados
        given()
                .when()
                .get("https://serverest.dev/produtos")
                .then()
                .body("produtos.nome", hasItem("Monitor para teste"))
        ;
        Utils.deleteProduct(id);
    }

    @Test
    public void testGetProducts() {
        given()
                .when()
                .get("https://serverest.dev/produtos")
                .then()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("getProductsSchema.json"))
        ;
    }

    @Test
    public void testDeleteProduct() {
        Map<String, Object> product = new HashMap<>();
        product.put("nome", "Monitor para teste deletar");
        product.put("preco", 50000);
        product.put("descricao", "apenas um teste delete");
        product.put("quantidade", 1);

        String id = given()
                .header("Authorization", Utils.getToken())
                .contentType(ContentType.JSON)
                .body(product)
                .when()
                .post("https://serverest.dev/produtos")
                .then()
                .extract().path("_id");

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", Utils.getToken())
                .pathParam("_id", id)
                .when()
                .delete("https://serverest.dev/produtos/{_id}")
                .then()
                .statusCode(200)
        ;

        given()
                .when()
                .get("https://serverest.dev/produtos")
                .then()
                .body("produtos.nome", hasItem(not("Monitor para teste deletar")))
        ;
    }
}
