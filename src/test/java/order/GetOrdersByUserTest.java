package order;

import com.github.javafaker.Faker;
import model.UserModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.UserSteps;

public class GetOrdersByUserTest {

    OrderSteps orderSteps = new OrderSteps();
    UserSteps userSteps = new UserSteps();

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
    public void getUserOrdersCheckStatusCodeAndSuccessPositiveTest(){
        orderSteps.getOrdersOfClientAndCheckStatusCodeAndSuccess(userSteps.getAuthToken());
    }

    @Test
    public void getUserOrdersCheckLastOrderPositiveTest(){
        orderSteps.createOrderPositive(userSteps.getAuthToken());
        orderSteps.getOrdersOfClientAndCheckIfCreatedOrderIsInList(userSteps.getAuthToken());
    }
}
