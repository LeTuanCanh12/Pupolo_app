package com.example.pulopo.model.response;

public class DataLogin {
    private User user;
    private Token token;

    public User getUser() {
        return user;
    }

    public void setUser(User value) {
        this.user = value;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token value) {
        this.token = value;
    }
}
