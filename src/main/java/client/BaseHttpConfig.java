package client;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class BaseHttpConfig {

    private final String JSON = "application/json";
    protected final String baseUrl = "https://stellarburgers.nomoreparties.site";

    protected RequestSpecification baseSpec(){
        return new RequestSpecBuilder()
                .setContentType(JSON)
                .setBaseUri(baseUrl)
                .build();
    }
}
