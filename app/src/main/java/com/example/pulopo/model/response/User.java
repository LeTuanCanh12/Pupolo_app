package com.example.pulopo.model.response;

import java.util.List;

public class User {
    private String password;
    private List<Object> chatsReceived;
    private List<Object> chatsSent;
    private long id;
    private String hoTen;
    private String userName;
    private String email;

    public String getPassword() {
        return password;
    }

    public void setPassword(String value) {
        this.password = value;
    }

    public List<Object> getChatsReceived() {
        return chatsReceived;
    }

    public void setChatsReceived(List<Object> value) {
        this.chatsReceived = value;
    }

    public List<Object> getChatsSent() {
        return chatsSent;
    }

    public void setChatsSent(List<Object> value) {
        this.chatsSent = value;
    }

    public long getid() {
        return id;
    }

    public void setid(long value) {
        this.id = value;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String value) {
        this.hoTen = value;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String value) {
        this.userName = value;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String value) {
        this.email = value;
    }
}
