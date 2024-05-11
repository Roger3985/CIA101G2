package com.roger.member.dto;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 會員登入狀態 DTO，用於確認登入狀態
 * 將 Service 查詢單項資料封包進這個 DTO 內
 */
public class LoginStateMember implements Serializable {

    private Integer memNo;

    private String jessionid;

    private Byte memLogin;

    private Byte memLogout;

    private Timestamp memActiveTime;

    public LoginStateMember() {
    }

    public LoginStateMember(Integer memNo) {
        this.memNo = memNo;
    }

    public LoginStateMember(String jessionid) {
        this.jessionid = jessionid;
    }

    public LoginStateMember(Integer memNo, String jessionid, Byte memLogin, Byte memLogout, Timestamp memActiveTime) {
        this.memNo = memNo;
        this.jessionid = jessionid;
        this.memLogin = memLogin;
        this.memLogout = memLogout;
        this.memActiveTime = memActiveTime;
    }

    public LoginStateMember(Byte memLogin, Byte memLogout, Timestamp memActiveTime) {
        this.memLogin = memLogin;
        this.memLogout = memLogout;
        this.memActiveTime = memActiveTime;
    }



    public Integer getMemNo() {
        return memNo;
    }

    public void setMemNo(Integer memNo) {
        this.memNo = memNo;
    }

    public String getJessionid() {
        return jessionid;
    }

    public void setJessionid(String jessionid) {
        this.jessionid = jessionid;
    }

    public Byte getMemLogin() {
        return memLogin;
    }

    public void setMemLogin(Byte memLogin) {
        this.memLogin = memLogin;
    }

    public Byte getMemLogout() {
        return memLogout;
    }

    public void setMemLogout(Byte memLogout) {
        this.memLogout = memLogout;
    }

    public Timestamp getMemActiveTime() {
        return memActiveTime;
    }

    public void setMemActiveTime(Timestamp memActiveTime) {
        this.memActiveTime = memActiveTime;
    }
}
