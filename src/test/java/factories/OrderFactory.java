package factories;

import models.OrderDTO;
import net.datafaker.Faker;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class OrderFactory {
    public OrderDTO getOrderWithCorrectData(){
        Faker faker = new Faker();
        return OrderDTO.builder()
                .id(faker.number().positive())
                .petId(faker.number().positive())
                .quantity(faker.number().numberBetween(1, 3))
                .shipDate(faker.date().future(2, TimeUnit.DAYS, new Date()))
                .status("placed")
                .complete(faker.bool().bool()).build();
    }
}
