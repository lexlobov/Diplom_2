import com.github.javafaker.Faker;
import model.UserModel;
import org.junit.Test;

public class UserLoginTest {

    UserSteps steps = new UserSteps();
    Faker faker = new Faker();

    String name = faker.name().firstName();
    String email = name + "@maik.ru";
    String password = faker.lorem().characters(10, true);
    UserModel user = new UserModel(email, password, name);

    @Test
    public void userLoginPositiveTest(){
        steps.createNewUser(user);
        steps.loginUserPositive(email, password);
    }
}
