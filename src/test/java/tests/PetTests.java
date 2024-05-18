package tests;

import constants.Constants;
import factories.PetFactory;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import models.PetDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import services.PetService;

import static org.junit.jupiter.api.Assertions.*;


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
        assertAll(
                ()-> assertEquals(petDTO.getName(), responsePet.getName()),
                ()-> assertEquals(200, response.statusCode())
        );
    }
    @Test
    public void create_AddPetAndAssertResponseWithJsonSchema_Success(){
        //arrange
        String jsonSchemaPath = "PetSchema.json";
        PetDTO petDTO = petFactory.getBasicPetWithCorrectData();

        //act
        Response response = petService.addPet(petDTO);

        //assert
        response
                .then()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(jsonSchemaPath));
    }
    @Test
    public void create_AddPetWithIncorrectData_StatusCode400(){
        //arrange
        PetDTO petDTO = petFactory.getBasicPetWithCorrectData();

        //act
        Response response = petService.addPetWithIncorrectBody(petDTO.toString());

        //assert
        assertAll(
                ()-> assertEquals(response.then().extract().body().jsonPath().get("message"), "bad input"),
                ()-> assertEquals(400, response.statusCode())
        );
    }
    @Test
    public void read_GetExistingPet_Success(){
        //arrange
        PetDTO petDTO = petFactory.getBasicPetWithCorrectData();
        petService.addPet(petDTO);

        //act
        Response response = petService.getPet(petDTO.getId());

        //assert
        PetDTO responsePet = response.then().extract().as(PetDTO.class);
        assertAll(
                ()->assertEquals(200, response.statusCode()),
                ()->assertEquals(petDTO.getName(), responsePet.getName())
        );
    }
    @Test
    public void read_GetExistingPetAndAssertSchema_SchemaIsValid(){
        //arrange
        String jsonSchemaPath = "PetSchema.json";
        PetDTO petDTO = petFactory.getBasicPetWithCorrectData();
        petService.addPet(petDTO);

        //act
        Response response = petService.getPet(petDTO.getId());

        //assert
        response
                .then()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(jsonSchemaPath));
    }
    @Test
    public void read_GetNotExistingPet_StatusCode404(){
        //arrange
        //act
        Response response = petService.getPet(-100);

        //assert
        assertAll(
                ()-> assertEquals(response.then().extract().body().jsonPath().get("message"), "Pet not found"),
                ()-> assertEquals(404, response.statusCode())
        );
    }
    @Test
    public void update_UploadFileOnExistingPet_Success(){
        //arrange
        String imgPath = "resources/image.png";
        PetDTO petDTO = petFactory.getBasicPetWithCorrectData();
        petService.addPet(petDTO);

        //act
        Response response = petService.uploadImage(petDTO.getId(), imgPath);

        //assert
        String message = response.then().extract().body().jsonPath().get("message");
        assertAll(
                ()->assertEquals(200, response.statusCode()),
                ()->assertTrue(message.contains("File uploaded"))
        );
    }
    @Test
    public void update_UpdateExistingPet_Success(){
        //arrange
        String newPetName = "Pet1";
        PetDTO petDTO = petFactory.getBasicPetWithCorrectData();
        petService.addPet(petDTO);
        petDTO.setName(newPetName);

        //act
        Response response = petService.updatePet(petDTO);

        //assert
        PetDTO responsePet = response.then().extract().as(PetDTO.class);
        assertEquals(petDTO.getName(), responsePet.getName());
    }
    @Test
    public void update_UpdateExistingPet_SchemaIsValid(){
        //arrange
        String jsonSchemaPath = "PetSchema.json";
        PetDTO petDTO = petFactory.getBasicPetWithCorrectData();
        petService.addPet(petDTO);
        petDTO.setName("Pet1");

        //act
        Response response = petService.updatePet(petDTO);

        //assert
        response
                .then()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(jsonSchemaPath));
    }
    @Test
    public void update_UpdateExistingPetWithParam_StatusCode200(){
        //arrange
        PetDTO petDTO = petFactory.getBasicPetWithCorrectData();
        petService.addPet(petDTO);

        //act
        Response response = petService.updatePetWithParam(petDTO.getId(), "pet1", "available");

        //assert
        assertEquals(200, response.statusCode());
    }
    @Test
    public void update_UpdateNotExistingPet_StatusCode404(){
        //arrange
        PetDTO petDTO = petFactory.getBasicPetWithCorrectData();
        petDTO.setId(-137845);

        //act
        Response response = petService.updatePetWithParam(petDTO.getId(), "pet1", "available");

        //assert
        assertAll(
                ()-> assertEquals( "not found", response.then().extract().body().jsonPath().get("message")),
                ()-> assertEquals(404, response.statusCode())
        );
    }
    @Test
    public void delete_DeleteExistingPet_Success(){
        //arrange
        PetDTO petDTO = petFactory.getBasicPetWithCorrectData();
        petService.addPet(petDTO);

        //act
        Response response = petService.deletePet(petDTO.getId());

        //assert
        assertEquals(200, response.statusCode());
    }
    @Test
    public void delete_DeleteNotExistingPet_StatusCode404(){
        //arrange

        //act
        Response response = petService.deletePet(-100);

        //assert
        assertEquals(404, response.statusCode());
    }
}
