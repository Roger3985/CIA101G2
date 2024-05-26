package com.chihyun.servicerobot.entity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "servicerobot")
public class ServiceRobot {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "keywordno")
	private Integer keywordNo;

    @NotEmpty(message = "關鍵字不可空白")
    @Column(name = "keywordname")
    private String keywordName;

    @NotEmpty(message = "預設回覆內容不可空白")
    @Column(name = "responsecontent")
    private String responseContent;

    public Integer getKeywordNo() {
        return keywordNo;
    }

    public void setKeywordNo(Integer keywordNo) {
        this.keywordNo = keywordNo;
    }

    public String getKeywordName() {
        return keywordName;
    }

    public void setKeywordName(String keywordName) {
        this.keywordName = keywordName;
    }

    public String getResponseContent() {
        return responseContent;
    }

    public void setResponseContent(String responseContent) {
        this.responseContent = responseContent;
    }
}
