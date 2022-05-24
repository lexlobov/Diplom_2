package client.order;

import client.BaseHttpConfig;
import io.restassured.response.ValidatableResponse;
import model.IngredientsCreateModel;

import static io.restassured.RestAssured.given;


public class CreateOrderClient extends BaseHttpConfig {

    private final String createOrderUri = "/api/orders";

    private ValidatableResponse createOrder(IngredientsCreateModel ingredients){
        return given().spec(baseSpec())
                .body(ingredients)
                .when()
                .post(createOrderUri)
                .then();
    }
}
