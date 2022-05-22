package user;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import model.UserModel;
import org.junit.Test;

@DisplayName("Различные сценарии создания пользователя")
public class UserRegistrationTest {

    UserSteps steps = new UserSteps();
    Faker faker = new Faker();

    String name = faker.name().firstName();
    String email = name + "@" + faker.name().lastName() + ".ru";
    String password = faker.lorem().characters(10,true);

    UserModel user = new UserModel(email, password, name);

    @Test
    @DisplayName("Создание пользователя, поизитвный сценарий")
    public void registerNewUserPositiveTest(){
        steps.createNewUser(user);
        steps.deleteUser();
    }

    @Test
    @DisplayName("Создание пользователя с уже существующей почтой")
    public void registerNewUserEmailAlreadyExistNegativeTest(){
        steps.createNewUser(user);
        steps.createNewUserAlreadyExistNegative(user);
        steps.deleteUser();
    }
}
