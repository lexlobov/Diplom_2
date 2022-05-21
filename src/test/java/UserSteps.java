import client.user.CreateUserClient;
import client.user.DeleteUserClient;
import client.user.LoginUserClient;
import client.user.UpdateUserClient;
import io.qameta.allure.junit4.DisplayName;
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

    @DisplayName("")
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

    public void createNewUserNotEnoughDataNegative(UserModel user){
        ValidatableResponse response = createUserClient.createUser(user);
        int statusCode = response.extract().statusCode();
        boolean isSuccess = response.extract().path("success");
        String message = response.extract().path("message");
        assertThat("Status code should be 403", statusCode, equalTo(HttpStatus.SC_FORBIDDEN));
        assertThat("Success should be false", isSuccess, equalTo(false));
        assertThat("Message should be 'Email, password and name are required fields'", message, equalTo("Email, password and name are required fields"));
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

    public void deleteUser(UserModel user){
        ValidatableResponse response = deleteUserClient.deleteUser(user);
    }

    public void updateUserPositive(UserModel user, String newName, String newEmail){
        ValidatableResponse response = updateUserClient.updateUser(user);
        int statusCode = response.extract().statusCode();
        boolean isSuccess = response.extract().path("success");
        String newNameActual = response.extract().path("user.name");
        String newEmailActual = response.extract().path("user.email");
        assertThat("Status code should be 200", statusCode, equalTo(HttpStatus.SC_OK));
        assertThat("Success should be true", isSuccess, equalTo(true));
        assertThat("Name should be updated to new one", newNameActual, equalTo(newName));
        assertThat("Email should be updated to new one", newEmailActual, equalTo(newEmail));
    }

    public void updateUserEmailAlreadyExistNegative(UserModel user){
        ValidatableResponse response = updateUserClient.updateUser(user);
        int statusCode = response.extract().statusCode();
        boolean isSuccess = response.extract().path("success");
        String message = response.extract().path("message");
        assertThat("Status code should be 403", statusCode, equalTo(HttpStatus.SC_FORBIDDEN));
        assertThat("Success should be false", isSuccess, equalTo(false));
        assertThat("Message should be 'User with such email already exists'", message, equalTo("User with such email already exists"));
    }

    public void updateUserUnauthorizedNegative(UserModel user){
        ValidatableResponse response = updateUserClient.updateUser(user);
        int statusCode = response.extract().statusCode();
        boolean isSuccess = response.extract().path("success");
        String message = response.extract().path("message");
        assertThat("Status code should be 401", statusCode, equalTo(HttpStatus.SC_UNAUTHORIZED));
        assertThat("Success should be false", isSuccess, equalTo(false));
        assertThat("Message should be 'You should be authorised'", message, equalTo("You should be authorised"));
    }

}
