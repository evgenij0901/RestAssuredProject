package services;
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
}
