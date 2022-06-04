package user;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.UserModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@DisplayName("Разилчные негативные сценарии создания пользователя")
@RunWith(Parameterized.class)
public class UserRegistrationParameterizedNegativeTest {

    public String email;
    public String name;
    public String password;

    UserSteps steps = new UserSteps();

    public UserRegistrationParameterizedNegativeTest(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    @Parameterized.Parameters
    public static Object[][] getData(){
        Faker faker = new Faker();
        return new Object[][]{
                {null, faker.name().firstName(), faker.lorem().characters(10, true)},
                {faker.name().firstName() + "@maik.ru", null, faker.lorem().characters(10, true)},
                {faker.name().firstName() + "@maik.ru", faker.name().firstName(), null},
                {null, null, faker.lorem().characters(10, true)},
                {null, faker.name().firstName(), null},
                {faker.name().firstName() + "@maik.ru", null, null},
                {null, null, null}
        };
    }

    @Test
    @DisplayName("Создание пользователя, негативный параметризованный тест с незаполненными полям")
    public void createNewUserNotEnoughData(){
        UserModel user = new UserModel(email, name, password);
        ValidatableResponse response = steps.createNewUser(user);
        steps.checkNewUserNotEnoughDataNegative(response);
        steps.deleteUser();
    }
}
