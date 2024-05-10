package com.chihyun.mycoupon.entity;

import com.chihyun.coupon.entity.Coupon;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.roger.member.entity.Member;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "mycoupon")
public class MyCoupon {
    @EmbeddedId
    private CompositeCouponMember compositeCouponMember;
    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "coupno", referencedColumnName = "coupno", insertable = false, updatable = false)
    private Coupon coupon ;
    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "memno", referencedColumnName = "memno", insertable = false, updatable = false)
    private Member member;
    @Column(name = "coupusedstat")
    private Byte coupUsedStat;
    @Column(name = "coupinfo")
    private String coupInfo;
    @Column(name = "coupexpdate")
    private Timestamp coupExpDate;

    @Embeddable
    public static class CompositeCouponMember implements Serializable {
        private static final long serialVersionUID = 1L;
        @Column(name = "coupno")
        private Integer coupNo;
        @Column(name = "memno")
        private Integer memNo;

        public Integer getTitleNo() {
            return coupNo;
        }

        public void setTitleNo(Integer coupNo) {
            this.coupNo = coupNo;
        }

        public Integer getAuthFuncNo() {
            return memNo;
        }

        public void setAuthFuncNo(Integer memNo) {
            this.memNo = memNo;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((coupNo == null) ? 0 : coupNo.hashCode());
            result = prime * result + ((memNo == null) ? 0 : memNo.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {

            if (this == obj) {
                return true;
            }

            if (obj != null && getClass() == obj.getClass()) {
                CompositeCouponMember compositeCouponMember = (CompositeCouponMember) obj;
                if (coupNo.equals(compositeCouponMember.coupNo) && memNo.equals(compositeCouponMember.memNo)) {
                    return true;
                }
            }
            return false;
        }
    }

    public CompositeCouponMember getCompositeCouponMember() {
        return compositeCouponMember;
    }

    public void setCompositeCouponMember(CompositeCouponMember compositeCouponMember) {
        this.compositeCouponMember = compositeCouponMember;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Byte getCoupUsedStat() {
        return coupUsedStat;
    }

    public void setCoupUsedStat(Byte coupUsedStat) {
        this.coupUsedStat = coupUsedStat;
    }

    public String getCoupInfo() {
        return coupInfo;
    }

    public void setCoupInfo(String coupInfo) {
        this.coupInfo = coupInfo;
    }

    public Timestamp getCoupExpDate() {
        return coupExpDate;
    }

    public void setCoupExpDate(Timestamp coupExpDate) {
        this.coupExpDate = coupExpDate;
    }
}
