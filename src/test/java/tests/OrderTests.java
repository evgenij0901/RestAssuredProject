package tests;

import constants.Constants;
import factories.OrderFactory;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import models.OrderDTO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import services.OrderService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderTests {
    private OrderService orderService;
    private OrderFactory orderFactory;
    private final List<Integer> orderIdsForDelete = new ArrayList<>();



    @BeforeAll
    public void initServices(){
        orderService = new OrderService(Constants.BASE_URL);
        orderFactory = new OrderFactory();
    }

    @Test
    public void read_GetInventory_Success(){
        //arrange

        //act
        Response response = orderService.getInventory();

        //assert
        assertEquals(200, response.statusCode());
    }
    @Test
    public void create_AddOrder_Success(){
        //arrange
        OrderDTO orderDTO = orderFactory.getOrderWithCorrectData();
        orderIdsForDelete.add(orderDTO.getId());

        //act
        Response response = orderService.addOrder(orderDTO);

        //assert
        OrderDTO responseOrder = response.then().extract().as(OrderDTO.class);
        assertAll(
                ()->assertEquals(200, response.statusCode()),
                ()->assertEquals(orderDTO.getId(), responseOrder.getId())
        );
    }
    @Test
    public void create_AddOrderWithIncorrectBody_StatusCode500(){
        //arrange
        OrderDTO orderDTO = orderFactory.getOrderWithCorrectData();
        orderIdsForDelete.add(orderDTO.getId());
        orderDTO.setShipDate("skkfs");

        //act
        Response response = orderService.addOrder(orderDTO);

        //assert
        assertAll(
                ()->assertEquals(500, response.statusCode()),
                ()->assertEquals("something bad happened", response.then().extract().body().jsonPath().get("message"))
        );
    }
    @Test
    public void read_GetExistingOrder_Success(){
        //arrange
        OrderDTO orderDTO = orderFactory.getOrderWithCorrectData();
        orderIdsForDelete.add(orderDTO.getId());
        orderService.addOrder(orderDTO);

        //act
        Response response = orderService.getOrder(orderDTO.getId());

        //assert
        OrderDTO responseOrder = response.then().extract().as(OrderDTO.class);
        assertAll(
                ()->assertEquals(200, response.statusCode()),
                ()->assertEquals(orderDTO.getPetId(), responseOrder.getPetId())
        );
    }
    @Test
    public void read_GetExistingOrder_SchemaIsValid(){
        //arrange
        String path = "OrderSchema.json";
        OrderDTO orderDTO = orderFactory.getOrderWithCorrectData();
        orderIdsForDelete.add(orderDTO.getId());
        orderService.addOrder(orderDTO);

        //act
        Response response = orderService.getOrder(orderDTO.getId());

        //assert
        response
                .then()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(path));
    }
    @Test
    public void read_GetNotExistingOrder_StatusCode404(){
        //arrange
        OrderDTO orderDTO = orderFactory.getOrderWithCorrectData();
        orderIdsForDelete.add(orderDTO.getId());

        //act
        Response response = orderService.getOrder(orderDTO.getId());

        //assert
        assertAll(
                ()->assertEquals(404, response.statusCode()),
                ()->assertEquals("Order not found", response.then().extract().body().jsonPath().get("message"))
        );
    }
    @Test
    public void delete_DeleteExistingOrder_Success(){
        //arrange
        OrderDTO orderDTO = orderFactory.getOrderWithCorrectData();
        orderService.addOrder(orderDTO);

        //act
        Response response = orderService.deleteOrder(orderDTO.getId());

        //assert
        assertEquals(200, response.statusCode());
    }
    @Test
    public void delete_DeleteNotExistingOrder_StatusCode404(){
        //arrange
        OrderDTO orderDTO = orderFactory.getOrderWithCorrectData();

        //act
        Response response = orderService.deleteOrder(orderDTO.getId());

        //assert
        assertAll(
                ()->assertEquals(404, response.statusCode()),
                ()->assertEquals("Order Not Found", response.then().extract().body().jsonPath().get("message"))
        );
    }

    @AfterAll
    public void tearDown(){
        orderIdsForDelete.forEach(o->orderService.deleteOrder(o));
    }

}
