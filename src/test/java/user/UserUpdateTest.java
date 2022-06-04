package user;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.UserModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

@DisplayName("Различные сценарии обновления пользователя")
public class UserUpdateTest {

    UserSteps steps = new UserSteps();
    Faker faker = new Faker();

    String name = faker.name().firstName();
    String email = name + "@" + faker.name().lastName() + ".ru";
    String password = faker.lorem().characters(10, true);

    UserModel user = new UserModel(email, password, name);

    String newName = faker.name().firstName();
    String newEmail = newName.toLowerCase(Locale.ROOT) + "@" + faker.name().lastName().toLowerCase(Locale.ROOT) + ".ru";

    UserModel newUser = new UserModel(newEmail, null, newName);

    @Before
    public void setUp(){
        steps.createNewUser(user);
    }

    @After
    public void cleanUp(){
        steps.deleteUser();
    }

    @Test
    @DisplayName("Обновление пользователя, положительный сценарий")
    public void updateUserInfoTest(){
        steps.loginUserPositive(email, password);
        ValidatableResponse response = steps.updateUserPositive(newName, newEmail);
        steps.checkUserUpdated(response, newName, newEmail);
    }

    @Test
    @DisplayName("Обнволение пользователя, негативный сценарий без авторизационного токена")
    public void updateUserInfoWithoutAuthorizationNegativeTest(){
        steps.loginUserPositive(email, password);
        steps.updateUserUnauthorizedNegative(newUser);
    }

    @Test
    @DisplayName("Обновление пользователя, тест с уже использованной почтой")
    public void updateUserInfoWithUsedEmailNegativeTest(){
        String existingMail = faker.name().lastName() + "@" + faker.name().lastName() + ".ru";
        String newPassword = faker.lorem().characters(10, true);
        UserModel newUserWithExistingEmail = new UserModel(existingMail,
                newPassword,
                faker.name().firstName());
        steps.createNewUser(newUserWithExistingEmail);
        steps.loginUserPositive(email, password);
        ValidatableResponse response = steps.updateUserPositive(newName, existingMail);
        steps.checkUpdateUserEmailAlreadyExistNegative(response);
        steps.loginUserPositive(existingMail, newPassword);
        steps.deleteUser();
        steps.loginUserPositive(email, password);
    }
}
