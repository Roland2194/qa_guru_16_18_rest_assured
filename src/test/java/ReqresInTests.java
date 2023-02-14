import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.*;
import static org.hamcrest.core.Is.is;

public class ReqresInTests {
    String baseURI = "https://reqres.in";

    @Test
    void createUser() {
        given()
                .baseUri(baseURI)
                .body("{ \"name\": \"morpheus\", \"job\": \"leader\" }")
                .log().uri()
                .log().body()
                .contentType(JSON)
                .when()
                .post("/api/users")
                .then()
                .log().body()
                .statusCode(201)
                .body("job", is("leader"));
    }

    @Test
    void notFoundUserTest() {
        given()
                .baseUri(baseURI)
                .when()
                .get("/api/users/23")
                .then()
                .log().status()
                .statusCode(404);
    }

    @Test
    void updateUsersTest() {
        given()
                .baseUri(baseURI)
                .body("{ \"name\": \"morpheus\", \"job\": \"zion resident\" }")
                .contentType(JSON)
                .when()
                .put("/api/users/2")
                .then()
                .log().body()
                .statusCode(200)
                .body("job", is("zion resident"));
    }

    @Test
    void deleteUsersTest() {
        given()
                .baseUri(baseURI)
                .contentType(JSON)
                .when()
                .delete("/api/users/2")
                .then()
                .statusCode(204);
    }

    @Test
    void successfulLoginUsersTest() {
        given()
                .baseUri(baseURI)
                .body("{ \"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\" }")
                .contentType(JSON)
                .when()
                .post("/api/login")
                .then()
                .log().body()
                .statusCode(200)
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    void unsuccessfulLoginUsersTest() {
        given()
                .baseUri(baseURI)
                .body("{ \"email\": \"sydney@fife\" }")
                .contentType(JSON)
                .when()
                .post("/api/login")
                .then()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing password"));
    }
}
