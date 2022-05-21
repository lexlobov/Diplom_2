package client.user;

import client.BaseHttpConfig;
import io.restassured.response.ValidatableResponse;
import model.UserModel;

import static io.restassured.RestAssured.given;

public class CreateUserClient extends BaseHttpConfig {

    private final String createUserUri = "/api/auth/register";

    public ValidatableResponse createUser(UserModel user){
        return given().spec(baseSpec())
                .body(user)
                .when()
                .post(createUserUri)
                .then();
    }
}
