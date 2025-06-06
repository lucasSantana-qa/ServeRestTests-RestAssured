package br.qa.lcsantana.apitest;

import br.qa.lcsantana.apitest.core.BaseTest;
import br.qa.lcsantana.apitest.utils.Produto;
import br.qa.lcsantana.apitest.utils.Utils;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ProdutosTest extends BaseTest{

    @Test
    public void testCadastrarProduto() {

        //cadastrar produto
        String id = given()
                .contentType(ContentType.JSON)
                .body(getProduct())
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

//    @Test
//    public void testDeletarProdutoCadastrado() {
//        Produto PRODUCT = getProduct();
//
//        String id = given()
//                .contentType(ContentType.JSON)
//                .body(PRODUCT)
//                .when()
//                .post("/produtos")
//                .then()
//                .extract().path("_id");
//
//        given()
//                .contentType(ContentType.JSON)
//                .pathParam("_id", id)
//                .when()
//                .delete("/produtos/{_id}")
//                .then()
//                .statusCode(200)
//        ;
//
//        given()
//                .when()
//                .get("/produtos")
//                .then()
//                .body("produtos.nome", hasItem(not("Teste delete")))
//        ;
//    }

    public Produto getProduct() {
        Produto PRODUCT = new Produto();
        PRODUCT.setNome("Teste");
        PRODUCT.setPreco(3200);
        PRODUCT.setDescricao("apenas um teste");
        PRODUCT.setQuantidade(1);
        return PRODUCT;
    }
}
