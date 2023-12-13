package resources.utils;

import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class UserUtils {

    public static String
    tokenGenerator(){
       return "Bearer "+ given()
                .contentType(ContentType.JSON)
                .body("{\"cpf\":\"" + "123.456.789-01" + "\",\"password\":\"123456\"}")
            .when()
                .post("/login")
            .then()
                .statusCode(is(201))
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("loginJsonSchema.json"))
                .header("Authorization", is(notNullValue())).extract().header("Authorization");

    }
}
