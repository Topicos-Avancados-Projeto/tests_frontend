package resources;

import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.junit.jupiter.api.*;
import resources.utils.BrokerUtils;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BrokerTest {


    @BeforeAll
    static void setUp() {
        baseURI = "http://localhost:3003";
    }
    //Função POST


    @Test
    @Order(1)
    void deveResgistrarUmNovoClienteBrokerComSucesso(){

        String broker = BrokerUtils.criarPostBroker().toString();
        given()
                .contentType(ContentType.JSON)
                .body(broker)
        .when()
                .post("/broker_client")
        .then()
                .statusCode(is(201))
                .header("Location",is(notNullValue()))
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("brokerJsonSchema.json"));
    }

    @Test
    @Order(2)
    void deveNaoResgistrarUmNovoClienteBrokerComProblemaValidacao(){

        String broker = BrokerUtils.criarPostBrokerInvalido().toString();
        given()
                .contentType(ContentType.JSON)
                .body(broker)
        .when()
                .post("/broker_client")
        .then()
                .statusCode(is(422))
                .body("msg",is("Syntax Error!"));
    }

    @Test
    @Order(3)
    void deveNaoResgistrarUmNovoClienteBrokerComConflito(){

        String broker = BrokerUtils.criarPostBroker().toString();
        given()
                .contentType(ContentType.JSON)
                .body(broker)
        .when()
                .post("/broker_client")
        .then()
                .statusCode(is(409))
                .body("msg",is("Broker Client Already exists!"));
    }


    //Função Get
    @Test
    @Order(4)
    void deveObterOsClientesDoBrokerComSucesso(){

        given()
        .when()
                .get("/broker_client")
        .then()
                .statusCode(is(200))
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("brokersJsonSchema.json"));
    }

    
    @Test
    @Order(5)
    void deveObterClienteBrokerComSucesso() {

        given()
        .when()
                .get("/broker_client/1")
        .then()
                .statusCode(is(200))
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("brokerJsonSchema.json"));
    }
    
    // FUNÇÃO PATCH

    @Test
    @Order(6)
    void deveAtualizarClientBrokerComSucesso(){

        String broker = BrokerUtils.criarPostBroker().toString();

        given()
                .contentType(ContentType.JSON)
                .body(broker)
        .when()
                .patch("/broker_client/1")
        .then()
                .statusCode(is(204));
    }


    @Test
    @Order(7)
    void deveAtualizarClientBrokerSemSucessoEComDadosRepetidos(){

        BrokerUtils.criarPostBrokerAlternativoParaTestarAtualizacao();

        String broker = BrokerUtils.criarPostBroker().toString();
        given()
                .contentType(ContentType.JSON)
                .body(broker)
        .when()
                .patch("/broker_client/2")
        .then()
                .statusCode(is(409))
                .body("msg",is("Broker Client Already exists!"));
    }

    @Test
    @Order(8)
    void deveAtualizarUmUsuarioSemSucessoENotFound(){

        String broker = BrokerUtils.criarPostBroker().toString();

        given()
                .contentType(ContentType.JSON)
                .body(broker)
        .when()
                .patch("/broker_client/100000000")
        .then()
                .statusCode(is(404))
                .body("msg",is("Broker Client not found."));
    }

  //Função DELETE
    
    @Test
    @Order(9)
    void deveRemoverUmClienteBrokerComSucesso(){

        given()
        .when()
            .delete("/broker_client/1")
        .then()
                .statusCode(is(204));
    }
    
    @Test
    @Order(10)
    void deveRemoverUmClienteBrokerSemSucessoENotFound(){

        given()
        .when()
            .delete("/broker_client/10000")
        .then()
                .statusCode(is(404))
                .body("msg",is("Broker Client not found."));
    }
    
}