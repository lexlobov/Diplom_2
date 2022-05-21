package client.user;

import client.BaseHttpConfig;
import io.restassured.response.ValidatableResponse;
import model.UserModel;

import static io.restassured.RestAssured.given;

public class LoginUserClient extends BaseHttpConfig {

    private final String loginUri = "/api/auth/login";

    public ValidatableResponse loginUser(UserModel user){
        return given().spec(baseSpec())
                .body(user)
                .when()
                .post(loginUri)
                .then();
    }
}
