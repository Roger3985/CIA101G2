package com.chihyun.news.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "news")
public class News {
    @Id
    @Column(name = "newsno")
    private Integer newsNo;
    @Column(name = "newstitle")
    private String newsTitle;
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
