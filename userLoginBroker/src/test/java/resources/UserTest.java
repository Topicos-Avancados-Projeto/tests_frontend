package resources;

import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

class UserTest {

        @BeforeAll
         static void setUp() {
            baseURI = "http://localhost:3003";
        }


    // FUNÇÃO POST

    @Test
    @Order(1)
    void deveRegistrarUmUsuarioComSucesso() {

        User usuario = new User("João da Silva", "123.456.789-00", "joao@example.com", new Date());

        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        String expectedDateOfBirth = sdf.format(usuario.getDateOfBirth());

        given()
                .contentType(ContentType.JSON)
                .body("{\"name\":\"" + usuario.getName() +
                        "\",\"cpf\":\"" + usuario.getCpf() +
                        "\",\"email\":\"" + usuario.getEmail() +
                        "\",\"password\":\"12345\",\"date_of_birth\":\"" + expectedDateOfBirth + "\"}")
        .when()
                .post("/user")
        .then()
                .statusCode(is(201))
                .body("id", is(notNullValue()))
                .body("name", is(usuario.getName()))
                .body("cpf", is(usuario.getCpf()))
                .body("email", is(usuario.getEmail()))
                .body("date_of_birth", is(expectedDateOfBirth))
                .header("Authorization", is(notNullValue()));
    }

    @Test
    @Order(2)
    void deveRegistrarUmUsuarioSemSucessoComConflitoDeCPF(){

        User usuario = new User("João da Silva", "123.456.789-00", "joao@example.com", new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        String expectedDateOfBirth = sdf.format(usuario.getDateOfBirth());

        given()
                .contentType(ContentType.JSON)
                .body("{\"name\":\"" + usuario.getName() +
                        "\",\"cpf\":\"" + usuario.getCpf() +
                        "\",\"email\":\"" + usuario.getEmail() +
                        "\",\"password\":\"12345\",\"date_of_birth\":\"" + expectedDateOfBirth + "\"}")
        .when()
                .post("/user")
        .then()
                .statusCode(is(409))
                .body("msg",is("CPF already exists!"));
    }


    @Test
    @Order(3)
    void deveRegistrarUmUsuarioSemSucessoEComProblemaDeValidacao() {

        given()
                .contentType(ContentType.JSON)
                .body("{\"name\":\"" + "" +
                        "\",\"cpf\":\"" + "" +
                        "\",\"email\":\"" + ""
                        + "\",\"password\":\"12345\",\"date_of_birth\":\"" + "" + "\"}")
        .when()
                .post("/user")
        .then()
                .statusCode(is(422))
                .body("msg", is("Validation problem"));
    }

        //FUNÇÃO GET

    @Test
    @Order(4)
    void deveObterUsuariosComSucesso() {

        given()
        .when()
                .get("/user")
        .then()
                .statusCode(is(200))
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("usersJsonSchema.json"));
    }



    @Test
    @Order(5)
    void deveDetalharUmUsuarioComSucesso(){
        given()
        .when()
                .get("/user/1")
        .then()
                .statusCode(is(200))
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("userJsonSchema.json"));
    }


    @Test
    @Order(6)
    void deveDetalharUmUsuarioSemSucessoENaoEncontrado(){

        given()
        .when()
                .get("/user/1000")
        .then()
                .statusCode(is(404))
                .body("msg",is("User not found."));
    }



    // FUNÇÃO PATCH

    @Test
    @Order(7)
    void deveAtualizarUmUsuarioComSucesso(){

        User usuario = new User("João da Silva", "123.456.789-00", "joao@example.com", new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        String expectedDateOfBirth = sdf.format(usuario.getDateOfBirth());

        given()
                .contentType(ContentType.JSON)
                .body("{\"name\":\"" + usuario.getName() +
                        "\",\"cpf\":\"" + usuario.getCpf() +
                        "\",\"email\":\"" + usuario.getEmail() +
                        "\",\"password\":\"12345\",\"date_of_birth\":\"" + expectedDateOfBirth + "\"}")
        .when()
                .patch("/user/1")
        .then()
                .statusCode(is(200));
    }

    @Test
    @Order(8)
    void deveAtualizarUmUsuarioSemSucessoENaoEncontrado(){

        given()
        .when()
                .patch("/user/1000")
        .then()
                .statusCode(is(404))
                .body("msg",is("User not found."));
    }

    @Test
    @Order(9)
    void deveAtualizarUmUsuarioSemSucessoEComProblemaDeValidacao(){


        given()
                .contentType(ContentType.JSON)
                .body("{\"name\":\"" + "" +
                        "\",\"cpf\":\"" + "" +
                        "\",\"email\":\"" + ""
                        + "\",\"password\":\"12345\",\"date_of_birth\":\"" + "" + "\"}")
                .pathParam("id", 1)
        .when()
                .patch("/user/{id}")
        .then()
                .statusCode(is(422))
                .body("msg",is("Validation Problem."));

    }

        // FUNÇÃO DELETE

    @Test
    @Order(10)
    void deveRemoverUmUsuarioComSucesso(){

        given()
        .when()
            .delete("/user/1")
        .then()
                .statusCode(is(204));
    }

    @Test
    @Order(11)
    void deveRemoverUmUsuarioSemSucessoENaoEncontrado(){

        given()
        .when()
                .delete("/user/10000")
        .then()
            .statusCode(is(404))
            .body("msg",is("User not found."));
    }
}
