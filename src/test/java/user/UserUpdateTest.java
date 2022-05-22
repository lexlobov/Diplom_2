package user;

import com.github.javafaker.Faker;
import model.UserModel;
import org.junit.Test;

import java.util.Locale;

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
    public void updateUserInfoTest(){
        steps.createNewUser(user);
        steps.loginUserPositive(email, password);
        steps.updateUserPositive(newUser, newName, newEmail);
    }

    @Test
    public void updateUserInfoWithoutAuthorizationNegativeTest(){
        steps.createNewUser(user);
        steps.loginUserPositive(email, password);
        steps.updateUserUnauthorizedNegative(newUser);
    }
}
