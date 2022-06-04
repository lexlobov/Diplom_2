package order;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.OrderModel;
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
    UserModel user = new UserModel(email, password, userName);

    @Before
    public void setUp(){
        userSteps.createNewUser(user);
    }

    @After
    public void tearDown(){
        userSteps.deleteUser();
    }

    @Test
    @DisplayName("Получение списка заказов, позитивный сценарий")
    public void getUserOrdersCheckStatusCodeAndSuccessPositiveTest(){
        orderSteps.createOrderPositive(userSteps.getAuthToken());
        ValidatableResponse response = orderSteps.getOrdersOfClientAndCheckStatusCodeAndSuccess(userSteps.getAuthToken());
        orderSteps.checkOrdersReturned(response);
    }

    @Test
    @DisplayName("Получение списка заказов, позитвный сценарий и сравнение заказа в списке с  ранее созданным")
    public void getUserOrdersCheckLastOrderPositiveTest(){
        orderSteps.createOrderPositive(userSteps.getAuthToken());
        OrderModel order = orderSteps.getOrdersOfClientAndCheckIfCreatedOrderIsInList(userSteps.getAuthToken());
        orderSteps.checkOrderNumberCorrect(order);
    }

    @Test
    @DisplayName("Получение списка заказов, негативный сценарий без авторизации")
    public void getUserOrdersUnauthorizedNegativeTest(){
        orderSteps.createOrderPositive(userSteps.getAuthToken());
        ValidatableResponse response = orderSteps.getOrdersOfClientWithoutAuthorization();
        orderSteps.checkOrdersOfClientDontReturnWithoutAuthorization(response);
    }
}
