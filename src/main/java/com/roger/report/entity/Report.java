package com.roger.report.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ren.administrator.entity.Administrator;
import com.roger.columnreply.entity.ColumnReply;
import com.roger.member.entity.Member;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "report")
public class Report implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reportno")
    private Integer reportNo;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "artreplyno", referencedColumnName = "columnreplyno")
    private ColumnReply columnReply;

    // private Integer artReplyNo;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "memno", referencedColumnName = "memno")
    private Member member;

    // private Integer memNo;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "admno", referencedColumnName = "admno")
    private Administrator administrator;

    // private Integer admNo;

    @Column(name = "reporttime")
    private Timestamp reportTime;

    @Column(name = "reportreason")
    private String reportReason;

    @Column(name = "reporttype")
    private Byte reportType;

    public Integer getReportNo() {
        return reportNo;
    }

    public void setReportNo(Integer reportNo) {
        this.reportNo = reportNo;
    }

    public ColumnReply getColumnReply() {
        return columnReply;
    }

    public void setColumnReply(ColumnReply columnReply) {
        this.columnReply = columnReply;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Administrator getAdministrator() {
        return administrator;
    }

    public void setAdministrator(Administrator administrator) {
        this.administrator = administrator;
    }

    public Timestamp getReportTime() {
        return reportTime;
    }

    public void setReportTime(Timestamp reportTime) {
        this.reportTime = reportTime;
    }

    public String getReportReason() {
        return reportReason;
    }

    public void setReportReason(String reportReason) {
        this.reportReason = reportReason;
    }

    public Byte getReportType() {
        return reportType;
    }

    public void setReportType(Byte reportType) {
        this.reportType = reportType;
    }
}
