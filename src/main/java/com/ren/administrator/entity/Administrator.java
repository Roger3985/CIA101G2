package com.ren.administrator.entity;

import com.chihyun.servicerecord.entity.ServiceRecord;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.firesnoopy.studioorder.entity.StudioOrder;
import com.ren.title.entity.Title;
import com.roger.columnarticle.entity.ColumnArticle;
import com.roger.report.entity.Report;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "administrator")
public class Administrator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admno")
    private Integer admNo;
    @NotEmpty(message = "密碼不可空白")
    @Column(name = "admpwd")
    private String admPwd;
    @NotEmpty(message = "管理員名稱不可空白")
    @Column(name = "admname")
    private String admName;
    @NotNull(message = "請選擇在職狀態")
    @Column(name = "admstat")
    private Byte admStat;
    @NotEmpty(message = "請填入信箱!")
    @Email(message = "不符合信箱格式!")
    @Column(name = "admemail")
    private String admEmail;
    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "titleno", referencedColumnName = "titleno")
    private Title title;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd")
    @Column(name = "admhiredate")
    private Date admHireDate;
    @Column(name = "admphoto", columnDefinition = "blob")
    private byte[] admPhoto;
    @Column(name = "admsalt")
    private String admSalt;
    @NotNull(message = "請選擇登入狀態")
    @Column(name = "admlogin")
    private Byte admLogin;
    @NotNull(message = "請選擇登出狀態")
    @Column(name = "admlogout")
    private Byte admLogout;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "admactivetime")
    private Timestamp admActiveTime;

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

    public Administrator(Integer admNo) {
        this.admNo = admNo;
    }

    public Administrator(String admPwd, String admName, Byte admStat, String admEmail, Title title, Date admHireDate, byte[] admPhoto, String admSalt, Byte admLogin, Byte admLogout, Timestamp admActiveTime) {
        this.admPwd = admPwd;
        this.admName = admName;
        this.admStat = admStat;
        this.admEmail = admEmail;
        this.title = title;
        this.admHireDate = admHireDate;
        this.admPhoto = admPhoto;
        this.admSalt = admSalt;
        this.admLogin = admLogin;
        this.admLogout = admLogout;
        this.admActiveTime = admActiveTime;
    }

    public Administrator(Integer admNo, String admPwd, String admName, Byte admStat, String admEmail, Title title, Date admHireDate, byte[] admPhoto, String admSalt, Byte admLogin, Byte admLogout, Timestamp admActiveTime) {
        this.admNo = admNo;
        this.admPwd = admPwd;
        this.admName = admName;
        this.admStat = admStat;
        this.admEmail = admEmail;
        this.title = title;
        this.admHireDate = admHireDate;
        this.admPhoto = admPhoto;
        this.admSalt = admSalt;
        this.admLogin = admLogin;
        this.admLogout = admLogout;
        this.admActiveTime = admActiveTime;
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