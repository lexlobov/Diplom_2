package order;

import client.order.CreateOrderClient;
import client.order.GetIngredientsClient;
import client.order.GetUserOrdersClient;
import com.github.javafaker.Faker;
import io.restassured.response.ValidatableResponse;
import model.IngredientModel;
import model.IngredientsCreateModel;
import model.IngredientsModel;
import org.apache.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class OrderSteps {

    CreateOrderClient createOrderClient = new CreateOrderClient();
    GetIngredientsClient getIngredientsClient = new GetIngredientsClient();
    GetUserOrdersClient getUserOrdersClient = new GetUserOrdersClient();
    Random random = new Random();

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

    public List<String> getIngredientHashes(IngredientsModel ingredientsModel){
        List<String> ingredientHashes = new ArrayList<>();
        for(IngredientModel ingredient : ingredientsModel.getData()) {
            if (!(Objects.equals(ingredient.getType(), "bun"))) {
                ingredientHashes.add(ingredient.get_id());
            }
        }
        return ingredientHashes;
    }

    public List<String> generateFakeIngredientHashes(){
        List<String> randomBurgerWithFakeHashes = new ArrayList<>();
        Faker faker = new Faker();
        for (int l=0; l<5; l++){
           randomBurgerWithFakeHashes.add(faker.lorem().characters(10, true));
        }
        return randomBurgerWithFakeHashes;
    }

    public List<String> generateBurgerWithRandomIngredients(List<String> ingredientHashes){
        int i = random.nextInt(ingredientHashes.size());
        List<String> randomBurger = new ArrayList<>();
        for (int l=0; l<i; l++){
            randomBurger.add(ingredientHashes.get(random.nextInt(ingredientHashes.size()-1)));
        }
        return randomBurger;
    }

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
        assertThat("Status code should be 200", statusCode, equalTo(HttpStatus.SC_OK));
        assertThat("Success should be true", isSuccess, equalTo(true));
        assertThat("Order number should be not null", orderNumber, notNullValue());
    }

    public void createOrderWithoutAuthorization(){
        String authToken = "";
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

    public void createOrderWithInvalidIngredientHashes(String authToken){
        IngredientsCreateModel ingredientsCreateModel = new IngredientsCreateModel();
        ingredientsCreateModel.setIngredients(generateFakeIngredientHashes());
        ValidatableResponse response = createOrderClient.createOrder(ingredientsCreateModel, authToken);
        int statusCode = response.extract().statusCode();
        assertThat("Status code should be 500", statusCode, equalTo(HttpStatus.SC_INTERNAL_SERVER_ERROR));
    }
    // TODO доделать метод
}
