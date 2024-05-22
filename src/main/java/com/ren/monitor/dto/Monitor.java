package com.ren.monitor.dto;

public class Monitor {
    private Integer admNo;
    private String admName;
    private Integer titleNo;
    private String message;
    private String messageTime;
    private Boolean isRead;

    public Monitor() {
    }

    public Monitor(Integer admNo) {
        this.admNo = admNo;
    }

    public Monitor(String admName, Integer titleNo, String message, String messageTime) {
        this.admName = admName;
        this.titleNo = titleNo;
        this.message = message;
        this.messageTime = messageTime;
    }

    public Monitor(Integer admNo, String admName, Integer titleNo, String message, String messageTime) {
        this.admNo = admNo;
        this.admName = admName;
        this.titleNo = titleNo;
        this.message = message;
        this.messageTime = messageTime;
    }

    public Monitor(String admName, Integer titleNo, String message, String messageTime, Boolean isRead) {
        this.admName = admName;
        this.titleNo = titleNo;
        this.message = message;
        this.messageTime = messageTime;
        this.isRead = isRead;
    }

    public Monitor(Integer admNo, String admName, Integer titleNo, String message, String messageTime, Boolean isRead) {
        this.admNo = admNo;
        this.admName = admName;
        this.titleNo = titleNo;
        this.message = message;
        this.messageTime = messageTime;
        this.isRead = isRead;
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

    public Integer getTitleNo() {
        return titleNo;
    }

    public void setTitleNo(Integer titleNo) {
        this.titleNo = titleNo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }
}
