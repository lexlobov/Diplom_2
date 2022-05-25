package client.order;

import client.BaseHttpConfig;
import io.restassured.response.ValidatableResponse;
import model.OrderModel;
import model.OrdersApiResponseModel;

import static io.restassured.RestAssured.given;

public class GetUserOrdersClient extends BaseHttpConfig {

    private final String getUserOrdersUri = "/api/orders";

    public ValidatableResponse getUserOrders(String authToken){
        return given().spec(baseSpec())
                .header("authorization", authToken)
                .when()
                .get(getUserOrdersUri)
                .then();
    }

    public OrdersApiResponseModel getUserOrdersAsOrdersClass(String authToken){
        return given().spec(baseSpec())
                .header("authorization", authToken)
                .when()
                .get(getUserOrdersUri)
                .body().as(OrdersApiResponseModel.class);
    }
}
