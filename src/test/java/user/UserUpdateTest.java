package user;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import model.UserModel;
import org.junit.Test;

import java.util.Locale;

@DisplayName("Различные сценарии обновления пользователя")
public class UserUpdateTest {

    UserSteps steps = new UserSteps();
    Faker faker = new Faker();

    String name = faker.name().firstName();
    String email = name + "@maik.ru";
    String password = faker.lorem().characters(10, true);

    UserModel user = new UserModel(email, password, name);

    String newName = faker.name().firstName();
    String newEmail = newName.toLowerCase(Locale.ROOT) + "@maik.ru";

    UserModel newUser = new UserModel(newEmail, null, newName);

    @Test
    @DisplayName("Обновление пользователя, положительный сценарий")
    public void updateUserInfoTest(){
        steps.createNewUser(user);
        steps.loginUserPositive(email, password);
        steps.updateUserPositive(newUser, newName, newEmail);
    }

    @Test
    @DisplayName("Обнволение пользователя, негативный сценарий без авторизационного токена")
    public void updateUserInfoWithoutAuthorizationNegativeTest(){
        steps.createNewUser(user);
        steps.loginUserPositive(email, password);
        steps.updateUserUnauthorizedNegative(newUser);
    }

    @Test
    @DisplayName("Обновление пользователя, тест с уже использованной почтой")
    public void updateUserInfoWithUsedEmailNegativeTest(){
        steps.createNewUser(user);
        String existingMail = faker.name().lastName() + "@maik.ru";
        steps.createNewUser(new UserModel(existingMail,
                faker.lorem().characters(10, true),
                faker.name().firstName()));
        steps.loginUserPositive(email, password);
        steps.updateUserEmailAlreadyExistNegative(newName, existingMail);


    }
}
