package order;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import model.UserModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.UserSteps;

@DisplayName("Различные сценарии получения списка заказов пользователя")
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
    @DisplayName("Получение списка заказов, позитивный сценарий")
    public void getUserOrdersCheckStatusCodeAndSuccessPositiveTest(){
        orderSteps.createOrderPositive(userSteps.getAuthToken());
        orderSteps.getOrdersOfClientAndCheckStatusCodeAndSuccess(userSteps.getAuthToken());
    }

    @Test
    @DisplayName("Получение списка заказов, позитвный сценарий и сравнение заказа в списке с  ранее созданным")
    public void getUserOrdersCheckLastOrderPositiveTest(){
        orderSteps.createOrderPositive(userSteps.getAuthToken());
        orderSteps.getOrdersOfClientAndCheckIfCreatedOrderIsInList(userSteps.getAuthToken());
    }

    @Test
    @DisplayName("Получение списка заказов, негативный сценарий без авторизации")
    public void getUserOrdersUnauthorizedNegativeTest(){
        orderSteps.createOrderPositive(userSteps.getAuthToken());
        orderSteps.getOrdersOfClientWithoutAuthorization();
    }
}
