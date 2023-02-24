package tests;

import models.*;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.LoginSpecs.*;

public class ReqresInTests extends TestBase {

    @Test
    void createUser() {
        CreateAndUpdateUsersBodyModel data = new CreateAndUpdateUsersBodyModel();
        data.setName("morpheus");
        data.setJob("leader");
        CreateUsersResponseModel response = given()
                .log().uri()
                .log().headers()
                .log().body()
                .body(data)
                .contentType(JSON)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .extract().as(CreateUsersResponseModel.class);

        assertThat(response.getJob()).isEqualTo(data.getJob());
    }

    @Test
    void notFoundUserTest() {
        given()
                .when()
                .get("/users/23")
                .then()
                .log().status()
                .statusCode(404);
    }

    @Test
    void updateUsersTest() {
        CreateAndUpdateUsersBodyModel data = new CreateAndUpdateUsersBodyModel();
        data.setName("morpheus");
        data.setJob("zion resident");
        UpdateUsersResponseModel response = given()
                .body(data)
                .contentType(JSON)
                .when()
                .put("/users/2")
                .then()
                .log().body()
                .statusCode(200)
                .extract().as(UpdateUsersResponseModel.class);

        assertThat(response.getJob()).isEqualTo(data.getJob());
    }

    @Test
    void deleteUsersTest() {
        given()
                .contentType(JSON)
                .when()
                .delete("/users/2")
                .then()
                .statusCode(204);
    }

    @Test
    void successfulLoginUsersTest() {
        LoginModel data = new LoginModel();
        data.setEmail("eve.holt@reqres.in");
        data.setPassword("cityslicka");
        LoginResponseModel response = given(loginRequestSpec)
                .body(data)
                .when()
                .post("/login")
                .then()
                .spec(loginResponseSpec)
                .extract().as(LoginResponseModel.class);

        assertThat(response.getToken()).isEqualTo("QpwL5tke4Pnpja7X4");
    }

    @Test
    void unsuccessfulLoginUsersTest() {
        LoginModel data = new LoginModel();
        data.setEmail("peter@klaven");
        UnsuccessfulLoginResponseModel response = given(loginRequestSpec)
                .body(data)
                .when()
                .post("/login")
                .then()
                .spec(unsuccessfulLoginResponseSpec)
                .extract().as(UnsuccessfulLoginResponseModel.class);

        assertThat(response.getError()).isEqualTo("Missing password");
    }
}
