package resources.utils;

import com.google.gson.JsonObject;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class BrokerUtils {

    public static JsonObject criarPostBroker(){
        JsonObject broker = new JsonObject();

        broker.addProperty("user_id", "a3d5f1a7-8a9b-4c6c-9d2e-26a3e7256bf3");
        broker.addProperty("cleansession", true);
        broker.addProperty("name", "John Doe");
        broker.addProperty("description", "Description for John Doe");
        broker.addProperty("broker_port", 1883);
        broker.addProperty("broker_host", "mqtt.example.com");
        broker.addProperty("username", "john_doe");
        broker.addProperty("password", "mySecretPassword");
        broker.addProperty("lastwilltopic", "lastwill_topic");
        broker.addProperty("lastwillqos", 0);
        broker.addProperty("lastwillmessage", "Last Will Message");
        broker.addProperty("lastwillretain", true);
        broker.addProperty("keepalive", 300);

        return broker;
    }

    private static JsonObject criarPostBrokerAlternativo(){
        JsonObject broker = new JsonObject();

        broker.addProperty("user_id", "a3d5f1a7-8a9b-4c6c-9d2e-26a3e7256bf6");
        broker.addProperty("cleansession", true);
        broker.addProperty("name", "Joao Feliz");
        broker.addProperty("description", "Description for John Doe");
        broker.addProperty("broker_port", 1884);
        broker.addProperty("broker_host", "mqtt.example.com");
        broker.addProperty("username", "joaoFeliz");
        broker.addProperty("password", "mySecretPassword");
        broker.addProperty("lastwilltopic", "lastwill_topic");
        broker.addProperty("lastwillqos", 0);
        broker.addProperty("lastwillmessage", "Last Will Message");
        broker.addProperty("lastwillretain", true);
        broker.addProperty("keepalive", 300);

        return broker;
    }

    public static void criarPostBrokerAlternativoParaTestarAtualizacao(){

        String broker = criarPostBrokerAlternativo().toString();
        given()
                .contentType(ContentType.JSON)
                .body(broker)
        .when()
                .post("/broker_client")
        .then()
                .statusCode(is(201))
                .header("Location",is(notNullValue()))
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/broker/brokerJsonSchema.json"));

    }
    public static JsonObject criarPostBrokerInvalido(){
    	JsonObject broker = new JsonObject();

        broker.addProperty("user_id", "");
        broker.addProperty("cleansession", true);
        broker.addProperty("name", "");
        broker.addProperty("description", "");
        broker.addProperty("broker_port", 0);
        broker.addProperty("broker_host", "");
        broker.addProperty("username", "");
        broker.addProperty("password", "");
        broker.addProperty("lastwilltopic", "");
        JsonObject lastwillqos = new JsonObject();
        lastwillqos.addProperty("", 1);
        lastwillqos.addProperty("", "AT_LEAST_ONCE");
        broker.add("lastwillqos", lastwillqos);
        broker.addProperty("lastwillmessage", "");
        broker.addProperty("lastwillretain", true);
        broker.addProperty("keepalive", 300);

        return broker;
    }

}
