package com.chihyun.servicerecord.dto;


import java.util.List;
import java.util.Map;
import java.util.Set;

public class State {

    private String type;

    private String user;

    private Set<String> userList;

    public State(String type, String user, Set<String> userList) {
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

    public Set<String> getUserList() {
        return userList;
    }

    public void setUserList(Set<String> userList) {
        this.userList = userList;
    }
}
