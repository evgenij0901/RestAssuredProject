package tests;

import constants.Constants;
import factories.PetFactory;
import io.restassured.response.Response;
import models.PetDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import services.PetService;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PetTests {
    private PetService petService;
    private PetFactory petFactory;

    @BeforeAll
    public void initServices(){
         petService = new PetService(Constants.BASE_URL);
         petFactory = new PetFactory();
    }

    @Test
    public void create_AddPetWithCorrectData_Success(){
        //arrange
        PetDTO petDTO = petFactory.getBasicPetWithCorrectData();

        //act
        Response response = petService.addPet(petDTO);
        PetDTO responsePet = response.then().extract().as(PetDTO.class);

        //assert
        Assertions.assertAll(
                ()->Assertions.assertEquals(petDTO.getName(), responsePet.getName()),
                ()->Assertions.assertEquals(200, response.statusCode())
        );
    }
    @Test
    public void create_AddPetWithIncorrectData_StatusCode400(){
        //arrange
        PetDTO petDTO = petFactory.getBasicPetWithCorrectData();

        //act
        Response response = petService.addPetWithIncorrectBody(petDTO.toString());

        //assert
        Assertions.assertAll(
                ()->Assertions.assertEquals(response.then().extract().body().jsonPath().get("message"), "bad input"),
                ()->Assertions.assertEquals(400, response.statusCode())
        );
    }
}
