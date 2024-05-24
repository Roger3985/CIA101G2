package com.roger.columnarticle.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ren.administrator.entity.Administrator;
import com.roger.articlecollection.entity.ArticleCollection;
import com.roger.clicklike.entity.ClickLike;
import com.roger.columnarticle.entity.uniqueAnnotation.Create;
import com.roger.columnarticle.entity.uniqueAnnotation.ValidArtContent;
import com.roger.columnarticle.entity.uniqueAnnotation.ValidArtTitle;
import com.roger.columnreply.entity.ColumnReply;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "columnarticle")
public class ColumnArticle implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "artno")
    private Integer artNo;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "admno", referencedColumnName = "admno")
    @NotNull(message = "管理員編號: 請勿空白")
    private Administrator administrator;

//    @Column(name = "admNo")
//    private Integer admNo;

    @Column(name = "arttitle")
    @NotEmpty(message = "專欄標題: 請勿空白!")
    @ValidArtTitle(groups = Create.class)
    private String artTitle;

    @Column(name = "artcontent", columnDefinition = "longtext")
    @ValidArtContent(groups = Create.class)
    @NotEmpty(message = "專欄內容: 請勿空白!")
    private String artContent;

    @Column(name = "arttime")
    @NotNull(message = "最新上架時間: 請勿空白!")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp artTime;

    @Column(name = "artcatno")
    @NotNull(message = "文章分類編號: 請勿空白!")
    private Integer artCatNo;

    @Column(name = "artstat")
    private Byte artStat;

    @JsonBackReference
    @OneToMany(mappedBy = "columnArticle", cascade = CascadeType.ALL)
    private Set<ClickLike> clickLikes;

    @JsonBackReference
    @OneToMany(mappedBy = "columnArticle", cascade = CascadeType.ALL)
    private Set<ArticleCollection> articleCollections;

    @JsonBackReference
    @OneToMany(mappedBy = "columnArticle", cascade = CascadeType.ALL)
    private Set<ColumnReply> columnReplies;

    public Integer getArtNo() {
        return artNo;
    }

    public void setArtNo(Integer artNo) {
        this.artNo = artNo;
    }

    public Administrator getAdministrator() {
        return administrator;
    }

    public void setAdministrator(Administrator administrator) {
        this.administrator = administrator;
    }

    public String getArtTitle() {
        return artTitle;
    }

    public void setArtTitle(String artTitle) {
        this.artTitle = artTitle;
    }

    public String getArtContent() {
        return artContent;
    }

    public void setArtContent(String artContent) {
        this.artContent = artContent;
    }

    public Timestamp getArtTime() {
        return artTime;
    }

    public void setArtTime(Timestamp artTime) {
        this.artTime = artTime;
    }

    public Integer getArtCatNo() {
        return artCatNo;
    }

    public void setArtCatNo(Integer artCatNo) {
        this.artCatNo = artCatNo;
    }

    public Byte getArtStat() {
        return artStat;
    }

    public void setArtStat(Byte artStat) {
        this.artStat = artStat;
    }

    public Set<ClickLike> getClickLikes() {
        return clickLikes;
    }

    public void setClickLikes(Set<ClickLike> clickLikes) {
        this.clickLikes = clickLikes;
    }

    public Set<ArticleCollection> getArticleCollections() {
        return articleCollections;
    }

    public void setArticleCollections(Set<ArticleCollection> articleCollections) {
        this.articleCollections = articleCollections;
    }

    public Set<ColumnReply> getColumnReplies() {
        return columnReplies;
    }

    public void setColumnReplies(Set<ColumnReply> columnReplies) {
        this.columnReplies = columnReplies;
    }
}
