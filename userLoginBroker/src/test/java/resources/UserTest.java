package resources;

import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.junit.jupiter.api.*;
import resources.utils.JWTGenerator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
                        "\",\"password\":\"123456\",\"date_of_birth\":\"" + expectedDateOfBirth + "\"}")
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
                        "\",\"password\":\"123456\",\"date_of_birth\":\"" + expectedDateOfBirth + "\"}")
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
                .body("{\"name\":\"" + " " +
                        "\",\"cpf\":\"" + " " +
                        "\",\"email\":\"" + " "
                        + "\",\"password\":\"12345\",\"date_of_birth\":\"" + " " + "\"}")
        .when()
                .post("/user")
        .then()
                .statusCode(is(422))
                .body("msg", is("Validation problem"));
    }

    //FUNÇÃO GET

    @Test
    @Order(5)
    void deveObterUsuariosComSucesso() {

        String token = JWTGenerator.tokenGenerator("admin","123.456.789-00");
        given()
                .header("Authorization",token)
        .when()
                .get("/user")
        .then()
                .statusCode(is(200))
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("usersJsonSchema.json"));
    }

    @Test
    @Order(6)
    void deveObterUsuariosComSucessoSemAutorizacão() {

        given()
        .when()
                .get("/user")
        .then()
                .statusCode(is(401))
                .body("msg",is("User not logged in!"));
    }

    @Test
    @Order(7)
    void deveObterUsuariosComSucessoSemPermissao() {

        String token = JWTGenerator.tokenGenerator("owner","123.456.789-00");

        given()
            .header("Authorization",token)
        .when()
            .get("/user")
        .then()
            .statusCode(is(403))
            .body("msg",is("Access forbidden for this user."));
    }


    @Test
    @Order(8)
    void deveDetalharUmUsuarioComSucesso(){

        String token = JWTGenerator.tokenGenerator("admin","123.456.789-00");

        given()
                .header("Authorization",token)
        .when()
                .get("/user/1")
        .then()
                .statusCode(is(200))
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("userJsonSchema.json"));
    }


    @Test
    @Order(9)
    void deveDetalharUmUsuarioSemSucessoENaoEncontrado(){

        String token = JWTGenerator.tokenGenerator("admin","123.456.789-00");

        given()
                .header("Authorization",token)
        .when()
                .get("/user/1000")
        .then()
                .statusCode(is(404))
                .body("msg",is("User not found."));
    }

    @Test
    @Order(10)
    void deveDetalharUmUsuarioSemSucessoESemAutorizacao(){

        given()
        .when()
                .get("/user/1")
        .then()
                .statusCode(is(401))
                .body("msg",is("User not logged in!"));
    }

    @Test
    @Order(11)
    void deveDetalharUmUsuarioSemSucessoESemPermissao(){

        String token = JWTGenerator.tokenGenerator("NoRole","123.456.789-00");

        given()
                .header("Authorization",token)
        .when()
            .get("/user/1")
        .then()
            .statusCode(is(403))
            .body("msg",is("Access forbidden for this user."));
    }


    // FUNÇÃO PATCH

    @Test
    @Order(12)
    void deveAtualizarUmUsuarioComSucesso(){

        String token = JWTGenerator.tokenGenerator("owner","123.456.789-00");

        User usuario = new User("João da Silva", "123.456.789-00", "joaoo@example.com", new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        String expectedDateOfBirth = sdf.format(usuario.getDateOfBirth());

        given()
                .header("Authorization",token)
                .log().all()
                .contentType(ContentType.JSON)
                .body("{\"name\":\"" + usuario.getName() +
                        "\",\"cpf\":\"" + usuario.getCpf() +
                        "\",\"email\":\"" + usuario.getEmail() +
                        "\",\"password\":\"1234567\",\"date_of_birth\":\"" + expectedDateOfBirth + "\"}")
        .when()
                .patch("/user/1")
        .then()
                .statusCode(is(200));
    }

    @Test
    @Order(13)
    void deveAtualizarUmUsuarioSemSucessoENaoEncontrado(){

        String token = JWTGenerator.tokenGenerator("owner","123.456.789-00");

        User usuario = new User("João", "123.456.789-30", "davi@example.com", new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        String expectedDateOfBirth = sdf.format(usuario.getDateOfBirth());

        given()
                .header("Authorization",token)
                .contentType(ContentType.JSON)
                .body("{\"name\":\"" + usuario.getName() +
                        "\",\"cpf\":\"" + usuario.getCpf() +
                        "\",\"email\":\"" + usuario.getEmail() +
                        "\",\"password\":\"12345\",\"date_of_birth\":\"" + expectedDateOfBirth + "\"}")
        .when()
                .patch("/user/1000")
        .then()
                .statusCode(is(404))
                .body("msg",is("User not found."));
    }

    @Test
    @Order(14)
    void deveAtualizarUmUsuarioSemSucessoEComProblemaDeValidacao(){

        String token = JWTGenerator.tokenGenerator("owner","123.456.789-00");

        given()
                .header("Authorization",token)
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

    @Test
    @Order(15)
    void deveAtualizarUmUsuarioSemSucessoESemAutorizacao(){

        User usuario = new User("João", "123.456.789-30", "davi@example.com", new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        String expectedDateOfBirth = sdf.format(usuario.getDateOfBirth());

        given()
                .contentType(ContentType.JSON)
                .body("{\"name\":\"" + usuario.getName() +
                        "\",\"cpf\":\"" + usuario.getCpf() +
                        "\",\"email\":\"" + usuario.getEmail() +
                        "\",\"password\":\"12345\",\"date_of_birth\":\"" + expectedDateOfBirth + "\"}")
                .pathParam("id", 1)
        .when()
            .patch("/user/{id}")
        .then()
            .statusCode(is(401))
            .body("msg",is("User not logged in!"));
    }

    @Test
    @Order(16)
    void deveAtualizarUmUsuarioSemSucessoESemPermissao(){

        String token = JWTGenerator.tokenGenerator("NoRole","123.456.789-00");

        User usuario = new User("João", "123.456.789-00", "davi@example.com", new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        String expectedDateOfBirth = sdf.format(usuario.getDateOfBirth());

        given()
                .header("Authorization",token)
                .contentType(ContentType.JSON)
                .body("{\"name\":\"" + usuario.getName() +
                        "\",\"cpf\":\"" + usuario.getCpf() +
                        "\",\"email\":\"" + usuario.getEmail() +
                        "\",\"password\":\"123456\",\"date_of_birth\":\"" + expectedDateOfBirth + "\"}")
                .pathParam("id", 1)
        .when()
                .patch("/user/{id}")
        .then()
                .statusCode(is(403))
                .body("msg",is("Access forbidden for this user."));

    }

        // FUNÇÃO DELETE

    @Test
    @Order(17)
    void deveRemoverUmUsuarioComSucesso(){

        String token = JWTGenerator.tokenGenerator("owner","123.456.789-00");

        given()
                .header("Authorization",token)
        .when()
            .delete("/user/1")
        .then()
                .statusCode(is(204));
    }

    @Test
    @Order(18)
    void deveRemoverUmUsuarioSemSucessoENaoEncontrado(){
        String token = JWTGenerator.tokenGenerator("owner","123.456.789-00");

        given()
                .header("Authorization",token)
        .when()
                .delete("/user/10000")
        .then()
            .statusCode(is(404))
            .body("msg",is("User not found."));
    }

    @Test
    @Order(19)
    void deveRemoverUmUsuarioSemSucessoESemPermissao(){
        String token = JWTGenerator.tokenGenerator("NoRole","123.456.789-00");

        given()
                .header("Authorization",token)
        .when()
                .delete("/user/1")
        .then()
                .statusCode(is(403))
                .body("msg",is("Access forbidden for this user."));
    }

    @Test
    @Order(20)
    void deveRemoverUmUsuarioSemSucessoESemAutorizacao(){

        given()
        .when()
                .delete("/user/1")
        .then()
                .statusCode(is(401))
                .body("msg",is("User not logged in!"));
    }
}
