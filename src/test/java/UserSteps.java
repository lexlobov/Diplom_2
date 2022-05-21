import client.CreateUserClient;
import client.DeleteUserClient;
import client.LoginUserClient;
import client.UpdateUserClient;

public class UserSteps {

    CreateUserClient createUserClient = new CreateUserClient();
    LoginUserClient loginUserClient = new LoginUserClient();
    UpdateUserClient updateUserClient = new UpdateUserClient();
    DeleteUserClient deleteUserClient = new DeleteUserClient();

}
