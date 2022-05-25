package order;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import model.UserModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.UserSteps;

@DisplayName("Различные сценарии создания заказа")
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
    @DisplayName("Создание заказа позитивный сценарий")
    public void createNewOrderPositiveTest(){
        orderSteps.createOrderPositive(userSteps.getAuthToken());
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void createNewOrderWithoutAuthorizationPositiveTest(){
        orderSteps.createOrderWithoutAuthorization();
    }

    @Test
    @DisplayName("Создание заказа, негативный сценрий без ингредиентов")
    public void createOrderWithoutIngredientsNegativeTest(){
        orderSteps.createOrderWithoutIngredients(userSteps.getAuthToken());
    }

    @Test
    @DisplayName("Создание заказа, негативный сценарий с невалидными ингредиентами")
    public void createOrderWithInvalidIngredientHashesNegativeTest(){
        orderSteps.createOrderWithInvalidIngredientHashes(userSteps.getAuthToken());
    }
}
