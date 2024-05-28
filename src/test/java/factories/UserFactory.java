package factories;

import models.UserDTO;
import net.datafaker.Faker;

public class UserFactory {
    public UserDTO getUserWithCorrectData(){
        Faker faker = new Faker();
        return UserDTO.builder()
                .id(faker.number().positive())
                .username(faker.name().username())
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .email(faker.internet().emailAddress())
                .password(faker.internet().password())
                .phone("+79110887563")
                .userStatus(0).build();
    }
}
