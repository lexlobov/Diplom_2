package user;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import model.UserModel;
import org.junit.Test;

@DisplayName("Различные сценарии авторизации")
public class UserLoginTest {

    UserSteps steps = new UserSteps();
    Faker faker = new Faker();

    String name = faker.name().firstName();
    String email = name + "@" + faker.name().lastName() + ".ru";
    String password = faker.lorem().characters(10, true);

    UserModel user = new UserModel(email, password, name);

    @Test
    @DisplayName("Авторизация пользователя, позитивный сценарий")
    public void userLoginPositiveTest(){
        steps.createNewUser(user);
        steps.loginUserPositive(email, password);
        steps.deleteUser();
    }

    @Test
    @DisplayName("Авторизация пользователя, негативный сценарий с незаполненным логином")
    public void userLoginWithoutEmailNegativeTest(){
        steps.createNewUser(user);
        steps.loginUserNegative(null, password);
        steps.deleteUser();
    }

    @Test
    @DisplayName("Авторизация пользователя, негативный сценарий с незаполненным паролем")
    public void userLoginWithoutPasswordNegativeTest(){
        steps.createNewUser(user);
        steps.loginUserNegative(email, null);
        steps.deleteUser();
    }

    @Test
    @DisplayName("Авторизация пользователя, негативный сценарий с неверным паролем")
    public void userLoginWithWrongPasswordNegativeTest(){
        steps.createNewUser(user);
        steps.loginUserNegative(email, faker.lorem().characters(10, true));
        steps.deleteUser();
    }

    @Test
    @DisplayName("Авторизация пользователя, негативный сценарий с неверным email")
    public void userLoginWithWrongEmailNegativeTest(){
        steps.createNewUser(user);
        steps.loginUserNegative(faker.name().username() + "@maik.ru", password);
        steps.deleteUser();
    }
}
