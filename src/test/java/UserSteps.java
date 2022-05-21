import client.user.CreateUserClient;
import client.user.DeleteUserClient;
import client.user.LoginUserClient;
import client.user.UpdateUserClient;

public class UserSteps {

    CreateUserClient createUserClient = new CreateUserClient();
    LoginUserClient loginUserClient = new LoginUserClient();
    UpdateUserClient updateUserClient = new UpdateUserClient();
    DeleteUserClient deleteUserClient = new DeleteUserClient();

}
