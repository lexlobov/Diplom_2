package order;

import client.order.CreateOrderClient;
import client.order.GetIngredientsClient;
import client.order.GetUserOrdersClient;
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

    public List<String> getBunHashes(IngredientsModel ingredientsModel) {
        List<String> bunHashes = new ArrayList<>();
        for (IngredientModel ingredient : ingredientsModel.getData()) {
            if (Objects.equals(ingredient.getType(), "bun")) {
                bunHashes.add(ingredient.get_id());
            }
        }
        return bunHashes;
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



    public void createOrderPositive(String authToken){
        IngredientsModel ingredientsModel = getIngredientsClient.getIngredients();
        IngredientsCreateModel ingredientsCreateModel = new IngredientsCreateModel();
        List<String> bunHashes = getBunHashes(ingredientsModel);
        List<String> ingredientHashes = getIngredientHashes(ingredientsModel);
        List<String> randomBurger = new ArrayList<>();

        int i = random.nextInt(ingredientHashes.size());
        for (int l=0; l<i; l++){
            randomBurger.add(ingredientHashes.get(random.nextInt(ingredientHashes.size()-1)));
        }
        randomBurger.add(bunHashes.get(random.nextInt(bunHashes.size()-1)));
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
        IngredientsModel ingredientsModel = getIngredientsClient.getIngredients();
        IngredientsCreateModel ingredientsCreateModel = new IngredientsCreateModel();
        List<String> bunHashes = getBunHashes(ingredientsModel);
        List<String> ingredientHashes = getIngredientHashes(ingredientsModel);
        List<String> randomBurger = new ArrayList<>();
        String authToken = "";

        int i = random.nextInt(ingredientHashes.size());
        for (int l=0; l<i; l++){
            randomBurger.add(ingredientHashes.get(random.nextInt(ingredientHashes.size()-1)));
        }
        randomBurger.add(bunHashes.get(random.nextInt(bunHashes.size()-1)));
        ingredientsCreateModel.setIngredients(randomBurger);
        ValidatableResponse response = createOrderClient.createOrder(ingredientsCreateModel, authToken);
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
        assertThat("Status code should be 400", statusCode, equalTo(HttpStatus.SC_BAD_REQUEST));
        assertThat("Success should be false", isSuccess, equalTo(false));
    }
    // TODO доделать метод
}
