package client.user;

import client.BaseHttpConfig;
import io.restassured.response.ValidatableResponse;
import model.UserModel;

import static io.restassured.RestAssured.given;

public class UpdateUserClient extends BaseHttpConfig {

    private final String updateUserUri = "/api/auth/user";

    public ValidatableResponse updateUser(UserModel user, String authToken){
        return given().spec(baseSpec())
                .body(user)
                .header("authorization", authToken)
                .when()
                .patch(updateUserUri)
                .then();
    }
}
