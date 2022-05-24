package order;

import com.github.javafaker.Faker;
import model.UserModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.UserSteps;

public class OrderCreateTest {

    UserSteps userSteps = new UserSteps();
    OrderSteps orderSteps = new OrderSteps();

    Faker faker = new Faker();
    String userName = faker.name().firstName();
    String email = userName + "@" + faker.name().lastName() + ".ru";
    String password = faker.lorem().characters(10, true);
    UserModel uer = new UserModel(email, password, userName);

    @Before
    public void setUp(){
        userSteps.createNewUser(uer);
    }

    @After
    public void tearDown(){
        userSteps.deleteUser();
    }

    @Test
    public void createNewOrderPositiveTest(){
        orderSteps.createOrderPositive(userSteps.getAuthToken());
    }
}
