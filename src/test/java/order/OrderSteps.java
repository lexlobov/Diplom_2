package order;

import client.order.CreateOrderClient;
import client.order.GetIngredientsClient;
import client.order.GetUserOrdersClient;
import io.restassured.response.ValidatableResponse;
import model.IngredientModel;
import model.IngredientsCreateModel;
import model.IngredientsModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OrderSteps {

    CreateOrderClient createOrderClient = new CreateOrderClient();
    GetIngredientsClient getIngredientsClient = new GetIngredientsClient();
    GetUserOrdersClient getUserOrdersClient = new GetUserOrdersClient();
    Random random = new Random();

    public void createOrderPositive(String authToken){
        IngredientsModel ingredientsModel = getIngredientsClient.getIngredients();
        IngredientsCreateModel ingredientsCreateModel = new IngredientsCreateModel();
        List<String> ingredientHashes = new ArrayList<>();
        List<String> bunHashes = new ArrayList<>();

        for(IngredientModel ingredient : ingredientsModel.getIngredients()){
            if (ingredient.getType() == "bun"){
                bunHashes.add(ingredient.get_id());
            } else {
                ingredientHashes.add(ingredient.get_id());
            }
        }
        int i = random.nextInt(ingredientHashes.size());
        for (int l=0; l<i; l++){
            ingredientsCreateModel.getIngredients().add(ingredientHashes.get(random.nextInt(ingredientHashes.size()-1)));
        }
        ingredientsCreateModel.getIngredients().add(bunHashes.get(random.nextInt(bunHashes.size()-1)));
        ValidatableResponse response = createOrderClient.createOrder(ingredientsCreateModel, authToken);
        int statusCode = response.extract().statusCode();
        boolean isSuccess = response.extract().path("success");
        int orderNumber = response.extract().path("order.number");

    }

    // TODO доделать метод
}
