package resources;

import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import resources.utils.BrokerUtils;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

class BrokerTest {


    @BeforeAll
    static void setUp() {
        baseURI = "http://localhost:3003";
    }


    //Função POST

    @Test
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
    void deveNaoResgistrarUmNovoClienteBrokerComProblemaValidacao(){

        String broker = BrokerUtils.criarPostBrokerInvalido().toString();
        System.out.println(broker);
        given()
                .contentType(ContentType.JSON)
                .body(broker)
        .when()
                .post("/broker_client")
        .then()
                .statusCode(is(422))
                .body("msg",is("Validation Problem"));
    }

    @Test
    void deveNaoResgistrarUmNovoClienteBrokerComConflito(){

        String broker = BrokerUtils.criarPostBroker().toString();

        given()
                .contentType(ContentType.JSON)
                .body(broker)
        .when()
                .post("/broker_client")
        .then()
                .statusCode(is(409))
                .body("msg",is("Broker Client already exists!"));
    }


    //Função Get
    @Test

    void deveObterOsClientesDoBrokerComSucesso(){

        given()
        .when()
                .get("/broker_client")
        .then()
                .statusCode(is(200))
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("brokerJsonSchema.json"));
    }

    
    @Test
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
    void deveAtualizarClientBrokerSemSucessoEComDadosRepetidos(){

        String broker = BrokerUtils.criarPostBroker().toString();
        given()
                .contentType(ContentType.JSON)
                .body(broker)
        .when()
                .patch("/broker_client/1")
        .then()
                .statusCode(is(409))
                .body("msg",is("Broker Client Already exists"));
    }

    @Test
    void deveAtualizarUmUsuarioSemSucessoENotFound(){

        String broker = BrokerUtils.criarPostBroker().toString();

        given()
                .contentType(ContentType.JSON)
                .body(broker)
        .when()
                .patch("/broker_client/100000000")
        .then()
                .statusCode(is(404))
                .body("msg",is("Broker Client does not exists"));
    }



  //Função DELETE
    
    @Test
    void deveRemoverUmClienteBrokerComSucesso(){

        given()
        .when()
            .delete("/broker_client/1")
        .then()
                .statusCode(is(204));
    }
    
    @Test
    void deveRemoverUmClienteBrokerSemSucessoENotFound(){

        given()
        .when()
            .delete("/broker_client/1")
        .then()
                .statusCode(is(404))
                .body("msg",is("Broker Client does not exists"));
    }

    

    
}