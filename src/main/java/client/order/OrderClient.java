package client.order;

import client.BaseHttpConfig;
import io.restassured.response.ValidatableResponse;
import model.IngredientsCreateModel;
import model.IngredientsModel;
import model.OrdersApiResponseModel;

import static io.restassured.RestAssured.given;


public class OrderClient extends BaseHttpConfig {

    private final String orderUri = "/api/orders";
    private final String getIngredientsUri = "/api/ingredients";

    public ValidatableResponse createOrder(IngredientsCreateModel ingredients, String authToken){
        return given().spec(baseSpec())
                .header("authorization", authToken)
                .body(ingredients)
                .when()
                .post(orderUri)
                .then();
    }

    public ValidatableResponse createOrderWithoutAuthorization(IngredientsCreateModel ingredients){
        return given().spec(baseSpec())
                .body(ingredients)
                .when()
                .post(orderUri)
                .then();
    }

    public ValidatableResponse getUserOrders(String authToken){
        return given().spec(baseSpec())
                .header("authorization", authToken)
                .when()
                .get(orderUri)
                .then();
    }

    public OrdersApiResponseModel getUserOrdersAsOrdersClass(String authToken){
        return given().spec(baseSpec())
                .header("authorization", authToken)
                .when()
                .get(orderUri)
                .body().as(OrdersApiResponseModel.class);
    }

    public ValidatableResponse getUserOrdersWithoutAuthorization(){
        return given().spec(baseSpec())
                .when()
                .get(orderUri)
                .then();
    }



    public IngredientsModel getIngredients(){
        return given().spec(baseSpec())
                .get(getIngredientsUri)
                .body().as(IngredientsModel.class);

    }
}
