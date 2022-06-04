package user;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
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
        steps.loginUserPositive(email, password);
    }

    @Test
    @DisplayName("Авторизация пользователя, негативный сценарий с незаполненным логином")
    public void userLoginWithoutEmailNegativeTest(){
        steps.loginUserNegative(null, password);
    }

    @Test
    @DisplayName("Авторизация пользователя, негативный сценарий с незаполненным паролем")
    public void userLoginWithoutPasswordNegativeTest(){
        steps.loginUserNegative(email, null);
    }

    @Test
    @DisplayName("Авторизация пользователя, негативный сценарий с неверным паролем")
    public void userLoginWithWrongPasswordNegativeTest(){
        steps.loginUserNegative(email, faker.lorem().characters(10, true));
    }

    @Test
    @DisplayName("Авторизация пользователя, негативный сценарий с неверным email")
    public void userLoginWithWrongEmailNegativeTest(){
        steps.loginUserNegative(faker.name().username() + "@maik.ru", password);
    }
}
