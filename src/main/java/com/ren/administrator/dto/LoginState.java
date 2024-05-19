package com.ren.administrator.dto;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.concurrent.atomic.AtomicInteger;

public class LoginState implements Serializable, HttpSessionBindingListener {

    // 管理員登入狀態DTO，用於確認登入狀態
    // 將Service查詢單項資料封包進這個DTO內
    private Integer admNo;

    private String admName;

    private String admEmail;

    private String jsessionid;

    private Byte admLogin;

    private Byte admLogout;

    private Timestamp admActiveTime;

    private Integer titleNo;

    public LoginState() {
    }

    public LoginState(String admName, String admEmail, String jsessionid, Byte admLogin, Byte admLogout, Timestamp admActiveTime, Integer titleNo) {
        this.admName = admName;
        this.admEmail = admEmail;
        this.jsessionid = jsessionid;
        this.admLogin = admLogin;
        this.admLogout = admLogout;
        this.admActiveTime = admActiveTime;
        this.titleNo = titleNo;
    }

    public LoginState(Integer admNo, String admName, String admEmail, String jsessionid, Byte admLogin, Byte admLogout, Timestamp admActiveTime, Integer titleNo) {
        this.admNo = admNo;
        this.admName = admName;
        this.admEmail = admEmail;
        this.jsessionid = jsessionid;
        this.admLogin = admLogin;
        this.admLogout = admLogout;
        this.admActiveTime = admActiveTime;
        this.titleNo = titleNo;
    }

    public LoginState(Integer admNo) {
        this.admNo = admNo;
    }

    public LoginState(String jsessionid) {
        this.jsessionid = jsessionid;
    }

    public LoginState(String admName, String jsessionid, Byte admLogin, Byte admLogout, Timestamp admActiveTime, Integer titleNo) {
        this.admName = admName;
        this.jsessionid = jsessionid;
        this.admLogin = admLogin;
        this.admLogout = admLogout;
        this.admActiveTime = admActiveTime;
        this.titleNo = titleNo;
    }

    public LoginState(Integer admNo, String admName, String jsessionid, Byte admLogin, Byte admLogout, Timestamp admActiveTime, Integer titleNo) {
        this.admNo = admNo;
        this.admName = admName;
        this.jsessionid = jsessionid;
        this.admLogin = admLogin;
        this.admLogout = admLogout;
        this.admActiveTime = admActiveTime;
        this.titleNo = titleNo;
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

    public String getAdmName() {
        return admName;
    }

    public void setAdmName(String admName) {
        this.admName = admName;
    }

    public String getAdmEmail() {
        return admEmail;
    }

    public void setAdmEmail(String admEmail) {
        this.admEmail = admEmail;
    }

    // 在線管理員計數器
    private static final String ONLINE_ADMS = "onlineAdms";

    @Override
    public void valueBound(HttpSessionBindingEvent event) {
        ServletContext context = event.getSession().getServletContext();
        AtomicInteger onlineAdms = (AtomicInteger) context.getAttribute(ONLINE_ADMS);

        if (onlineAdms == null) {
            onlineAdms = new AtomicInteger(0);
            context.setAttribute(ONLINE_ADMS, onlineAdms);
        }
        onlineAdms.incrementAndGet();
        System.out.println("管理員已登入");
    }

    @Override
    public void valueUnbound(HttpSessionBindingEvent event) {
        ServletContext context = event.getSession().getServletContext();
        AtomicInteger onlineAdms = (AtomicInteger) context.getAttribute(ONLINE_ADMS);
        if (onlineAdms != null && onlineAdms.get() > 0) {
            onlineAdms.decrementAndGet();
        }
    }
}