package resources;

import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import resources.utils.DispositivoUtils;
import resources.utils.JWTGenerator;
import resources.utils.UserUtils;

import java.util.List;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DispositivoTest {


    @BeforeAll
    static void setUp() {
        baseURI = "http://localhost:3003";
    }



        @Test
        @Order(1)
        void deveCriarUmDispositivoComSucesso(){

            String token = UserUtils.tokenGenerator();
            String dispositivo = DispositivoUtils.criarPostDispositivo().toString();

            given()
                    .header("Authorization", token)
                    .contentType(ContentType.JSON)
                    .body(dispositivo)
            .when()
                    .post("/device")
            .then()
                    .statusCode(201)
                    .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("dispositivoJsonSchema.json"));
        }

    @Test
    @Order(2)
    void deveCriarUmDispositivoSemSucessoConflitoDeDados(){

        String token = UserUtils.tokenGenerator();

        String dispositivo = DispositivoUtils.criarPostDispositivoComConflito().toString();

        given()
            .header("Authorization", token)
                .contentType(ContentType.JSON)
                .body(dispositivo)
            .when()
                .post("/device")
            .then()
                .statusCode(409)
                .body("msg",is("ID  already exists!"));
    }

    @Test
    @Order(3)
    void deveCriarUmDispositivoSemSucessoComProblemaDeValidacao(){

        String token = UserUtils.tokenGenerator();

        String dispositivo = DispositivoUtils.criarPostDispositivoComProblemaDeValidacao().toString();

        given()
            .header("Authorization", token)
                .contentType(ContentType.JSON)
                .body(dispositivo)
            .when()
                .post("/device")
            .then()
                .statusCode(422)
                .body("msg",is("Validation problem"));
    }

    @Test
    @Order(3)
    void deveCriarUmDispositivoSemSucessoSemAutorizacao(){


        String dispositivo = DispositivoUtils.criarPostDispositivoComProblemaDeValidacao().toString();

        given()
            .contentType(ContentType.JSON)
            .body(dispositivo)
        .when()
            .post("/device")
        .then()
            .statusCode(401)
            .body("msg",is("Device not logged in!"));
    }


    @Test
        @Order(5)
        void deveObterUmaPaginacaoComSucesso() {

            String token = UserUtils.tokenGenerator();

           Response response = given()
                .header("Authorization", token)
                .queryParam("limit", 10)
                .queryParam("sortby", "nome")
                .queryParam("order", "DESC")
            .when()
                .get("/device")
            .then()
                .statusCode(is(200))
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("dispositivosJsonSchema.json"))
                   .extract().response();

            List<Object> devices = response.getBody().jsonPath().getList("devices");

           assertTrue(devices.size()<=10);
        }


    @Test
    @Order(6)
    void deveObterUmaPaginacaoSemSucessoNaoAutorizado() {

        given()
            .queryParam("limit", 10)
            .queryParam("sortby", "nome")
            .queryParam("order", "DESC")
        .when()
            .get("/device")
        .then()
            .statusCode(is(401))
            .body("msg", is("Device not logged in"));

    }
    @Test
    @Order(6)
    void deveObterUmaPaginacaoSemSucessoComErroDePermissao() {

        String token = JWTGenerator.tokenGenerator("NoRole","123.456.789-00");

        given()
            .header("Authorization",token)
            .queryParam("limit", 10)
            .queryParam("sortby", "nome")
            .queryParam("order", "DESC")
            .when()
                .get("/device")
            .then()
                .statusCode(is(401))
                .body("msg", is("Access forbidden for this device."));
    }

    @Test
    @Order(7)
    void deveObterUmDispositivoComSucesso() {
        String token = UserUtils.tokenGenerator();
            given()
                .header("Authorization", token)
            .when()
                .get("/device/1")
            .then()
                .statusCode(is(200))
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("dispositivoJsonSchema.json"));
    }

    @Test
    @Order(8)
    void deveObterUmDispositivoSemSucessoNaoAutorizado() {
        given()
        .when()
                .get("/device/1")
        .then()
                .statusCode(is(401))
                .body("msg",is("Device not logged in!"));
    }

    @Test
    @Order(9)
    void deveObterUmDispositivoSemSucessoNotFound() {
        String token = UserUtils.tokenGenerator();
        given()
                .header("Authorization", token)
        .when()
            .get("/device/10000")
        .then()
            .statusCode(is(404))
                .body("msg",is("Device not found."));
    }

    @Test
    @Order(9)
    void deveObterUmDispositivoSemSucessoSemPermissao() {
        String token = JWTGenerator.tokenGenerator("NoRole","123.456.789-00");
        given()
                .header("Authorization", token)
        .when()
            .get("/device/1")
        .then()
            .statusCode(is(403))
            .body("msg",is( "Access forbidden for this device."));
    }

    @Test
    @Order(10)
        void deveAtualizarUmDispositivoComSucesso(){
        String token = UserUtils.tokenGenerator();
        String dispositivo = DispositivoUtils.criarPostDispositivoAtualizado().toString();

        given()
            .header("Authorization", token)
            .contentType(ContentType.JSON)
            .body(dispositivo)
        .when()
            .patch("/device/1")
        .then()
            .statusCode(200)
            .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("dispositivoJsonSchema.json"));
    }

    @Test
    @Order(12)
    void deveAtualizarUmDispositivoSemSucessoNaoEncontrado(){
        String token = UserUtils.tokenGenerator();
        String dispositivo = DispositivoUtils.criarPostDispositivoAtualizado().toString();

        given()
            .header("Authorization", token)
            .contentType(ContentType.JSON)
            .body(dispositivo)
        .when()
            .patch("/device/1000")
        .then()
            .statusCode(404)
            .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("dispositivoJsonSchema.json"));
    }

    @Test
    @Order(13)
    void deveAtualizarUmDispositivoSemSucessoSemAutorizacao(){

        String dispositivo = DispositivoUtils.criarPostDispositivoAtualizado().toString();

        given()
                .contentType(ContentType.JSON)
                .body(dispositivo)
        .when()
            .patch("/device/1")
        .then()
            .statusCode(401)
            .body("msg",is("Device not logged in"));
    }

    @Test
    @Order(14)
    void deveAtualizarUmDispositivoSemSucessoSemPermissao(){
        String token = JWTGenerator.tokenGenerator("NoRole","123.456.789-00");

        String dispositivo = DispositivoUtils.criarPostDispositivoAtualizado().toString();

        given()
            .header("Authorization", token)
            .contentType(ContentType.JSON)
            .body(dispositivo)
        .when()
            .patch("/device/1")
        .then()
            .statusCode(403)
            .body("msg",is("Access forbidden for this Device."));
    }


    @Test
    @Order(15)
    void deveDeletarUmDispositivoComSucesso(){
        String token = UserUtils.tokenGenerator();

        given()
            .header("Authorization", token)
        .when()
            .delete("/device/1")
        .then()
            .statusCode(204);
    }


    @Test
    @Order(16)
    void deveDeletarUmDispositivoSemSucessoNotFound(){
        String token = UserUtils.tokenGenerator();

        given()
                .header("Authorization", token)
        .when()
                .delete("/device/1000")
        .then()
                .statusCode(404);
    }

    @Test
    @Order(17)
    void deveDeletarUmDispositivoSemSucessoNaoAutorizado(){
        given()
        .when()
                .delete("/device/1")
        .then()
                .statusCode(401);
    }

    @Test
    @Order(18)
    void deveDeletarUmDispositivoSemSucessoSemPermissao() {
        String token = UserUtils.tokenGenerator();

        given()
            .header("Authorization", token)
        .when()
            .delete("/device/1")
        .then()
            .statusCode(403);
    }
}
