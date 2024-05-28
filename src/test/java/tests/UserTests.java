package tests;

import constants.Constants;
import factories.UserFactory;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import models.UserDTO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import services.UserService;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserTests {
    private UserService userService;
    private UserFactory userFactory;
    private final List<String> usernamesForDelete = new ArrayList<>();

    @BeforeAll
    public void initServices(){
        userService = new UserService(Constants.BASE_URL);
        userFactory = new UserFactory();
    }

    @Test
    public void create_AddCorrectUser_Success(){
        //arrange
        UserDTO userDTO = userFactory.getUserWithCorrectData();
        usernamesForDelete.add(userDTO.getUsername());

        //act
        Response response = userService.addUser(userDTO);

        //assert
        assertEquals(200, response.statusCode());
    }
    @Test
    public void create_AddUserWithWrongParam_ErrorCode(){
        //arrange
        UserDTO userDTO = userFactory.getUserWithCorrectData();
        userDTO.setUserStatus(1299299102);

        //act
        Response response = userService.addUser(userDTO);

        //assert
        assertEquals(404, response.statusCode());
    }
    @Test
    public void create_AddListOfUsers_Success(){
        //arrange
        UserDTO user1 = userFactory.getUserWithCorrectData();
        UserDTO user2 = userFactory.getUserWithCorrectData();
        List<UserDTO> userDTOList = new ArrayList<>();
        userDTOList.add(user1);
        userDTOList.add(user2);

        //act
        Response response = userService.addListOfUser(userDTOList);

        //assert
        assertEquals(200, response.statusCode());
    }
    @Test
    public void create_AddEmptyUserList_ErrorCode(){
        //arrange
        List<UserDTO> userDTOList = new ArrayList<>();

        //act
        Response response = userService.addListOfUser(userDTOList);

        //assert
        assertEquals(404, response.statusCode());
    }
    @Test
    public void read_GetExistingUser_Success(){
        //arrange
        UserDTO userDTO = userFactory.getUserWithCorrectData();
        usernamesForDelete.add(userDTO.getUsername());
        userService.addUser(userDTO);

        //act
        Response response = userService.getUser(userDTO.getUsername());

        //assert
        UserDTO responseUser = response.then().extract().as(UserDTO.class);
        assertAll(
                ()->assertEquals(userDTO.getFirstName(), responseUser.getFirstName()),
                ()->assertEquals(200, response.statusCode()));
    }
    @Test
    public void read_GetUser_SchemaIsValid(){
        //arrange
        String path = "UserSchema.json";
        UserDTO userDTO = userFactory.getUserWithCorrectData();
        usernamesForDelete.add(userDTO.getUsername());
        userService.addUser(userDTO);

        //act
        Response response = userService.getUser(userDTO.getUsername());

        //assert
        response
                .then()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(path));
    }
    @Test
    public void read_GetNotExistingUser_ErrorCode(){
        //arrange
        UserDTO userDTO = userFactory.getUserWithCorrectData();

        //act
        Response response = userService.getUser(userDTO.getUsername());

        //assert
        assertAll(
                ()->assertEquals(404, response.statusCode()),
                ()->assertEquals("User not found", response.then().extract().body().jsonPath().get("message"))
        );
    }
    @Test
    public void update_UpdateExistingUser_Success(){
        //arrange
        UserDTO userDTO = userFactory.getUserWithCorrectData();
        usernamesForDelete.add(userDTO.getUsername());
        userService.addUser(userDTO);
        userDTO.setFirstName("Misha");

        //act
        Response response = userService.updateUser(userDTO, userDTO.getUsername());

        //assert
        assertEquals(200, response.statusCode());
    }
    @Test
    public void update_UpdateNotExistingUser_ErrorCode(){
        //arrange
        UserDTO userDTO = userFactory.getUserWithCorrectData();

        //act
        Response response = userService.updateUser(userDTO, userDTO.getUsername());

        //assert
        assertAll(
                ()->assertEquals(404, response.statusCode()),
                ()->assertEquals("User not found", response.then().extract().body().jsonPath().get("message"))
        );
    }
    @Test
    public void delete_DeleteExistingUser_Success(){
        //arrange
        UserDTO userDTO = userFactory.getUserWithCorrectData();
        userService.addUser(userDTO);

        //act
        Response response = userService.deleteUser(userDTO.getUsername());

        //assert
        assertEquals(200, response.statusCode());
    }
    @Test
    public void delete_DeleteNotExistingUser_Success(){
        //arrange
        UserDTO userDTO = userFactory.getUserWithCorrectData();

        //act
        Response response = userService.deleteUser(userDTO.getUsername());

        //assert
        assertEquals(404, response.statusCode());
    }
    @Test
    public void login_LoginWithCorrectCred_Success(){
        //arrange
        UserDTO userDTO = userFactory.getUserWithCorrectData();
        usernamesForDelete.add(userDTO.getUsername());
        userService.addUser(userDTO);

        //act
        Response response = userService.login(userDTO.getUsername(), userDTO.getPassword());

        //assert
        assertEquals(200, response.statusCode());
    }
    @Test
    public void login_LoginWithIncorrectCred_ErrorCode(){
        //arrange
        UserDTO userDTO = userFactory.getUserWithCorrectData();

        //act
        Response response = userService.login(userDTO.getUsername(), userDTO.getPassword());

        //assert
        assertEquals(400, response.statusCode());
    }
    @Test
    public void logout_Logout_Success(){
        //arrange
        //act
        Response response = userService.logout();

        //assert
        assertEquals(200, response.statusCode());
    }

    @AfterAll
    public void tearDown(){
        usernamesForDelete.forEach(u->userService.deleteUser(u));
    }
}
