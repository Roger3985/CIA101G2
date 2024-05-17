package com.roger.member.entity;

import com.chihyun.mycoupon.entity.MyCoupon;
import com.chihyun.servicerecord.entity.ServiceRecord;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.howard.rentalmytrack.entity.RentalMyTrack;
import com.howard.rentalorder.entity.RentalOrder;
import com.iting.cart.entity.Cart;
import com.iting.productmyfavorite.entity.ProductMyFavorite;
import com.iting.productorder.entity.ProductOrder;
import com.firesnoopy.studioorder.entity.StudioOrder;
import com.ren.productreview.entity.ProductReview;
import com.roger.articlecollection.entity.ArticleCollection;
import com.roger.clicklike.entity.ClickLike;
import com.roger.columnreply.entity.ColumnReply;
import com.roger.member.entity.uniqueAnnotation.*;
import com.roger.notice.entity.Notice;
import com.roger.report.entity.Report;
import com.yu.rentalmyfavorite.entity.RentalMyFavorite;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Set;

@Entity // 這個注釋標記 Member 類為一個 JPA 實體（entity）。這意味著這個類將映射到數據庫中的一個表，並表示該類的每個對象（實例）對應於數據庫表中的一行。使用此注釋可以讓 JPA 框架自動管理該類的持久化、查詢、更新和刪除操作。
@Table(name = "member")
public class Member implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Id // 主鍵
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 設置自動增長鍵
    @Column(name = "memno") // 映射到資料庫中的column的memNo
    private Integer memNo;

    @NotBlank(message = "會員姓名: 請勿空白")
    @ValidMemName(groups = {Create.class, CreateWithout.class})
    @Column(name = "memname")
    private String memName;

    @NotBlank(message = "會員帳號: 請勿空白")
    @ValidMemAccount(groups = {Create.class, CreateWithout.class})
    @UniqueMemberAccount(groups = Create.class)
    @Column(name = "memacc", unique = true) //`uniqueAnnotation` 屬性設置為 `true`，表示該列應該具有唯一性約束。這意味著數據庫中的每一個 `memAcc` 值都必須是唯一的，不能有重複的值。
    private String memAcc;

    @NotBlank(message = "會員密碼: 請勿空白")
    @ValidMemPassword(groups = {Create.class})
    @Column(name = "mempwd")
    private String memPwd;

    @NotBlank(message = "會員手機: 請勿空白")
    @ValidMemMobile(groups = {Create.class, CreateWithout.class})
    @UniqueMemberMobile(groups = Create.class)
    @Column(name = "memmob", columnDefinition = "CHAR(10)", unique = true)
    private String memMob;

    @NotNull(message = "請輸入性別")
    @Column(name = "memgender")
    private Byte memGender;

    @NotBlank(message = "會員信箱: 請勿空白!")
    @ValidMemMail(groups = {Create.class, CreateWithout.class})
    @UniqueMemberMail(groups = Create.class)
//    @Email
    @Column(name = "memmail", unique = true)
    private String memMail;

    @NotBlank(message = "會員詳細地址: 請勿空白")
    @Column(name = "memadd")
    private String memAdd;

//    @NotBlank(message = "會員生日: 請勿空白")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd")
    @Column(name = "membd")
    private java.sql.Date memBd;

//    @NotBlank(message = "會員信用卡: 請勿空白")
    @ValidCreditCard(groups = {Create.class, CreateWithout.class})
    @Column(name = "memcard")
    private String memCard;

    @Column(name = "provider")
    private Byte provider;

    @Column(name = "clientid")
    private String clientID;

    @Column(name = "displayname")
    private String displayName;

    @Column(name = "accesstoken")
    private String accessToken;

    @Column(name = "refreshtoken")
    private String refreshToken;

    @Column(name = "tknexpiretime")
    private Timestamp tknExpireTime;

    @Column(name = "creationtime")
    private Timestamp creationTime;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd HH:mm:ss")
//    @NotNull(message = "請輸入加入時間")
    @Column(name = "memberjointime")
    private Timestamp memberJoinTime;

    @Column(name = "memstat")
//    @NotBlank(message = "請輸入驗證狀態")
    private Byte memStat;

    @Column(name = "memsalt")
    private String memSalt;

    @Column(name = "mempic", columnDefinition = "longblob")
    private byte[] memPic;

    @Column(name = "memlogin")
    private Byte memLogin;

    @Column(name = "memlogout")
    private Byte memLogout;

    @Column(name = "memactivetime")
    private Timestamp memActiveTime;

    @JsonBackReference
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private Set<Notice> notices;
    @JsonBackReference
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private Set<MyCoupon> myCoupons;
    @JsonBackReference
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private Set<ProductOrder> productOrders;
    @JsonBackReference
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private Set<ProductMyFavorite> productMyFavorites;
    @JsonBackReference
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private Set<Cart> carts;
    @JsonBackReference
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private Set<ClickLike> clickLikes;
    @JsonBackReference
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private Set<ArticleCollection> articleCollections;
    @JsonBackReference
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private Set<Report> reports;
    @JsonBackReference
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private Set<ColumnReply> columnReplies;
    @JsonBackReference
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private Set<RentalOrder> rentalOrders;
    @JsonBackReference
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private Set<RentalMyTrack> rentalMyTracks;
    @JsonBackReference
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private Set<RentalMyFavorite> rentalMyFavorites;
    @JsonBackReference
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private Set<StudioOrder> studioOrders;
    @JsonBackReference
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private Set<ServiceRecord> serviceRecords;
    @JsonBackReference
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private Set<ProductReview> productReviews;

    public Member() {
    }

    public Member(Integer memNo) {
        this.memNo = memNo;
    }

    public Member(Integer memNo, String memName, String memAcc, String memPwd, String memMob, Byte memGender, String memMail, String memAdd, Date memBd, String memCard, Byte provider, String clientID, String displayName, String accessToken, String refreshToken, Timestamp tknExpireTime, Timestamp creationTime, Timestamp memberJoinTime, Byte memStat) {
        this.memNo = memNo;
        this.memName = memName;
        this.memAcc = memAcc;
        this.memPwd = memPwd;
        this.memMob = memMob;
        this.memGender = memGender;
        this.memMail = memMail;
        this.memAdd = memAdd;
        this.memBd = memBd;
        this.memCard = memCard;
        this.provider = provider;
        this.clientID = clientID;
        this.displayName = displayName;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tknExpireTime = tknExpireTime;
        this.creationTime = creationTime;
        this.memberJoinTime = memberJoinTime;
        this.memStat = memStat;
    }

    public Member(Integer memNo, String memName, String memAcc, String memPwd, String memMob, Byte memGender, String memMail, String memAdd, Date memBd, String memCard, Byte provider, String clientID, String displayName, String accessToken, String refreshToken, Timestamp tknExpireTime, Timestamp creationTime, Timestamp memberJoinTime, Byte memStat, String memSalt, byte[] memPic) {
        this.memNo = memNo;
        this.memName = memName;
        this.memAcc = memAcc;
        this.memPwd = memPwd;
        this.memMob = memMob;
        this.memGender = memGender;
        this.memMail = memMail;
        this.memAdd = memAdd;
        this.memBd = memBd;
        this.memCard = memCard;
        this.provider = provider;
        this.clientID = clientID;
        this.displayName = displayName;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tknExpireTime = tknExpireTime;
        this.creationTime = creationTime;
        this.memberJoinTime = memberJoinTime;
        this.memStat = memStat;
        this.memSalt = memSalt;
        this.memPic = memPic;
    }

    public Member(Integer memNo, String memName, String memAcc, String memPwd, String memMob, Byte memGender, String memMail, String memAdd, Date memBd, String memCard, Byte provider, String clientID, String displayName, String accessToken, String refreshToken, Timestamp tknExpireTime, Timestamp creationTime, Timestamp memberJoinTime, Byte memStat, String memSalt, byte[] memPic, Byte memLogin, Byte memLogout, Timestamp memActiveTime) {
        this.memNo = memNo;
        this.memName = memName;
        this.memAcc = memAcc;
        this.memPwd = memPwd;
        this.memMob = memMob;
        this.memGender = memGender;
        this.memMail = memMail;
        this.memAdd = memAdd;
        this.memBd = memBd;
        this.memCard = memCard;
        this.provider = provider;
        this.clientID = clientID;
        this.displayName = displayName;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tknExpireTime = tknExpireTime;
        this.creationTime = creationTime;
        this.memberJoinTime = memberJoinTime;
        this.memStat = memStat;
        this.memSalt = memSalt;
        this.memPic = memPic;
        this.memLogin = memLogin;
        this.memLogout = memLogout;
        this.memActiveTime = memActiveTime;
    }

    public Integer getMemNo() {
        return memNo;
    }

    public void setMemNo(Integer memNo) {
        this.memNo = memNo;
    }

    public String getMemName() {
        return memName;
    }

    public void setMemName(String memName) {
        this.memName = memName;
    }

    public String getMemAcc() {
        return memAcc;
    }

    public void setMemAcc(String memAcc) {
        this.memAcc = memAcc;
    }

    public String getMemPwd() {
        return memPwd;
    }

    public void setMemPwd(String memPwd) {
        this.memPwd = memPwd;
    }

    public String getMemMob() {
        return memMob;
    }

    public void setMemMob(String memMob) {
        this.memMob = memMob;
    }

    public Byte getMemGender() {
        return memGender;
    }

    public void setMemGender(Byte memGender) {
        this.memGender = memGender;
    }

    public String getMemMail() {
        return memMail;
    }

    public void setMemMail(String memMail) {
        this.memMail = memMail;
    }

    public String getMemAdd() {
        return memAdd;
    }

    public void setMemAdd(String memAdd) {
        this.memAdd = memAdd;
    }

    public Date getMemBd() {
        return memBd;
    }

    public void setMemBd(Date memBd) {
        this.memBd = memBd;
    }

    public String getMemCard() {
        return memCard;
    }

    public void setMemCard(String memCard) {
        this.memCard = memCard;
    }

    public Byte getProvider() {
        return provider;
    }

    public void setProvider(Byte provider) {
        this.provider = provider;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Timestamp getTknExpireTime() {
        return tknExpireTime;
    }

    public void setTknExpireTime(Timestamp tknExpireTime) {
        this.tknExpireTime = tknExpireTime;
    }

    public Timestamp getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Timestamp creationTime) {
        this.creationTime = creationTime;
    }

    public Timestamp getMemberJoinTime() {
        return memberJoinTime;
    }

    public void setMemberJoinTime(Timestamp memberJoinTime) {
        this.memberJoinTime = memberJoinTime;
    }

//    // 在保存之前設置當前時間
//    @PrePersist
//    public void setMemberJoinTime() {
//        if (this.memberJoinTime == null) {
//            this.memberJoinTime = new Timestamp(System.currentTimeMillis());
//        }
//    }

    public Byte getMemStat() {
        return memStat;
    }

    public void setMemStat(Byte memStat) {
        this.memStat = memStat;
    }

    public String getMemSalt() {
        return memSalt;
    }

    public void setMemSalt(String memSalt) {
        this.memSalt = memSalt;
    }

    public byte[] getMemPic() {
        return memPic;
    }

    public void setMemPic(byte[] memPic) {
        this.memPic = memPic;
    }

    public Byte getMemLogin() {
        return memLogin;
    }

    public void setMemLogin(Byte memLogin) {
        this.memLogin = memLogin;
    }

    public Byte getMemLogout() {
        return memLogout;
    }

    public void setMemLogout(Byte memLogout) {
        this.memLogout = memLogout;
    }

    public Timestamp getMemActiveTime() {
        return memActiveTime;
    }

    public void setMemActiveTime(Timestamp memActiveTime) {
        this.memActiveTime = memActiveTime;
    }

    public Set<Notice> getNotices() {
        return notices;
    }

    public void setNotices(Set<Notice> notices) {
        this.notices = notices;
    }

    public Set<MyCoupon> getMyCoupons() {
        return myCoupons;
    }

    public void setMyCoupons(Set<MyCoupon> myCoupons) {
        this.myCoupons = myCoupons;
    }

    public Set<ProductOrder> getProductOrders() {
        return productOrders;
    }

    public void setProductOrders(Set<ProductOrder> productOrders) {
        this.productOrders = productOrders;
    }

    public Set<ProductMyFavorite> getProductMyFavorites() {
        return productMyFavorites;
    }

    public void setProductMyFavorites(Set<ProductMyFavorite> productMyFavorites) {
        this.productMyFavorites = productMyFavorites;
    }

    public Set<Cart> getCarts() {
        return carts;
    }

    public void setCarts(Set<Cart> carts) {
        this.carts = carts;
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

    public Set<Report> getReports() {
        return reports;
    }

    public void setReports(Set<Report> reports) {
        this.reports = reports;
    }

    public Set<ColumnReply> getColumnReplies() {
        return columnReplies;
    }

    public void setColumnReplies(Set<ColumnReply> columnReplies) {
        this.columnReplies = columnReplies;
    }

    public Set<RentalOrder> getRentalOrders() {
        return rentalOrders;
    }

    public void setRentalOrders(Set<RentalOrder> rentalOrders) {
        this.rentalOrders = rentalOrders;
    }

    public Set<RentalMyTrack> getRentalMyTracks() {
        return rentalMyTracks;
    }

    public void setRentalMyTracks(Set<RentalMyTrack> rentalMyTracks) {
        this.rentalMyTracks = rentalMyTracks;
    }

    public Set<RentalMyFavorite> getRentalMyFavorites() {
        return rentalMyFavorites;
    }

    public void setRentalMyFavorites(Set<RentalMyFavorite> rentalMyFavorites) {
        this.rentalMyFavorites = rentalMyFavorites;
    }

    public Set<StudioOrder> getStudioOrders() {
        return studioOrders;
    }

    public void setStudioOrders(Set<StudioOrder> studioOrders) {
        this.studioOrders = studioOrders;
    }

    public Set<ServiceRecord> getServiceRecords() {
        return serviceRecords;
    }

    public void setServiceRecords(Set<ServiceRecord> serviceRecords) {
        this.serviceRecords = serviceRecords;
    }

    public Set<ProductReview> getProductReviews() {
        return productReviews;
    }

    public void setProductReviews(Set<ProductReview> productReviews) {
        this.productReviews = productReviews;
    }
}