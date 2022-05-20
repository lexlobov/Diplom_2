package model;

public class UserLoginSuccessApiResponseModel {
    private boolean success;
    private String accessToken;
    private String refreshToken;
    private UserModel user;

    public UserLoginSuccessApiResponseModel(boolean success, String accessToken, String refreshToken, UserModel user) {
        this.success = success;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.user = user;
    }

    public UserLoginSuccessApiResponseModel() {
    }

    public boolean IsSuccess() {
        return success;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public UserModel getUser() {
        return user;
    }
}
