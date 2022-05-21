import client.user.CreateUserClient;
import client.user.DeleteUserClient;
import client.user.LoginUserClient;
import client.user.UpdateUserClient;
import io.restassured.response.ValidatableResponse;
import model.UserModel;
import org.apache.http.HttpStatus;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

public class UserSteps {

    CreateUserClient createUserClient = new CreateUserClient();
    LoginUserClient loginUserClient = new LoginUserClient();
    UpdateUserClient updateUserClient = new UpdateUserClient();
    DeleteUserClient deleteUserClient = new DeleteUserClient();

    public void createNewUser(UserModel user){
        ValidatableResponse response = createUserClient.createUser(user);
        int statusCode = response.extract().statusCode();
        boolean isSuccess = response.extract().path("success");
        assertThat("Status code should be 201", statusCode, equalTo(HttpStatus.SC_CREATED));
        assertThat("Success should be true", isSuccess, equalTo(true));
    }

    public void loginUserPositive(UserModel user){
        ValidatableResponse response = loginUserClient.loginUser(user);
        int statusCode = response.extract().statusCode();
        boolean isSuccess = response.extract().path("success");
        String accessToken = response.extract().path("accessToken");
        String refreshToken = response.extract().path("refreshToken");
        assertThat("Status code should be 200", statusCode, equalTo(HttpStatus.SC_OK));
        assertThat("Success should be true", isSuccess, equalTo(true));
        assertThat("Access token should be not null", accessToken, equalTo(not(null)));
        assertThat("Refresh token should be not null", refreshToken, equalTo(not(null)));
    }

    public void createNewUserAlreadyExistNegative(UserModel user){
        ValidatableResponse response = createUserClient.createUser(user);
        int statusCode = response.extract().statusCode();
        boolean isSuccess = response.extract().path("success");
        String message = response.extract().path("message");
        assertThat("Status code should be 403", statusCode, equalTo(HttpStatus.SC_FORBIDDEN));
        assertThat("Success should be false", isSuccess, equalTo(false));
        assertThat("Message should be 'User already exists'", message, equalTo("User already exists"));
    }

    public void loginUserNegative(UserModel user){
        ValidatableResponse response = loginUserClient.loginUser(user);
        int statusCode = response.extract().statusCode();
        boolean isSuccess = response.extract().path("success");
        String message = response.extract().path("message");
        assertThat("Status code should be 401", statusCode, equalTo(HttpStatus.SC_UNAUTHORIZED));
        assertThat("Success should be false", isSuccess, equalTo(false));
        assertThat("Message should be 'email or password are incorrect'", message, equalTo("email or password are incorrect"));
    }

}
