package client.order;

import client.BaseHttpConfig;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class GetUserOrdersClient extends BaseHttpConfig {

    private final String getUserOrdersUri = "/api/orders";

    private ValidatableResponse getUserOrders(String authToken){
        return given().spec(baseSpec())
                .queryParam("token", authToken)
                .when()
                .get(getUserOrdersUri)
                .then();
    }
}
