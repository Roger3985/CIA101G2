package com.ren.monitor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Monitor {
    private String messageId;
    private Integer admNo;
    private String admName;
    private Integer titleNo;
    private String message;
    private String messageTime;
    private Boolean readStat;

    public Monitor() {
    }

    public Monitor(String messageId) {
        this.messageId = messageId;
    }

    public Monitor(Integer admNo) {
        this.admNo = admNo;
    }

    public Monitor(String admName, Integer titleNo, String message, String messageTime, Boolean readStat) {
        this.admName = admName;
        this.titleNo = titleNo;
        this.message = message;
        this.messageTime = messageTime;
        this.readStat = readStat;
    }

    public Monitor(Integer admNo, String admName, Integer titleNo, String message, String messageTime, Boolean readStat) {
        this.admNo = admNo;
        this.admName = admName;
        this.titleNo = titleNo;
        this.message = message;
        this.messageTime = messageTime;
        this.readStat = readStat;
    }

    public Monitor(String messageId, Integer admNo, String admName, Integer titleNo, String message, String messageTime, Boolean readStat) {
        this.messageId = messageId;
        this.admNo = admNo;
        this.admName = admName;
        this.titleNo = titleNo;
        this.message = message;
        this.messageTime = messageTime;
        this.readStat = readStat;
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

    public Boolean getReadStat() {
        return readStat;
    }

    public void setReadStat(Boolean readStat) {
        this.readStat = readStat;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
