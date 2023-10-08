package resources;

import com.google.gson.JsonObject;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import resources.utils.JWTGenerator;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

class LoginTest {

    @BeforeAll
    static void setUp() {
        baseURI = "http://localhost:3003";
    }

    @Test
    void deveLogarComSucesso() {

        given()
                .contentType(ContentType.JSON)
                .body("{\"cpf\":\"" + "123.456.789-00" + "\",\"password\":\"123456\"}")
        .when()
                .post("/login")
        .then()
                .statusCode(is(201))
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("loginJsonSchema.json"))
                .header("Authorization", is(notNullValue()));

    }

    @Test
    void deveLogarSemSucessoCamposIncorretos(){

        given()
                .contentType(ContentType.JSON)
                .body("{\"cpf\":\"" + "123.456.789-01" + "\",\"password\":\"1234567\"}")
        .when()
                .post("/login")
        .then()
                .statusCode(is(401))
                .body("msg",is("Incorrect CPF or Password!"));
    }


    @Test
    void deveLogarSemSucessoCamposInvalidados(){

        given()
                .contentType(ContentType.JSON)
                .body("{\"cpf\":\"" + " "  + "\",\"password\":\" \"}") // ta passando quando é vazio ""
        .when()
                .post("/login")
        .then()
                .statusCode(is(422))
                .body("msg",is("Validation Problem."));
    }

    @Test

    void deveLogarSemSucessoComRequestMalFormado(){

        given()
                .contentType(ContentType.JSON)
                .body("{\"cpf\":\"" + "02734" + "\",\"password\":\"1\"}")
        .when()
            .post("/login")
        .then()
            .statusCode(is(422))
            .body("msg",is("Validation Problem."));
    }

    @Test
    void devePegarAsInformacoesDoUsuarioComProblemaDeAutorizacao(){
        given()
        .when()
            .get("/login")
        .then()
            .statusCode(is(401))
                .body("msg",is("User not logged in!"));
    }
    @Test
    void deveProibirOAcessoASolicitacaoDeInformacoesDeLogin(){
        String token = JWTGenerator.tokenGenerator("","123.456.789-01");

        given()
                .header("Authorization",token)
        .when()
            .get("/login")
        .then()
            .statusCode(is(403))
            .body("msg",is("Access forbidden for this user."));
    }

    @Test
        void deveAutorizarOAccessoSolicitadoDeInformaçõesDeLogin(){
        String token = JWTGenerator.tokenGenerator("owner","123.456.789-00");

        given()
                .header("Authorization",token)
        .when()
            .get("/login")
        .then()
            .statusCode(is(200))
            .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("loginJsonSchema.json"));
    }

}