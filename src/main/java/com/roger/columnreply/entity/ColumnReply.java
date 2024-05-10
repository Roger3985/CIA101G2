package com.roger.columnreply.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.roger.columnarticle.entity.ColumnArticle;
import com.roger.member.entity.Member;
import com.roger.report.entity.Report;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "columnreply")
public class ColumnReply implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "columnreplyno")
    private Integer columnReplyNo;
    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "artno", referencedColumnName = "artno")
    private ColumnArticle columnArticle;
    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "memno", referencedColumnName = "memno")
    private Member member;
    @Column(name = "comcontent")
    private String comContent;
    @Column(name = "comtime")
    private Timestamp comTime;
    @Column(name = "comstat")
    private Byte comStat;
    @JsonBackReference
    @OneToMany(mappedBy = "columnReply", cascade = CascadeType.ALL)
    private Set<Report> reports;

    public Integer getColumnReplyNo() {
        return columnReplyNo;
    }

    public void setColumnReplyNo(Integer columnReplyNo) {
        this.columnReplyNo = columnReplyNo;
    }

    public ColumnArticle getColumnArticle() {
        return columnArticle;
    }

    public void setColumnArticle(ColumnArticle columnArticle) {
        this.columnArticle = columnArticle;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getComContent() {
        return comContent;
    }

    public void setComContent(String comContent) {
        this.comContent = comContent;
    }

    public Timestamp getComTime() {
        return comTime;
    }

    public void setComTime(Timestamp comTime) {
        this.comTime = comTime;
    }

    public Byte getComStat() {
        return comStat;
    }

    public void setComStat(Byte comStat) {
        this.comStat = comStat;
    }

    public Set<Report> getReports() {
        return reports;
    }

    public void setReports(Set<Report> reports) {
        this.reports = reports;
    }
}
