package com.chihyun.news.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;

@Entity
@Table(name = "news")
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "newsno")
    private Integer newsNo;

    @NotBlank(message = "標題請勿空白")
    @Column(name = "newstitle")
    private String newsTitle;

    @NotBlank(message = "內容請勿空白")
    @Column(name = "newscontent", columnDefinition = "longtext")
    private String newsContent;

    @Column(name = "posttime")
    private Timestamp postTime;

    public Integer getNewsNo() {
        return newsNo;
    }

    public void setNewsNo(Integer newsNo) {
        this.newsNo = newsNo;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsContent() {
        return newsContent;
    }

    public void setNewsContent(String newsContent) {
        this.newsContent = newsContent;
    }

    public Timestamp getPostTime() {
        return postTime;
    }

    public void setPostTime(Timestamp postTime) {
        this.postTime = postTime;
    }
}
