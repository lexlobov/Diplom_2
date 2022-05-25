package order;

import client.order.CreateOrderClient;
import client.order.GetIngredientsClient;
import client.order.GetUserOrdersClient;
import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import model.*;
import org.apache.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class OrderSteps {

    private int orderNumber;

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }



    CreateOrderClient createOrderClient = new CreateOrderClient();
    GetIngredientsClient getIngredientsClient = new GetIngredientsClient();
    GetUserOrdersClient getUserOrdersClient = new GetUserOrdersClient();
    Random random = new Random();

    @Step("Получение хэша одной булки из списка всех ингредиентов")
    public String getRandomBun(IngredientsModel ingredientsModel) {
        List<String> bunHashes = new ArrayList<>();
        for (IngredientModel ingredient : ingredientsModel.getData()) {
            if (Objects.equals(ingredient.getType(), "bun")) {
                bunHashes.add(ingredient.get_id());
            }
        }
        String randomBun = bunHashes.get(random.nextInt(bunHashes.size()-1));
        return randomBun;
    }

    @Step("Получение списка хэшей всех ингредиентов, которые не являются булками")
    public List<String> getIngredientHashes(IngredientsModel ingredientsModel){
        List<String> ingredientHashes = new ArrayList<>();
        for(IngredientModel ingredient : ingredientsModel.getData()) {
            if (!(Objects.equals(ingredient.getType(), "bun"))) {
                ingredientHashes.add(ingredient.get_id());
            }
        }
        return ingredientHashes;
    }

    @Step("Создание невалидных хэшей ингредиентов в количестве 4 штук")
    public List<String> generateFakeIngredientHashes(){
        List<String> randomBurgerWithFakeHashes = new ArrayList<>();
        Faker faker = new Faker();
        for (int l=0; l<5; l++){
           randomBurgerWithFakeHashes.add(faker.lorem().characters(10, true));
        }
        return randomBurgerWithFakeHashes;
    }

    @Step("Создание случайного списка ингредиентов, которые пойдут в заказ")
    public List<String> generateBurgerWithRandomIngredients(List<String> ingredientHashes){
        int i = random.nextInt(ingredientHashes.size());
        List<String> randomBurger = new ArrayList<>();
        for (int l=0; l<i; l++){
            randomBurger.add(ingredientHashes.get(random.nextInt(ingredientHashes.size()-1)));
        }
        return randomBurger;
    }

    @Step("Создание заказа")
    @Description("Проверки статус кода, флага success, а также, что номер заказа не null")
    public void createOrderPositive(String authToken){
        IngredientsModel ingredientsModel = getIngredientsClient.getIngredients();
        IngredientsCreateModel ingredientsCreateModel = new IngredientsCreateModel();
        String randomBun = getRandomBun(ingredientsModel);
        List<String> ingredientHashes = getIngredientHashes(ingredientsModel);
        List<String> randomBurger = generateBurgerWithRandomIngredients(ingredientHashes);
        randomBurger.add(randomBun);
        ingredientsCreateModel.setIngredients(randomBurger);

        ValidatableResponse response = createOrderClient.createOrder(ingredientsCreateModel, authToken);
        int statusCode = response.extract().statusCode();
        boolean isSuccess = response.extract().path("success");
        int orderNumber = response.extract().path("order.number");
        setOrderNumber(orderNumber);
        assertThat("Status code should be 200", statusCode, equalTo(HttpStatus.SC_OK));
        assertThat("Success should be true", isSuccess, equalTo(true));
        assertThat("Order number should be not null", orderNumber, notNullValue());
    }

    @Step("Создание заказа без авторизации")
    @Description("Проверки статус кода, флага success, а также, что номер заказа не null")
    public void createOrderWithoutAuthorization(){
        IngredientsModel ingredientsModel = getIngredientsClient.getIngredients();
        IngredientsCreateModel ingredientsCreateModel = new IngredientsCreateModel();
        String randomBun = getRandomBun(ingredientsModel);
        List<String> ingredientHashes = getIngredientHashes(ingredientsModel);
        List<String> randomBurger = generateBurgerWithRandomIngredients(ingredientHashes);
        randomBurger.add(randomBun);
        ingredientsCreateModel.setIngredients(randomBurger);

        ValidatableResponse response = createOrderClient.createOrderWithoutAuthorization(ingredientsCreateModel);
        int statusCode = response.extract().statusCode();
        boolean isSuccess = response.extract().path("success");
        int orderNumber = response.extract().path("order.number");
        assertThat("Status code should be 200", statusCode, equalTo(HttpStatus.SC_OK));
        assertThat("Success should be true", isSuccess, equalTo(true));
        assertThat("Order number should be not null", orderNumber, notNullValue());
    }

    @Step("Создание заказа без ингредиентов")
    @Description("Проверки статус кода, флага success и сообщения")
    public void createOrderWithoutIngredients(String authToken){
        IngredientsCreateModel ingredientsCreateModel = new IngredientsCreateModel();
        ValidatableResponse response = createOrderClient.createOrder(ingredientsCreateModel, authToken);
        int statusCode = response.extract().statusCode();
        boolean isSuccess = response.extract().path("success");
        String message = response.extract().path("message");
        assertThat("Status code should be 400", statusCode, equalTo(HttpStatus.SC_BAD_REQUEST));
        assertThat("Success should be false", isSuccess, equalTo(false));
        assertThat("Message should be 'Ingredient ids must be provided'", message, equalTo("Ingredient ids must be provided"));
    }

    @Step("Создание заказа с невалидными хэшами ингредиентов")
    @Description("Проверка статус кода")
    public void createOrderWithInvalidIngredientHashes(String authToken){
        IngredientsCreateModel ingredientsCreateModel = new IngredientsCreateModel();
        ingredientsCreateModel.setIngredients(generateFakeIngredientHashes());
        ValidatableResponse response = createOrderClient.createOrder(ingredientsCreateModel, authToken);
        int statusCode = response.extract().statusCode();
        assertThat("Status code should be 500", statusCode, equalTo(HttpStatus.SC_INTERNAL_SERVER_ERROR));
    }

    @Step("Получение списка заказов пользователя")
    @Description("Проверки статус кода, флага success")
    public void getOrdersOfClientAndCheckStatusCodeAndSuccess(String authToken){
        ValidatableResponse response = getUserOrdersClient.getUserOrders(authToken);
        int statusCode = response.extract().statusCode();
        boolean isSuccess = response.extract().path("success");
        assertThat("Status code should be 200", statusCode, equalTo(HttpStatus.SC_OK));
        assertThat("Success should be true", isSuccess, equalTo(true));
    }

    @Step("Получение списка заказов пользователя")
    @Description("Проверка того, что номер заказа, который приходит в запросе соответствует номеру, который пришел при создании заказа")
    public void getOrdersOfClientAndCheckIfCreatedOrderIsInList(String authToken)
    {
        OrdersApiResponseModel orders = getUserOrdersClient.getUserOrdersAsOrdersClass(authToken);
        List<OrderModel> orderList = orders.getOrders();
        OrderModel lastOrder = orderList.get(orderList.size()-1);
        int newOrderNumber = lastOrder.getNumber();
        assertThat("Order number should match previously created order", newOrderNumber, equalTo(getOrderNumber()));

    }

    @Step("Получение списка заказов пользователя без авторизации")
    @Description("Проверки статус кода, флага success и сообщения")
    public void getOrdersOfClientWithoutAuthorization(){
        ValidatableResponse response = getUserOrdersClient.getUserOrdersWithoutAuthorization();
        int statusCode = response.extract().statusCode();
        boolean isSuccess = response.extract().path("success");
        String message = response.extract().path("message");
        assertThat("Status code should be 401", statusCode, equalTo(HttpStatus.SC_UNAUTHORIZED));
        assertThat("Success should be false", isSuccess, equalTo(false));
        assertThat("Message should be 'You should be authorised'", message, equalTo("You should be authorised"));
    }
}
