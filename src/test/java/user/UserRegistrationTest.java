package user;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.UserModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@DisplayName("Различные сценарии создания пользователя")
public class UserRegistrationTest {

    UserSteps steps = new UserSteps();
    Faker faker = new Faker();

    String name = faker.name().firstName();
    String email = name + "@" + faker.name().lastName() + ".ru";
    String password = faker.lorem().characters(10,true);

    UserModel user = new UserModel(email, password, name);

    @After
    public void cleanUp(){
        steps.deleteUser();
    }

    @Test
    @DisplayName("Создание пользователя, поизитвный сценарий")
    public void registerNewUserPositiveTest(){
        ValidatableResponse response = steps.createNewUser(user);
        steps.checkRegistrationSuccessful(response);
    }

    @Test
    @DisplayName("Создание пользователя с уже существующей почтой")
    public void registerNewUserEmailAlreadyExistNegativeTest(){
        steps.createNewUser(user);
        ValidatableResponse response = steps.createNewUser(user);
        steps.checkNewUserAlreadyExistNegative(response);
        steps.loginUserPositive(email, password);
    }
}
