package com.yu.rentalmyfavorite.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.roger.member.entity.Member;
import com.yu.rental.entity.Rental;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Entity  //標示類別為"永續類別"
@Table(name = "rentalmyfavorite")  //此"永續類別"對應到的表格
public class RentalMyFavorite implements Serializable {

    // 直接宣告複合識別類別的屬性 (rentalNo與memNo是複合主鍵)
    @EmbeddedId   //加上@EmbeddedId 標註，必須override此類別的hashcode()、equals()
    private CompositeFavorite compositeFavorite;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd")
//    @JsonBackReference
    @Column(name = "rentalfavtime")
    private Timestamp rentalFavTime;

    @ManyToOne
    @JsonManagedReference
//    @JsonBackReference
    @JoinColumn(name = "rentalno", referencedColumnName = "rentalno", insertable = false, updatable = false)
    private Rental rental;

    @ManyToOne
    @JsonManagedReference
//    @JsonBackReference
    @JoinColumn(name = "memno", referencedColumnName = "memno", insertable = false, updatable = false)
    private Member member;


    // 需要宣告一個有包含複合主鍵屬性的類別，並一定要實作 java.io.Serializable 介面
    @Embeddable
    public static class CompositeFavorite implements Serializable {
        private static final long serialVersionUID = 1L;

        @Column(name = "rentalno")
        private Integer rentalNo;

        @Column(name = "memno")
        private Integer memNo;


        public Integer getRentalNo() {
            return rentalNo;
        }

        public void setRentalNo(Integer rentalNo) {
            this.rentalNo = rentalNo;
        }

        public Integer getMemNo() {
            return memNo;
        }

        public void setMemNo(Integer memNo) {
            this.memNo = memNo;
        }

        // 一定要有無參數建構子
        public CompositeFavorite() {
        }

        public CompositeFavorite(Integer rentalNo, Integer memNo) {
            this.rentalNo = rentalNo;
            this.memNo = memNo;
        }

        // 一定要 override 此類別的 hashCode() 與 equals() 方法！
        @Override
        public int hashCode() {
            return Objects.hash(getRentalNo(), getMemNo());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof RentalMyFavorite.CompositeFavorite that)) return false;
            return Objects.equals(getRentalNo(), that.getRentalNo()) && Objects.equals(getMemNo(), that.getMemNo());
        }
    }


    public RentalMyFavorite(){}

    public RentalMyFavorite(CompositeFavorite compositeFavorite, Timestamp rentalFavTime) {
        this.compositeFavorite = compositeFavorite;
        this.rentalFavTime = rentalFavTime;
    }

    public CompositeFavorite getCompositeKey() {
        return compositeFavorite;
    }

    public void setCompositeKey(CompositeFavorite compositeFavorite) {
        this.compositeFavorite = compositeFavorite;
    }

    public Timestamp getRentalFavTime() {
        return rentalFavTime;
    }

    public void setRentalFavTime(Timestamp rentalFavTime) {
        this.rentalFavTime = rentalFavTime;
    }

    public Rental getRental() {
        return rental;
    }

    public void setRental(Rental rental) {
        this.rental = rental;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

}