package user;

import client.user.CreateUserClient;
import client.user.DeleteUserClient;
import client.user.LoginUserClient;
import client.user.UpdateUserClient;
import groovy.json.JsonToken;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import model.UserModel;
import org.apache.http.HttpStatus;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class UserSteps {

    CreateUserClient createUserClient = new CreateUserClient();
    LoginUserClient loginUserClient = new LoginUserClient();
    UpdateUserClient updateUserClient = new UpdateUserClient();
    DeleteUserClient deleteUserClient = new DeleteUserClient();

    @Step("Создание пользователя")
    @Description("Проверяются код ответа и флаг success")
    public void createNewUser(UserModel user){
        ValidatableResponse response = createUserClient.createUser(user);
        int statusCode = response.extract().statusCode();
        boolean isSuccess = response.extract().path("success");
        assertThat("Status code should be 200", statusCode, equalTo(HttpStatus.SC_OK));
        assertThat("Success should be true", isSuccess, equalTo(true));
    }

    @Step("Авторизация пользователя")
    @Description("Проверяются код ответа, флаг success и наличие токенов")
    public void loginUserPositive(String email, String password){
        UserModel user = new UserModel(email, password);
        ValidatableResponse response = loginUserClient.loginUser(user);
        int statusCode = response.extract().statusCode();
        boolean isSuccess = response.extract().path("success");
        String accessToken = response.extract().path("accessToken");
        String refreshToken = response.extract().path("refreshToken");
        assertThat("Status code should be 200", statusCode, equalTo(HttpStatus.SC_OK));
        assertThat("Success should be true", isSuccess, equalTo(true));
        assertThat("Access token should be not null", accessToken, notNullValue());
        assertThat("Refresh token should be not null", refreshToken, notNullValue());
    }

    @Step("Негативный сценарий создания пользователя при вводе почты, которая уже зарегистрирована")
    @Description("Проверяются код ответа, флаг success и содержания сообщения от сервера")
    public void createNewUserAlreadyExistNegative(UserModel user){
        ValidatableResponse response = createUserClient.createUser(user);
        int statusCode = response.extract().statusCode();
        boolean isSuccess = response.extract().path("success");
        String message = response.extract().path("message");
        assertThat("Status code should be 403", statusCode, equalTo(HttpStatus.SC_FORBIDDEN));
        assertThat("Success should be false", isSuccess, equalTo(false));
        assertThat("Message should be 'User already exists'", message, equalTo("User already exists"));
    }

    @Step("Негативный сценарий создания пользователя при пропуске обязательных полей")
    @Description("Проверяются код ответа, флаг success и содержания сообщения от сервера")
    public void createNewUserNotEnoughDataNegative(UserModel user){
        ValidatableResponse response = createUserClient.createUser(user);
        int statusCode = response.extract().statusCode();
        boolean isSuccess = response.extract().path("success");
        String message = response.extract().path("message");
        assertThat("Status code should be 403", statusCode, equalTo(HttpStatus.SC_FORBIDDEN));
        assertThat("Success should be false", isSuccess, equalTo(false));
        assertThat("Message should be 'Email, password and name are required fields'", message, equalTo("Email, password and name are required fields"));
    }

    @Step("Негативный сценарий авторизации пользователя")
    @Description("Проверяются код ответа, флаг success и содержания сообщения от сервера")
    public void loginUserNegative(String email, String password){
        UserModel user = new UserModel(email, password);
        ValidatableResponse response = loginUserClient.loginUser(user);
        int statusCode = response.extract().statusCode();
        boolean isSuccess = response.extract().path("success");
        String message = response.extract().path("message");
        assertThat("Status code should be 401", statusCode, equalTo(HttpStatus.SC_UNAUTHORIZED));
        assertThat("Success should be false", isSuccess, equalTo(false));
        assertThat("Message should be 'email or password are incorrect'", message, equalTo("email or password are incorrect"));
    }

    @Step("Удаление пользователя")
    public void deleteUser(UserModel user){
        ValidatableResponse response = deleteUserClient.deleteUser(user);
    }

    @Step("Обновление данных пользователя")
    @Description("Проверяются код ответа, флаг success и обновленное имя и почта в ответе")
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

    @Step("Негатинвный сценраий обновления пользователя с попыткой присовения почты, которая уже зарегистрирована")
    @Description("Проверяются код ответа, флаг success и содержания сообщения от сервера")
    public void updateUserEmailAlreadyExistNegative(UserModel user){
        ValidatableResponse response = updateUserClient.updateUser(user);
        int statusCode = response.extract().statusCode();
        boolean isSuccess = response.extract().path("success");
        String message = response.extract().path("message");
        assertThat("Status code should be 403", statusCode, equalTo(HttpStatus.SC_FORBIDDEN));
        assertThat("Success should be false", isSuccess, equalTo(false));
        assertThat("Message should be 'User with such email already exists'", message, equalTo("User with such email already exists"));
    }

    @Step("Негативный сценарий обновления пользователя, когда нет авторизации")
    @Description("Проверяются код ответа, флаг success и содержания сообщения от сервера")
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
