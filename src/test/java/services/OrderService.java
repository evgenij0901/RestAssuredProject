package services;

import io.restassured.response.Response;
import models.OrderDTO;
import models.PetDTO;

import static io.restassured.RestAssured.given;

public class OrderService extends BaseService{

    private final String basePath = "/store";

    public OrderService(String baseUrl) {
        super(baseUrl);
    }

    public Response getInventory() {
        return given()
                .spec(requestSpec)
                .when()
                .get(basePath + "/inventory")
                .then()
                .spec(responseSpec)
                .extract()
                .response();
    }

    public Response addOrder(OrderDTO orderDTO)  {
        return given()
                .log().all()
                .spec(requestSpec)
                .when()
                .body(orderDTO)
                .post(basePath + "/order")
                .then()
                .spec(responseSpec)
                .extract().response();
    }

    public Response getOrder(int id) {
        return given()
                .log().all()
                .spec(requestSpec)
                .pathParam("id", id)
                .when()
                .get(basePath + "/order/{id}")
                .then()
                .spec(responseSpec)
                .extract()
                .response();
    }

    public Response deleteOrder(int id) {
        return given()
                .log().all()
                .spec(requestSpec)
                .pathParam("id", id)
                .when()
                .delete(basePath + "/order/{id}")
                .then()
                .spec(responseSpec)
                .extract()
                .response();
    }

}
