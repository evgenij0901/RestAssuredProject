package tests;

import constants.Constants;
import factories.UserFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import services.UserService;
import java.util.ArrayList;
import java.util.List;

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
        //act
        //assert
    }
    @Test
    public void create_AddUserWithWrongParam_ErrorCode(){
        //arrange
        //act
        //assert
    }
    @Test
    public void create_AddListOfUsers_Success(){
        //arrange
        //act
        //assert
    }
    @Test
    public void create_AddEmptyUserList_ErrorCode(){
        //arrange
        //act
        //assert
    }
    @Test
    public void read_GetExistingUser_Success(){
        //arrange
        //act
        //assert
    }
    @Test
    public void read_GetUser_SchemaIsValid(){
        //arrange
        //act
        //assert
    }
    @Test
    public void read_GetNotExistingUser_ErrorCode(){
        //arrange
        //act
        //assert
    }
    @Test
    public void update_UpdateExistingUser_Success(){
        //arrange
        //act
        //assert
    }
    @Test
    public void update_UpdateNotExistingUser_ErrorCode(){
        //arrange
        //act
        //assert
    }
    @Test
    public void update_UpdateWithIncorrectParam_ErrorCode(){
        //arrange
        //act
        //assert
    }
    @Test
    public void delete_DeleteExistingUser_Success(){
        //arrange
        //act
        //assert
    }
    @Test
    public void delete_DeleteNotExistingUser_Success(){
        //arrange
        //act
        //assert
    }
    @Test
    public void login_LoginWithCorrectCred_Success(){
        //arrange
        //act
        //assert
    }
    @Test
    public void login_LoginWithIncorrectCred_ErrorCode(){
        //arrange
        //act
        //assert
    }
    @Test
    public void logout_Logout_Success(){
        //arrange
        //act
        //assert
    }

    @AfterAll
    public void tearDown(){
        usernamesForDelete.forEach(u->userService.deleteUser(u));
    }
}
