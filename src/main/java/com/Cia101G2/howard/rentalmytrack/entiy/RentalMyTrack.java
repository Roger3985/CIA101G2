package com.Cia101G2.howard.rentalmytrack.entiy;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "rentalmytrack")
public class RentalMyTrack implements Serializable {

    @EmbeddedId
    private CompositeTrack compositeTrack;
    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "rentalno", referencedColumnName = "rentalno", insertable = false, updatable = false)
    private Rental rental;
    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "memno", referencedColumnName = "memno", insertable = false, updatable = false)
    private Member member;
    @Column(name = "rentaltracktime")
    private Timestamp rentalTrackTime;
    @Column(name = "exprentaldate")
    private Date expRentalDate;

    @Embeddable
    public static class CompositeTrack implements Serializable {

        @Column(name = "rentalno")
        private Integer rentalNo;

        @Column(name = "memno")
        private Integer memNo;

        public Integer getrentalNo() {
            return rentalNo;
        }

        public void setrentalNo(Integer rentalNo) {
            this.rentalNo = rentalNo;
        }

        public Integer getMemNo() {
            return memNo;
        }

        public void setMemNo(Integer memNo) {
            this.memNo = memNo;
        }

        public CompositeTrack() {
        }

        public CompositeTrack(Integer rentalNo, Integer memNo) {
            this.rentalNo = rentalNo;
            this.memNo = memNo;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CompositeTrack that)) return false;
            return Objects.equals(getrentalNo(), that.getrentalNo()) && Objects.equals(getMemNo(), that.getMemNo());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getrentalNo(), getMemNo());
        }

    } // 內部類別結束

    public RentalMyTrack() {
    }

    public RentalMyTrack(CompositeTrack compositeTrack, Timestamp rentalTrackTime) {
        this.compositeTrack = compositeTrack;
        this.rentalTrackTime = rentalTrackTime;
    }

    public RentalMyTrack(CompositeTrack compositeTrack, Date expRentalDate) {
        this.compositeTrack = compositeTrack;
        this.expRentalDate = expRentalDate;
    }

    public RentalMyTrack(CompositeTrack compositeTrack, Timestamp rentalTrackTime, Date expRentalDate) {
        this.compositeTrack = compositeTrack;
        this.rentalTrackTime = rentalTrackTime;
        this.expRentalDate = expRentalDate;
    }

    public CompositeTrack getCompositeTrack() {
        return compositeTrack;
    }

    public void setCompositeTrack(CompositeTrack compositeTrack) {
        this.compositeTrack = compositeTrack;
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

    public Timestamp getrentalTrackTime() {
        return rentalTrackTime;
    }

    public void setrentalTrackTime(Timestamp rentalTrackTime) {
        this.rentalTrackTime = rentalTrackTime;
    }

    public Date getExpRentalDate() {
        return expRentalDate;
    }

    public void setExpRentalDate(Date expRentalDate) {
        this.expRentalDate = expRentalDate;
    }


    @Override
    public String toString() {
        return "RentalMyTrack{" +
                "rentalNo=" + compositeTrack.getrentalNo() +
                "memNo=" + compositeTrack.getMemNo() +
                ", rentalTrackTime=" + rentalTrackTime +
                ", expRentalDate=" + expRentalDate +
                '}' + "\n";
    }

}
