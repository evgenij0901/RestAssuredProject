package services;
import constants.Constants;
import io.restassured.response.Response;

import models.PetDTO;

import static io.restassured.RestAssured.given;

public class PetService extends BaseService{

    private final String basePath = "/pet";

    public PetService(String baseUrl) {
        super(baseUrl);
    }

    public Response addPet(PetDTO petDTO)  {
        return given()
                .spec(requestSpec)
                .when()
                .body(petDTO)
                .post(basePath)
                .then()
                .spec(responseSpec)
                .extract().response();
    }

    public Response addPetWithIncorrectBody(String body)  {
        return given()
                .spec(requestSpec)
                .when()
                .body(body)
                .post(basePath)
                .then()
                .spec(responseSpec)
                .extract().response();
    }

    public Response getPet(int id) {
        return given()
                .log().all()
                .spec(requestSpec)
                .pathParam("id", id)
                .when()
                .get(basePath + "/{id}")
                .then()
                .spec(responseSpec)
                .extract()
                .response();
    }

    public Response uploadImage(int id, String imgPath){
        return given()
                .baseUri(Constants.BASE_URL)
                .multiPart("file", imgPath)
                .header("accept", "application/json")
                .contentType("multipart/form-data")
                .pathParam("id", id)
                .when()
                .post(basePath + "/{id}/uploadImage")
                .then()
                .spec(responseSpec)
                .extract()
                .response();
    }
    public Response updatePet(PetDTO petDTO)  {
        return given()
                .baseUri(Constants.BASE_URL)
                .when()
                .body(petDTO)
                .put(basePath)
                .then()
                .spec(responseSpec)
                .extract().response();
    }
    public Response updatePetWithParam(int id, String name, String status){
        return given()
                .baseUri(Constants.BASE_URL)
                .header("accept", "application/json")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .formParam("name", name)
                .formParam("status", status)
                .pathParam("id", id)
                .when()
                .post(basePath + "/{id}")
                .then()
                .spec(responseSpec)
                .extract().response();
    }
    public Response deletePet(int id) {
        return given()
                .log().all()
                .spec(requestSpec)
                .pathParam("id", id)
                .when()
                .delete(basePath + "/{id}")
                .then()
                .spec(responseSpec)
                .extract()
                .response();
    }
}
