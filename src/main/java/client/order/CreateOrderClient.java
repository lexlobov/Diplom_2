package client.order;

import client.BaseHttpConfig;
import io.restassured.response.ValidatableResponse;
import model.IngredientsCreateModel;

import static io.restassured.RestAssured.given;


public class CreateOrderClient extends BaseHttpConfig {

    private final String createOrderUri = "/api/orders";

    public ValidatableResponse createOrder(IngredientsCreateModel ingredients, String authToken){
        return given().spec(baseSpec())
                .header("authorization", authToken)
                .body(ingredients)
                .when()
                .post(createOrderUri)
                .then();
    }
}
