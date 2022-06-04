package client.user;

import client.BaseHttpConfig;
import io.restassured.response.ValidatableResponse;
import model.UserModel;

import static io.restassured.RestAssured.given;

public class UserClient extends BaseHttpConfig {

    private final String createUserUri = "/api/auth/register";
    private final String userUri = "/api/auth/user";
    private final String loginUri = "/api/auth/login";

    public ValidatableResponse createUser(UserModel user){
        return given().spec(baseSpec())
                .body(user)
                .when()
                .post(createUserUri)
                .then();
    }

    public ValidatableResponse updateUser(UserModel user, String authToken){
        return given().spec(baseSpec())
                .body(user)
                .header("authorization", authToken)
                .when()
                .patch(userUri)
                .then();
    }

    public ValidatableResponse loginUser(UserModel user){
        return given().spec(baseSpec())
                .body(user)
                .when()
                .post(loginUri)
                .then();
        }



    public ValidatableResponse deleteUser(String authToken){
        return given().spec(baseSpec())
                .header("authorization", authToken)
                .when()
                .delete(userUri)
                .then();
    }
}
