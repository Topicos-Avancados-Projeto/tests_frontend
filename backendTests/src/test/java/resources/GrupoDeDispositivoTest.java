package resources;

import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import resources.utils.GruposDeDispositivoUtils;
import resources.utils.TipoDeDispositivoUtils;
import resources.utils.UserUtils;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GrupoDeDispositivoTest {

    @Test
    @Order(1)
    void deveRegistarUmNovoGrupoDeDispositivoComSucesso() {

        String token = UserUtils.tokenGenerator();
        String GrupoDeDispositivo = TipoDeDispositivoUtils.criarPostTipoDeDispositivoComSucesso().toString();

        given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .body(GrupoDeDispositivo)
                .when()
                .post("/device_group")
                .then()
                .statusCode(201)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("gruposDeDispositivoJsonSchema.json"));
    }


    @Test
    @Order(2)
    void deveRegistrarUmNovoGrupoDeDispositivoSemSucessoComProblemaDeValidacao() {

        String token = UserUtils.tokenGenerator();
        String grupoDeDispositivo = GruposDeDispositivoUtils.criarPostGrupoDeDispositivoComSucesso().toString();

        given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .body(grupoDeDispositivo)
                .when()
                .post("/device_group")
                .then()
                .statusCode(409)
                .body("msg", is("Validation problem"));
    }


    @Test
    @Order(3)
    void deveRegistrarUmNovoGrupoDeDispositivoSemSucessoComErroDeAutorizacao() {

        String grupoDeDispositivo = GruposDeDispositivoUtils.criarPostGrupoDeDispositivoComSucesso().toString();

        given()
                .contentType(ContentType.JSON)
                .body(grupoDeDispositivo)
                .when()
                .post("/device_group")
                .then()
                .statusCode(401)
                .body("msg", is("user not logged in!"));
    }


    @Test
    @Order(4)
    void deveRegistrarUmNovoGrupoDeDispositivoSemSucessoSemPermissao() {

        String grupoDeDispositivo = GruposDeDispositivoUtils.criarPostGrupoDeDispositivoComSucesso().toString();

        given()
                .contentType(ContentType.JSON)
                .body(grupoDeDispositivo)
                .when()
                .post("/device_group")
                .then()
                .statusCode(403)
                .body("msg", is("Access forbidden for this  devices group."));
    }


    @Test
    @Order(5)
    void deveObterUmaPaginacaoComSucesso() {

        String token = UserUtils.tokenGenerator();

        Response response =
                given()
                        .header("Authorization", token)
                        .queryParam("limit", 10)
                        .queryParam("sortby", "nome")
                        .queryParam("order", "DESC")
                        .when()
                        .get("/device_group")
                        .then()
                        .statusCode(is(200))
                        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("gruposDeDispositivoJsonSchema.json"))
                        .extract().response();

        List<Object> groups = response.getBody().jsonPath().getList("gruposDeDispositivo");

        assertTrue(groups.size() <= 10);
    }

    @Test
    @Order(6)
    void deveObterUmaPaginacaoComSucessoSemAutorizacao() {

            given()
                .queryParam("limit", 10)
                .queryParam("sortby", "nome")
                .queryParam("order", "DESC")
            .when()
                .get("/device_group")
            .then()
                .statusCode(is(401));
    }

    @Test
    @Order(7)
    void deveObterUmGrupoDeDispositivoComSucesso() {

        String token = UserUtils.tokenGenerator();

        given()
            .header("Authorization", token)
        .when()
            .get("/devices group/1")
        .then()
            .statusCode(is(200))
            .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("grupoDeDispositivoJsonSchema.json"));
    }

    @Test
    @Order(8)
    void deveObterUmGrupoDeDispositivoSemSucessoESemAutorizacao() {


            given()
            .when()
                .get("/devices group/1")
            .then()
                .statusCode(is(401));

    }

    @Test
    @Order(9)
    void deveAtualizarUmGrupoDeDispositivoComSucesso(){

        String token = UserUtils.tokenGenerator();
        String grupoDeDispositivo = GruposDeDispositivoUtils.criarPostGrupoDeDispositivoAtualizado().toString();

        given()
            .header("Authorization", token)
            .contentType(ContentType.JSON)
            .body(grupoDeDispositivo)
        .when()
            .patch("/devices group/1")
        .then()
            .statusCode(200)
            .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("grupoDeDispositivoJsonSchema.json"));
    }

    @Test
    @Order(10)
    void deveAtualizarUmGrupoDeDispositivoSemSucessoSemAutorizacao(){

        String grupoDeDispositivo = GruposDeDispositivoUtils.criarPostGrupoDeDispositivoAtualizado().toString();

        given()
            .contentType(ContentType.JSON)
            .body(grupoDeDispositivo)
        .when()
            .patch("/devices group/1")
        .then()
            .statusCode(401);
    }

    @Test
    @Order(11)
    void deveDeletarUmGrupoDeDispositivoComSucesso(){

        String token = UserUtils.tokenGenerator();

        given()
                .header("Authorization", token)
        .when()
                .delete("/devices group/1")
        .then()
                .statusCode(204)
                .body("msg",is("devices group not found."));

    }

    @Test
    @Order(12)
    void deveDeletarUmGrupoDeDispositivoSemSucessoSemAutorizacao(){

        given()
        .when()
            .delete("/devices group/100000")
        .then()
            .statusCode(404)
            .body("msg",is("user not logged in!"));

    }

}