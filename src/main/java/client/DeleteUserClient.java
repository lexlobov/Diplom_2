package client;

import io.restassured.response.ValidatableResponse;
import model.UserModel;

import static io.restassured.RestAssured.given;

public class DeleteUserClient extends BaseHttpConfig{

    private final String deleteUserUri = "/api/auth/user";

    public ValidatableResponse deleteUser(UserModel user){

        return given().spec(baseSpec())
                .body(user)
                .when()
                .delete(deleteUserUri)
                .then();
    }
}
