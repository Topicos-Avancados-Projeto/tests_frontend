package resources.dispositivos;

import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import resources.utils.*;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TipoDeDispositivoTest {

    @BeforeAll
    static void setUp() {
        baseURI = "http://localhost:3003";
    }

    //POST

    @Test
    @Order(1)
    void deveRegistarUmNovoTipoDeDispositivoComSucesso(){

        String token = UserUtils.tokenGenerator();
        String tipoDeDispositivo = TipoDeDispositivoUtils.criarPostTipoDeDispositivoComSucesso();

        given()
            .header("Authorization", token)
            .contentType(ContentType.JSON)
            .body(tipoDeDispositivo)
        .when()
            .post("/type")
                .then()
                .statusCode(201)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/tipoDeDispositivo/tipoDeDispositivoJsonSchema.json"));
    }

    @Test
    @Order(2)
    void deveRegistarUmNovoTipoDeDispositivoSemSucessoComProblemaDeValidacao(){

        String token = UserUtils.tokenGenerator();
        String tipoDeDispositivo = TipoDeDispositivoUtils.criarPostTipoDeDispositivoVazio();

        given()
            .header("Authorization", token)
            .contentType(ContentType.JSON)
            .body(tipoDeDispositivo)
        .when()
            .post("/type")
        .then()
            .statusCode(400)
                .body("msg",is("name should not be empty"));
    }

    @Test
    @Order(3)
    void deveRegistarUmNovoTipoDeDispositivoSemSucessoComProblemaDeAutorizacao(){

        String tipoDeDispositivo = TipoDeDispositivoUtils.criarPostTipoDeDispositivoComSucesso();

        given()
            .contentType(ContentType.JSON)
            .body(tipoDeDispositivo)
        .when()
            .post("/type")
        .then()
            .statusCode(401)
            .body("msg",is("User not logged in!"));
    }

    @Test
    @Order(4)
    void deveRegistarUmNovoTipoDeDispositivoSemSucessoComProblemaDePermissao(){
        String token = JWTGenerator.tokenGenerator("NoRole","123.456.789-00");

        String tipoDeDispositivo = TipoDeDispositivoUtils.criarPostTipoDeDispositivoComSucesso();

        given()
            .header("Authorization",token)
            .contentType(ContentType.JSON)
            .body(tipoDeDispositivo)
        .when()
            .post("/type")
        .then()
            .statusCode(403)
            .body("msg",is("Access forbidden for this device."));
    }


    // GET

    @Test
    @Order(5)
    void deveObterUmaPaginacaoComSucesso() {

        String token = UserUtils.tokenGenerator();

                 given()
                        .header("Authorization", token)
                        .queryParam("limit", 10)
                        .queryParam("sortby", "nome")
                        .queryParam("order", "DESC")
                .when()
                        .get("/type")
                .then()
                        .statusCode(is(200))
                        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/tipoDeDispositivo/tiposDeDispositivoJsonSchema.json"));
    }

    @Test
    @Order(6)
    void deveObterUmaPaginacaoComSucessoSemAutorizacao() {

        given()
                .queryParam("limit", 10)
                .queryParam("sortby", "nome")
                .queryParam("order", "DESC")
        .when()
                .get("/type")
        .then()
                .statusCode(is(401));
    }

    @Test
    @Order(7)
    void deveObterUmTipoDeDispositivoComSucesso() {

        String token = UserUtils.tokenGenerator();

                given()
                        .header("Authorization", token)
                .when()
                        .get("/type/1")
                .then()
                        .statusCode(is(200))
                        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/tipoDeDispositivo/tipoDeDispositivoJsonSchema.json"));
    }


    @Test
    @Order(8)
    void deveObterUmTipoDeDispositivoSemSucessoNotFound() {

        String token = UserUtils.tokenGenerator();

        given()
                .header("Authorization", token)
        .when()
                .get("/type/100000000")
        .then()
                .statusCode(is(404))
                .body("msg",is("Type does not exist."));


    }

    @Test
    @Order(9)

    void deveAtualizarUmTipoDeDispositivoComSucesso(){

        String token = UserUtils.tokenGenerator();
        String tipoDeDispositivo = TipoDeDispositivoUtils.criarPostTipoDeDispositivo2();

        given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .body(tipoDeDispositivo)
        .when()
                .patch("/type/1")
        .then()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/tipoDeDispositivo/tipoDeDispositivoJsonSchema.json"));
    }

    @Test
    @Order(10)
    void deveAtualizarUmTipoDeDispositivoSemSucessoNotFound(){

        String token = UserUtils.tokenGenerator();
        String tipoDeDispositivo = TipoDeDispositivoUtils.criarPostTipoDeDispositivo2().toString();

        given()
            .header("Authorization", token)
            .contentType(ContentType.JSON)
            .body(tipoDeDispositivo)
        .when()
            .patch("/type/400000000")
        .then()
            .statusCode(404)
            .body("msg",is("Type does not exist."));
    }


    @Test
    @Order(11)

    void deveDeletarUmDispositivoComSucesso(){

        String token = UserUtils.tokenGenerator();

            given()
                .header("Authorization", token)
            .when()
                .delete("/type/1")
            .then()
                .statusCode(204);
    }

    @Test
    @Order(12)
    void deveDeletarUmDispositivoSemSucessoNotFound(){

        String token = UserUtils.tokenGenerator();

        given()
                .header("Authorization", token)
            .when()
                .delete("/type/1000000000")
            .then()
                .statusCode(404)
                .body("msg",is("Type does not exist."));
    }
}