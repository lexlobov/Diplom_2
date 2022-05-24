package client.order;

import client.BaseHttpConfig;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class GetIngredientsClient extends BaseHttpConfig {

    private final String getIngredientsUri = "/api/ingredients";
    private ValidatableResponse getIngredients(){
        return given().spec(baseSpec())
                .when()
                .get(getIngredientsUri)
                .then();
    }
}
