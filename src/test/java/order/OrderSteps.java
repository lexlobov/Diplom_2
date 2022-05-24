package order;

import client.order.CreateOrderClient;
import client.order.GetIngredientsClient;
import client.order.GetUserOrdersClient;
import io.restassured.response.ValidatableResponse;
import model.IngredientsCreateModel;
import model.IngredientsModel;

public class OrderSteps {

    CreateOrderClient createOrderClient = new CreateOrderClient();
    GetIngredientsClient getIngredientsClient = new GetIngredientsClient();
    GetUserOrdersClient getUserOrdersClient = new GetUserOrdersClient();

    public void createOrderPositive(){
        IngredientsModel ingredients = getIngredientsClient.getIngredients();
        IngredientsCreateModel ingredientsCreateModel =
        ValidatableResponse response = createOrderClient.createOrder();
    }

    // TODO доделать метод
}
