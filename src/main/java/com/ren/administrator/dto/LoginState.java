package com.ren.administrator.dto;

import java.io.Serializable;
import java.sql.Timestamp;

public class LoginState implements Serializable {

    // 管理員登入狀態DTO，用於確認登入狀態
    // 將Service查詢單項資料封包進這個DTO內
    private Integer admNo;

    private String jsessionid;

    private Byte admLogin;

    private Byte admLogout;

    private Timestamp admActiveTime;

    private Integer titleNo;

    public LoginState() {
    }

    public LoginState(Integer admNo) {
        this.admNo = admNo;
    }

    public LoginState(String jsessionid) {
        this.jsessionid = jsessionid;
    }

    public LoginState(Integer admNo, String jsessionid, Byte admLogin, Byte admLogout, Timestamp admActiveTime, Integer titleNo) {
        this.admNo = admNo;
        this.jsessionid = jsessionid;
        this.admLogin = admLogin;
        this.admLogout = admLogout;
        this.admActiveTime = admActiveTime;
        this.titleNo = titleNo;
    }

    public LoginState(Byte admLogin, Byte admLogout, Timestamp admActiveTime, Integer titleNo) {
        this.admLogin = admLogin;
        this.admLogout = admLogout;
        this.admActiveTime = admActiveTime;
        this.titleNo = titleNo;
    }

    public LoginState(String jsessionid, Byte admLogin, Byte admLogout, Timestamp admActiveTime, Integer titleNo) {
        this.jsessionid = jsessionid;
        this.admLogin = admLogin;
        this.admLogout = admLogout;
        this.admActiveTime = admActiveTime;
        this.titleNo = titleNo;
    }

    public String getJsessionid() {
        return jsessionid;
    }

    public void setJsessionid(String jsessionid) {
        this.jsessionid = jsessionid;
    }

    public Byte getAdmLogin() {
        return admLogin;
    }

    public void setAdmLogin(Byte admLogin) {
        this.admLogin = admLogin;
    }

    public Byte getAdmLogout() {
        return admLogout;
    }

    public void setAdmLogout(Byte admLogout) {
        this.admLogout = admLogout;
    }

    public Timestamp getAdmActiveTime() {
        return admActiveTime;
    }

    public void setAdmActiveTime(Timestamp admActiveTime) {
        this.admActiveTime = admActiveTime;
    }

    public Integer getTitleNo() {
        return titleNo;
    }

    public void setTitleNo(Integer titleNo) {
        this.titleNo = titleNo;
    }

    public Integer getAdmNo() {
        return admNo;
    }

    public void setAdmNo(Integer admNo) {
        this.admNo = admNo;
    }
}