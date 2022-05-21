package client;

import io.restassured.response.ValidatableResponse;
import model.UserModel;

import static io.restassured.RestAssured.given;

public class UpdateUserClient extends BaseHttpConfig{

    private final String updateUserUri = "/api/auth/user";

    public ValidatableResponse updateUser(UserModel user){

        return given().spec(baseSpec())
                .body(user)
                .when()
                .patch(updateUserUri)
                .then();
    }
}
