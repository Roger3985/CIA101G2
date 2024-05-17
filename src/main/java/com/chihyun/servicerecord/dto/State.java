package com.chihyun.servicerecord.dto;


import java.util.List;
import java.util.Map;

public class State {

    private String type;

    private String user;

    private List<Map<String, String>> userList;

    public State(String type, String user, List<Map<String, String>> userList) {
        this.type = type;
        this.user = user;
        this.userList = userList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public List<Map<String, String>> getUserList() {
        return userList;
    }

    public void setUserList(List<Map<String, String>> userList) {
        this.userList = userList;
    }
}
