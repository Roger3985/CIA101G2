package com.Cia101G2.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.sql.Date;
import java.util.Set;

@Entity
@Table(name = "administrator")
public class Administrator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admno")
    private Integer admNo;
    @Column(name = "admpwd")
    private String admPwd;
    @Column(name = "admname")
    private String admName;
    @Column(name = "admstat")
    private Byte admStat;
    @Column(name = "admemail")
    private String admEmail;
    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "titleno", referencedColumnName = "titleno")
    private Title title;
    @Column(name = "admhiredate")
    private Date admHireDate;
    @Column(name = "admphoto", columnDefinition = "blob")
    private byte[] admPhoto;
    @Column(name = "admsalt")
    private String admSalt;

    @JsonBackReference
    @OneToMany(mappedBy = "administrator", cascade = CascadeType.ALL)
    private Set<StudioOrder> studioOrders;
    @JsonBackReference
    @OneToMany(mappedBy = "administrator", cascade = CascadeType.ALL)
    private Set<Report> reports;
    @JsonBackReference
    @OneToMany(mappedBy = "administrator", cascade = CascadeType.ALL)
    private Set<ServiceRecord> serviceRecords;
    @JsonBackReference
    @OneToMany(mappedBy = "administrator", cascade = CascadeType.ALL)
    private Set<ColumnArticle> columnArticles;

    public Administrator() {

    }

    public Administrator(Integer admNo, String admPwd, String admName, Byte admStat, String admEmail, Title title, Date admHireDate, byte[] admPhoto, String admSalt, Set<StudioOrder> studioOrders, Set<Report> reports, Set<ServiceRecord> serviceRecords, Set<ColumnArticle> columnArticles) {
        this.admNo = admNo;
        this.admPwd = admPwd;
        this.admName = admName;
        this.admStat = admStat;
        this.admEmail = admEmail;
        this.title = title;
        this.admHireDate = admHireDate;
        this.admPhoto = admPhoto;
        this.admSalt = admSalt;
        this.studioOrders = studioOrders;
        this.reports = reports;
        this.serviceRecords = serviceRecords;
        this.columnArticles = columnArticles;
    }

    public Integer getAdmNo() {
        return admNo;
    }

    public void setAdmNo(Integer admNo) {
        this.admNo = admNo;
    }

    public String getAdmPwd() {
        return admPwd;
    }

    public void setAdmPwd(String admPwd) {
        this.admPwd = admPwd;
    }

    public String getAdmName() {
        return admName;
    }

    public void setAdmName(String admName) {
        this.admName = admName;
    }

    public Byte getAdmStat() {
        return admStat;
    }

    public void setAdmStat(Byte admStat) {
        this.admStat = admStat;
    }

    public String getAdmEmail() {
        return admEmail;
    }

    public void setAdmEmail(String admEmail) {
        this.admEmail = admEmail;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public Date getAdmHireDate() {
        return admHireDate;
    }

    public void setAdmHireDate(Date admHireDate) {
        this.admHireDate = admHireDate;
    }

    public byte[] getAdmPhoto() {
        return admPhoto;
    }

    public void setAdmPhoto(byte[] admPhoto) {
        this.admPhoto = admPhoto;
    }

    public String getAdmSalt() {
        return admSalt;
    }

    public void setAdmSalt(String admSalt) {
        this.admSalt = admSalt;
    }

    public Set<StudioOrder> getStudioOrders() {
        return studioOrders;
    }

    public void setStudioOrders(Set<StudioOrder> studioOrders) {
        this.studioOrders = studioOrders;
    }

    public Set<Report> getReports() {
        return reports;
    }

    public void setReports(Set<Report> reports) {
        this.reports = reports;
    }

    public Set<ServiceRecord> getServiceRecords() {
        return serviceRecords;
    }

    public void setServiceRecords(Set<ServiceRecord> serviceRecords) {
        this.serviceRecords = serviceRecords;
    }

    public Set<ColumnArticle> getColumnArticles() {
        return columnArticles;
    }

    public void setColumnArticles(Set<ColumnArticle> columnArticles) {
        this.columnArticles = columnArticles;
    }
}
