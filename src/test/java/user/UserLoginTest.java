package user;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.UserModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@DisplayName("Различные сценарии авторизации")
public class UserLoginTest {

    UserSteps steps = new UserSteps();
    Faker faker = new Faker();

    String name = faker.name().firstName();
    String email = name + "@" + faker.name().lastName() + ".ru";
    String password = faker.lorem().characters(10, true);

    UserModel user = new UserModel(email, password, name);

    @Before
    public void setUp(){
        steps.createNewUser(user);
    }

    @After
    public void cleanUp(){
        steps.deleteUser();
    }

    @Test
    @DisplayName("Авторизация пользователя, позитивный сценарий")
    public void userLoginPositiveTest(){
        ValidatableResponse response = steps.loginUserPositive(email, password);
        steps.checkAuthorizationSuccessful(response);
    }

    @Test
    @DisplayName("Авторизация пользователя, негативный сценарий с незаполненным логином")
    public void userLoginWithoutEmailNegativeTest(){
        ValidatableResponse response = steps.loginUserPositive(null, password);
        steps.checkLoginUserNegative(response);
    }

    @Test
    @DisplayName("Авторизация пользователя, негативный сценарий с незаполненным паролем")
    public void userLoginWithoutPasswordNegativeTest(){
        ValidatableResponse response = steps.loginUserPositive(email, null);
        steps.checkLoginUserNegative(response);
    }

    @Test
    @DisplayName("Авторизация пользователя, негативный сценарий с неверным паролем")
    public void userLoginWithWrongPasswordNegativeTest(){
        ValidatableResponse response = steps.loginUserPositive(email, faker.lorem().characters(10, true));
        steps.checkLoginUserNegative(response);
    }

    @Test
    @DisplayName("Авторизация пользователя, негативный сценарий с неверным email")
    public void userLoginWithWrongEmailNegativeTest(){
        ValidatableResponse response = steps.loginUserPositive(faker.name().username() + "@maik.ru", password);
        steps.checkLoginUserNegative(response);
    }
}
