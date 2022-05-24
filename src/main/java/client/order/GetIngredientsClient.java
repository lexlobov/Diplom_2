package client.order;

import client.BaseHttpConfig;
import io.restassured.response.ValidatableResponse;
import model.IngredientsModel;

import static io.restassured.RestAssured.given;

public class GetIngredientsClient extends BaseHttpConfig {

    private final String getIngredientsUri = "/api/ingredients";

    public IngredientsModel getIngredients(){
        return given().spec(baseSpec())
                .get(getIngredientsUri)
                .body().as(IngredientsModel.class);

    }
}
