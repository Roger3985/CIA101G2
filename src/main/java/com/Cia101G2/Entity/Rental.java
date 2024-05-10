package com.Cia101G2.Entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;


@Entity  //標示類別為"永續類別"
@Table(name = "rental")  //此"永續類別"對應到的表格
public class Rental {

    @Id //標示為PK
    @Column(name="rentalno")
    private Integer rentalNo;
    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "rentalcatno", referencedColumnName = "rentalcatno") //對應rental的rCatNo
    private RentalCategory rentalCategory;
    @Column(name="rentalname", length=40)
    private String rentalName;
    @Column(name="rentalprice",columnDefinition="BigDecimal")
    private BigDecimal rentalPrice;
    @Column(name="rentalsize")
    private Integer rentalSize;
    @Column(name="rentalcolor", length=10)
    private String rentalColor;
    @Column(name="rentalinfo", length=1000)
    private String rentalInfo;
    @Column(name="rentalstat",columnDefinition = "TINYINT")
    private Byte rentalStat;
//    @JsonBackReference
    @OneToMany(mappedBy = "rental", cascade = CascadeType.ALL)
    private Set<RentalOrderDetails> rentalOrderDetails;
    @JsonBackReference
    @OneToMany(mappedBy = "rental",cascade = CascadeType.ALL)
    private Set<RentalMyFavorite> rentalMyFavorites;
    @JsonBackReference
    @OneToMany(mappedBy = "rental", cascade = CascadeType.ALL)
    private Set<RentalPic> rentalPics;

    @JsonBackReference
    @OneToMany(mappedBy = "rental", cascade = CascadeType.ALL)
    private Set<RentalMyTrack> rentalMyTracks;

    public Rental() {
    }

    public Rental(Integer rentalNo) {
        this.rentalNo = rentalNo;
    }

    public Rental(RentalCategory rentalCategory, String rentalName, BigDecimal rentalPrice, Integer rentalSize, String rentalColor, String rentalInfo, Byte rentalStat) {
        this.rentalCategory = rentalCategory;
        this.rentalName = rentalName;
        this.rentalPrice = rentalPrice;
        this.rentalSize = rentalSize;
        this.rentalColor = rentalColor;
        this.rentalInfo = rentalInfo;
        this.rentalStat = rentalStat;
    }

    public Rental(Integer rentalNo, RentalCategory rentalCategory, String rentalName, BigDecimal rentalPrice, Integer rentalSize, String rentalColor, String rentalInfo, Byte rentalStat) {
        this.rentalNo = rentalNo;
        this.rentalCategory = rentalCategory;
        this.rentalName = rentalName;
        this.rentalPrice = rentalPrice;
        this.rentalSize = rentalSize;
        this.rentalColor = rentalColor;
        this.rentalInfo = rentalInfo;
        this.rentalStat = rentalStat;
    }

    public Integer getrentalNo() {
        return rentalNo;
    }

    public void setrentalNo(Integer rentalNo) {
        this.rentalNo = rentalNo;
    }

    public RentalCategory getRentalCategory() {
        return rentalCategory;
    }

    public void setRentalCategory(RentalCategory rentalCategory) {
        this.rentalCategory = rentalCategory;
    }

    public String getrentalName() {
        return rentalName;
    }

    public void setrentalName(String rentalName) {
        this.rentalName = rentalName;
    }

    public BigDecimal getrentalPrice() {
        return rentalPrice;
    }

    public void setrentalPrice(BigDecimal rentalPrice) {
        this.rentalPrice = rentalPrice;
    }

    public Integer getrentalSize() {
        return rentalSize;
    }

    public void setrentalSize(Integer rentalSize) {
        this.rentalSize = rentalSize;
    }

    public String getrentalColor() {
        return rentalColor;
    }

    public void setrentalColor(String rentalColor) {
        this.rentalColor = rentalColor;
    }

    public String getrentalInfo() {
        return rentalInfo;
    }

    public void setrentalInfo(String rentalInfo) {
        this.rentalInfo = rentalInfo;
    }

    public Byte getrentalStat() {
        return rentalStat;
    }

    public void setrentalStat(Byte rentalStat) {
        this.rentalStat = rentalStat;
    }

    public Set<RentalOrderDetails> getRentalOrderDetails() {
        return rentalOrderDetails;
    }

    public void setRentalOrderDetails(Set<RentalOrderDetails> rentalOrderDetails) {
        this.rentalOrderDetails = rentalOrderDetails;
    }

    public Set<RentalMyFavorite> getRentalMyFavorites() {
        return rentalMyFavorites;
    }

    public void setRentalMyFavorites(Set<RentalMyFavorite> rentalMyFavorites) {
        this.rentalMyFavorites = rentalMyFavorites;
    }

    public Set<RentalPic> getRentalPics() {
        return rentalPics;
    }

    public void setRentalPics(Set<RentalPic> rentalPics) {
        this.rentalPics = rentalPics;
    }

    public Set<RentalMyTrack> getRentalMyTracks() {
        return rentalMyTracks;
    }

    public void setRentalMyTracks(Set<RentalMyTrack> rentalMyTracks) {
        this.rentalMyTracks = rentalMyTracks;
    }

    @Override
    public String toString() {
        return "Rental{" +
                "rentalNo=" + rentalNo +
                ", rentalCategory=" + rentalCategory +
                ", rentalName='" + rentalName + '\'' +
                ", rentalPrice=" + rentalPrice +
                ", rentalSize=" + rentalSize +
                ", rentalColor='" + rentalColor + '\'' +
                ", rentalInfo='" + rentalInfo + '\'' +
                ", rentalStat=" + rentalStat +
                '}';
    }
}