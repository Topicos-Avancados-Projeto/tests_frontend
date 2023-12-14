package resources.loginUserBroker;

import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import resources.utils.UserUtils;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

class LoginTest {

    @BeforeAll
    static void setUp() {
        baseURI = "http://localhost:3003";
    }

    @Test
    void deveLogarComSucesso() {

        String token = given()
                .contentType(ContentType.JSON)
                .body("{\"cpf\":\"" + "123.456.789-01" + "\",\"password\":\"123456\"}")
        .when()
                .post("/schemas/login")
        .then()
                .statusCode(is(201))
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/login/loginJsonSchema.json"))
                .header("Authorization", is(notNullValue())).extract().header("Authorization");

        System.out.println(token);
    }

    @Test
    void deveLogarSemSucessoCamposIncorretos(){

        given()
                .contentType(ContentType.JSON)
                .body("{\"cpf\":\"" + "123.456.789-00" + "\",\"password\":\"1234567\"}")
        .when()
                .post("/schemas/login")
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
                .post("/schemas/login")
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
            .post("/schemas/login")
        .then()
            .statusCode(is(422))
            .body("msg",is("Validation Problem."));
    }

    @Test
    void devePegarAsInformacoesDoUsuarioComProblemaDeAutorizacao(){
        given()
        .when()
            .get("/schemas/login")
        .then()
            .statusCode(is(401))
                .body("msg",is("User not logged in!"));
    }

    @Test
        void deveAutorizarOAccessoSolicitadoDeInformaçõesDeLogin(){

        String token = UserUtils.tokenGenerator();
        System.out.println(token);
        given()
                .header("Authorization",token)
        .when()
            .get("/schemas/login")
        .then()
            .statusCode(is(200))
            .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/login/loginJsonSchema.json"));
    }

}