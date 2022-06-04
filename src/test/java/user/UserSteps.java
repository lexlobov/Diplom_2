package user;

import client.user.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import model.UserModel;
import org.apache.http.HttpStatus;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class UserSteps {

    UserClient userClient = new UserClient();

    private String authToken;

    @Step("Создание пользователя")
    public ValidatableResponse createNewUser(UserModel user){
        ValidatableResponse response = userClient.createUser(user);
        setAuthToken(response.extract().path("accessToken"));
        return response;
    }

    @Step("Проверка корректности работы запроса на регистрацию")
    @Description("Проверяются код ответа и флаг success, а также что присутствуют токены")
    public void checkRegistrationSuccessful(ValidatableResponse response){
        int statusCode = response.extract().statusCode();
        boolean isSuccess = response.extract().path("success");
        String refreshToken = response.extract().path("refreshToken");
        String accessToken = response.extract().path("accessToken");
        assertThat("Status code should be 200", statusCode, equalTo(HttpStatus.SC_OK));
        assertThat("Success should be true", isSuccess, equalTo(true));
        assertThat("Access token should be not null", accessToken, notNullValue());
        assertThat("Refresh token should be not null", refreshToken, notNullValue());

    }

    @Step("Авторизация пользователя")
    public ValidatableResponse loginUserPositive(String email, String password){
        UserModel user = new UserModel(email, password);
        ValidatableResponse response = userClient.loginUser(user);
        setAuthToken(response.extract().path("accessToken"));
        return response;
    }

    @Step("Проверка корректности работы запроса на авторизацию")
    @Description("Проверяются код ответа, флаг success и наличие токенов")
    public void checkAuthorizationSuccessful(ValidatableResponse response){
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
    public void checkNewUserAlreadyExistNegative(ValidatableResponse response){
        int statusCode = response.extract().statusCode();
        boolean isSuccess = response.extract().path("success");
        String message = response.extract().path("message");
        assertThat("Status code should be 403", statusCode, equalTo(HttpStatus.SC_FORBIDDEN));
        assertThat("Success should be false", isSuccess, equalTo(false));
        assertThat("Message should be 'User already exists'", message, equalTo("User already exists"));
    }

    @Step("Негативный сценарий создания пользователя при пропуске обязательных полей")
    @Description("Проверяются код ответа, флаг success и содержания сообщения от сервера")
    public void checkNewUserNotEnoughDataNegative(ValidatableResponse response){
        int statusCode = response.extract().statusCode();
        boolean isSuccess = response.extract().path("success");
        String message = response.extract().path("message");
        assertThat("Status code should be 403", statusCode, equalTo(HttpStatus.SC_FORBIDDEN));
        assertThat("Success should be false", isSuccess, equalTo(false));
        assertThat("Message should be 'Email, password and name are required fields'", message, equalTo("Email, password and name are required fields"));
    }

    @Step("Негативный сценарий авторизации пользователя")
    @Description("Проверяются код ответа, флаг success и содержания сообщения от сервера")
    public void checkLoginUserNegative(ValidatableResponse response){
        int statusCode = response.extract().statusCode();
        boolean isSuccess = response.extract().path("success");
        String message = response.extract().path("message");
        assertThat("Status code should be 401", statusCode, equalTo(HttpStatus.SC_UNAUTHORIZED));
        assertThat("Success should be false", isSuccess, equalTo(false));
        assertThat("Message should be 'email or password are incorrect'", message, equalTo("email or password are incorrect"));
    }

    @Step("Удаление пользователя")
    public ValidatableResponse deleteUser(){
        if (authToken == null) {
            setAuthToken("420");
        }
        return userClient.deleteUser(authToken);
    }

    @Step("Проверка, что пользователь удален")
    public void checkUserDeleted(ValidatableResponse response){
        int statusCode = response.extract().statusCode();
        boolean isSuccess = response.extract().path("success");
        assertThat("Status code should be 200", statusCode, equalTo(HttpStatus.SC_ACCEPTED));
        assertThat("Success should be true", isSuccess, equalTo(true));
    }

    @Step("Обновление данных пользователя")
    @Description("Проверяются код ответа, флаг success и обновленное имя и почта в ответе")
    public ValidatableResponse updateUserPositive(String newName, String newEmail){
        UserModel user = new UserModel(newEmail, null, newName);
        return userClient.updateUser(user, authToken);
    }

    @Step("Проверка, что пользователь обновлен успешно")
    public void checkUserUpdated(ValidatableResponse response, String newName, String newEmail){
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
    public void checkUpdateUserEmailAlreadyExistNegative(ValidatableResponse response){
        int statusCode = response.extract().statusCode();
        boolean isSuccess = response.extract().path("success");
        String message = response.extract().path("message");
        assertThat("Status code should be 403", statusCode, equalTo(HttpStatus.SC_FORBIDDEN));
        assertThat("Success should be false", isSuccess, equalTo(false));
        assertThat("Message should be 'User with such email already exists'", message, equalTo("User with such email already exists"));
    }

    @Step("Негативный сценарий обновления пользователя, когда нет авторизации")
    public ValidatableResponse updateUserUnauthorizedNegative(UserModel user){
        return userClient.updateUser(user, "");

    }

    @Step("Проверка, что нельзя обновить пользователя без авторизации")
    @Description("Проверяются код ответа, флаг success и содержания сообщения от сервера")
    public void checkUnauthorizedUserNotUpdated(ValidatableResponse response){
        int statusCode = response.extract().statusCode();
        boolean isSuccess = response.extract().path("success");
        String message = response.extract().path("message");
        assertThat("Status code should be 401", statusCode, equalTo(HttpStatus.SC_UNAUTHORIZED));
        assertThat("Success should be false", isSuccess, equalTo(false));
        assertThat("Message should be 'You should be authorised'", message, equalTo("You should be authorised"));
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
