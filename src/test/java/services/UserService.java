package services;

import io.restassured.response.Response;
import models.UserDTO;

import java.util.List;

import static io.restassured.RestAssured.given;

public class UserService extends BaseService{

    private final String basePath = "/user";

    public UserService(String baseUrl) {
        super(baseUrl);
    }
    public Response addUser(UserDTO userDTO)  {
        return given()
                .log().all()
                .spec(requestSpec)
                .when()
                .body(userDTO)
                .post(basePath)
                .then()
                .spec(responseSpec)
                .extract().response();
    }
    public Response addListOfUser(List<UserDTO> userList)  {
        return given()
                .log().all()
                .spec(requestSpec)
                .when()
                .body(userList)
                .post(basePath + "/createWithList")
                .then()
                .spec(responseSpec)
                .extract().response();
    }
    public Response getUser(String username) {
        return given()
                .log().all()
                .spec(requestSpec)
                .pathParam("username", username)
                .when()
                .get(basePath + "/{username}")
                .then()
                .spec(responseSpec)
                .extract()
                .response();
    }
    public Response updateUser(UserDTO userDTO, String username)  {
        return given()
                .log().all()
                .spec(requestSpec)
                .pathParam("username", username)
                .when()
                .body(userDTO)
                .put(basePath + "/{username}")
                .then()
                .spec(responseSpec)
                .extract().response();
    }
    public Response deleteUser(String username) {
        return given()
                .log().all()
                .spec(requestSpec)
                .pathParam("username", username)
                .when()
                .delete(basePath + "/{username}")
                .then()
                .spec(responseSpec)
                .extract()
                .response();
    }
    public Response login(String username, String password) {
        return given()
                .log().all()
                .spec(requestSpec)
                .pathParam("username", username)
                .pathParam("password", password)
                .when()
                .get(basePath + "/login")
                .then()
                .spec(responseSpec)
                .extract()
                .response();
    }
    public Response logout() {
        return given()
                .log().all()
                .spec(requestSpec)
                .when()
                .get(basePath + "/logout")
                .then()
                .spec(responseSpec)
                .extract()
                .response();
    }
}
